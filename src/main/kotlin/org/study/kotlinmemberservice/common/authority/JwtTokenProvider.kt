package org.study.kotlinmemberservice.common.authority

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.study.kotlinmemberservice.common.exception.AuthValidateException
import org.study.kotlinmemberservice.member.service.refreshPrefix
import java.util.Date
import java.util.concurrent.TimeUnit

const val AUTHORITIES_KEY: String = "auth"
const val MEMBER_KEY: String = "memberId"
const val ACCESS_EXPIRATION_MILLISECONDS: Long = 1000 * 60 * 30 // 30분
const val REFRESH_EXPIRATION_MILLISECONDS: Long = 1000 * 60 * 60 * 24 // 1일

@Component
class JwtTokenProvider(
  private val redisTemplate: RedisTemplate<String, String>
) {
  
  @Value("\${jwt.secret}")
  lateinit var secretKey: String // var 의 lazy 초기화
  
  private val key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)) } // val 의 lazy 초기화
  
  /*token 생성*/
  fun createToken(authentication: Authentication): TokenInfo {
    return TokenInfo(
      "Bearer",
      generateAccessToken(authentication),
      generateRefreshToken(authentication)
    )
  }
  
  fun generateAccessToken(authentication: Authentication): String {
    val authorities = authentication.authorities
      .joinToString(",", transform = GrantedAuthority::getAuthority)
    val now = Date()
    val accessExpiration = Date(now.time + ACCESS_EXPIRATION_MILLISECONDS)
    // Access Token
    return Jwts.builder()
      .setSubject(authentication.name)
      .claim(AUTHORITIES_KEY, authorities)
      .claim(MEMBER_KEY, (authentication.principal as CustomUser).memberId)
      .setIssuedAt(now)
      .setExpiration(accessExpiration)
      .signWith(key, SignatureAlgorithm.HS256)
      .compact()
  }
  
  fun generateRefreshToken(authentication: Authentication): String {
    
    val now = Date()
    val refreshExpiration = Date(now.time + REFRESH_EXPIRATION_MILLISECONDS)
    // Refresh Token
    val refreshToken = Jwts.builder()
      .claim(MEMBER_KEY, (authentication.principal as CustomUser).memberId)
      .setExpiration(refreshExpiration)
      .signWith(key, SignatureAlgorithm.HS256)
      .compact()
    
    redisTemplate.opsForValue().set(
      authentication.name.refreshPrefix(),
      refreshToken,
      refreshExpiration.time,
      TimeUnit.MILLISECONDS
    )
    
    return refreshToken
  }
  
  /*token 추출*/
  fun getAuthentication(token: String): Authentication {
    val claims: Claims = _getClaims(token)
    val auth = claims[AUTHORITIES_KEY] ?: throw AuthValidateException.notExistAuthoritiesKey()
    val memberId = claims[MEMBER_KEY] ?: throw AuthValidateException.notExistMemberKey()
    val authorities: Collection<GrantedAuthority> = (auth as String).split(",")
      .map { SimpleGrantedAuthority(it) }
    val principal: UserDetails = CustomUser(memberId.toString().toLong(), claims.subject, "", authorities)
    return UsernamePasswordAuthenticationToken(principal, "", authorities)
  }
  
  /*token 검증*/
  fun validateToken(token: String): Boolean {
    try {
      _getClaims(token)
      return true
    } catch (e: Exception) {
      when (e) {
        is MalformedJwtException -> {throw AuthValidateException.malformedJwtException()}
        is ExpiredJwtException -> {throw AuthValidateException.expiredJwtException()}
        is UnsupportedJwtException -> {throw AuthValidateException.unsupportedJwtException()} // Signed Claims JWSs are not supported.
        is SignatureException -> {throw AuthValidateException.signatureException()} // Signed Claims JWSs are not supported.
        is IllegalArgumentException -> {throw AuthValidateException.illegalArgumentException()} // jwt claims string is empty
        else -> {throw AuthValidateException("AUTH", "token 검증 중 에러가 발생했습니다.")}
      }
    }
  }
  
  /*token resolve*/
  fun resolveToken(httpServletRequest: HttpServletRequest, targetKey: String): String? {
    val bearerToken = httpServletRequest.getHeader(targetKey)
    return if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
      bearerToken.substring(7)
    } else if(StringUtils.hasText(bearerToken) && !bearerToken.startsWith("Bearer")){
      bearerToken
    }
    else null
  }
  
  fun getExpiration(token: String): Date = _getClaims(token).expiration
  
  private fun _getClaims(token: String): Claims =
    Jwts.parserBuilder()
      .setSigningKey(key)
      .build()
      .parseClaimsJws(token)
      .body
}