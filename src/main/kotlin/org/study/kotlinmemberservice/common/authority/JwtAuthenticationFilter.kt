package org.study.kotlinmemberservice.common.authority

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import org.study.kotlinmemberservice.member.service.blacklistPrefix

/*GenericFilterBean 과의 차이
* GenericFilterBean 은 특정 경우 스레드 중복 시 한 스레드가 authentication 을 여러번 하는 반면
* OncePerRequestFilter 는 한 스레드는 한번만 authentication 하도록 보장
*/
@Component
class JwtAuthenticationFilter(
  private val jwtTokenProvider: JwtTokenProvider,
  private val redisTemplate: RedisTemplate<String, String>
): OncePerRequestFilter() {
  
  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain
  ) {
    val token = jwtTokenProvider.resolveToken(request, "Authorization")
    validateBlackList(token)
    
    if(token!=null && jwtTokenProvider.validateToken(token)){
      val authentication = jwtTokenProvider.getAuthentication(token)
      SecurityContextHolder.getContext().authentication = authentication
    }
    filterChain.doFilter(request, response)
  }
  
  
  /*expire 시간 지난 다음 어떻게 수행되는지 확인 필요*/
  private fun validateBlackList(accessToken: String?) {
    
    if (accessToken != null && redisTemplate.hasKey(accessToken.blacklistPrefix())) {
      throw RuntimeException("로그아웃된 유효하지 않은 엑세스 토큰입니다.")
    }
  }
}