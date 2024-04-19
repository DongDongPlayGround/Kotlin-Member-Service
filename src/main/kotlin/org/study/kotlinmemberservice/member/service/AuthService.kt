package org.study.kotlinmemberservice.member.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.transaction.Transactional
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import org.study.kotlinmemberservice.common.authority.JwtTokenProvider
import org.study.kotlinmemberservice.common.authority.TokenInfo
import org.study.kotlinmemberservice.common.enums.HeaderKey
import org.study.kotlinmemberservice.member.Member
import org.study.kotlinmemberservice.common.authority.CustomUser
import org.study.kotlinmemberservice.member.dto.LoginDTO
import org.study.kotlinmemberservice.member.repository.MemberCommandRepository
import java.util.*
import java.util.concurrent.TimeUnit

fun String.blacklistPrefix(): String = let { "blacklist:".plus(it) }
fun String.refreshPrefix(): String = let { "refreshToken:".plus(it) }

@Service
class AuthService(
  private val jwtTokenProvider: JwtTokenProvider,
  private val authenticationManagerBuilder: AuthenticationManagerBuilder,
  private val memberCommandRepository: MemberCommandRepository,
  private val redisTemplate: RedisTemplate<String, String>
) {
  
  @Transactional
  fun login(loginDTO: LoginDTO): TokenInfo {
    val authenticationToken = UsernamePasswordAuthenticationToken(loginDTO.email, loginDTO.password)
    // loadUserByName 메소드를 통해 실제 db 의 정보와 비교 후 authenticate 반환
    val authenticate = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
    
    // 위 로직에서 정상으로 인증되면 createToken 실시
    return jwtTokenProvider.createToken(authenticate)
  }
  
  fun logout(httpServletRequest: HttpServletRequest) {
    val accessToken = _getAccessTokenFrom(httpServletRequest)
    
    // logout 한 accessToken 를 블랙리스트 처리
    redisTemplate.opsForValue().set(
      accessToken.blacklistPrefix(),
      "logout",
      jwtTokenProvider.getExpiration(accessToken).time - Date().time,
      TimeUnit.MILLISECONDS
    )
    
    // refreshToken 레디스에서 삭제
    redisTemplate.delete(
      jwtTokenProvider.getAuthentication(accessToken).name.refreshPrefix()
    )
  }
  
  fun reissue(httpServletRequest: HttpServletRequest): TokenInfo {
    
    val authentication: Authentication = jwtTokenProvider.getAuthentication(_getAccessTokenFrom(httpServletRequest))
    val refreshToken =
      jwtTokenProvider.resolveToken(httpServletRequest, HeaderKey.REFRESHTOKEN.value) ?: _getRefreshTokenWhenIsNull(
        authentication
      )
    
    if (!jwtTokenProvider.validateToken(refreshToken!!))
      throw RuntimeException("유효하지 않은 refreshToken 입니다")
    
    return TokenInfo(
      "Bearer",
      jwtTokenProvider.generateAccessToken(authentication),
      refreshToken
    )
  }
  
  fun remake(httpServletRequest: HttpServletRequest): TokenInfo {
    
    val authentication: Authentication = jwtTokenProvider.getAuthentication(_getAccessTokenFrom(httpServletRequest))
    val refreshToken =
      jwtTokenProvider.resolveToken(httpServletRequest, HeaderKey.REFRESHTOKEN.value) ?: _getRefreshTokenWhenIsNull(
        authentication
      )
    
    if (!jwtTokenProvider.validateToken(refreshToken!!))
      throw RuntimeException("유효하지 않은 refreshToken 입니다")
    // authentication
    val member: Member = memberCommandRepository.findById(
      (authentication.principal as CustomUser).memberId
    ).orElseThrow { NoSuchElementException() }
    
    return TokenInfo(
      "Bearer",
      jwtTokenProvider.generateAccessToken(_getNewAuthenticationFrom(authentication, member)),
      refreshToken
    )
  }
  
  private fun _getNewAuthenticationFrom(oldAuthentication: Authentication, member: Member) =
    UsernamePasswordAuthenticationToken(
      CustomUser(
        member.id!!,
        member.email,
        member.password,
        member.memberRoleSet.map { SimpleGrantedAuthority(it.role.roleType.name) }),
      oldAuthentication.credentials,
      member.memberRoleSet.map { SimpleGrantedAuthority(it.role.roleType.name) })
  
  
  private fun _getAccessTokenFrom(httpServletRequest: HttpServletRequest) =
    jwtTokenProvider.resolveToken(httpServletRequest, HeaderKey.AUTHORIZATION.value)!!
  
  private fun _getRefreshTokenWhenIsNull(authentication: Authentication): String? =
    if (redisTemplate.hasKey(authentication.name.refreshPrefix()))
      redisTemplate.opsForValue().get(authentication.name.refreshPrefix()) else null
  
}