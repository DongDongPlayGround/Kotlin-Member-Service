package org.study.kotlinmemberservice.common.authority

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.catalina.connector.Response
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.ErrorResponse
import org.springframework.web.servlet.HandlerExceptionResolver
import org.study.kotlinmemberservice.common.config.logger
import org.study.kotlinmemberservice.common.exception.AuthValidateException
import org.study.kotlinmemberservice.common.exception.ErrorBody


@Component
class JwtAuthenticationEntryPoint(
  @Qualifier("handlerExceptionResolver")
  private val resolver: HandlerExceptionResolver
) : AuthenticationEntryPoint {
  
  override fun commence(
    request: HttpServletRequest?,
    response: HttpServletResponse?,
    authException: AuthenticationException?
  ) {
    // JwtAuthenticationFilter에서 request에 담아서 보내준 예외를 처리
    request!!.getAttribute("exception")?.let {
      setErrorResponse(response!!, it as Exception)
    } ?: response!!.sendError(HttpStatus.UNAUTHORIZED.value())
  }
  
  fun setErrorResponse(response: HttpServletResponse, e: Exception) {
    response
      .also { it.contentType = "application/json;charset=UTF-8" }
      .also { it.status = HttpStatus.UNAUTHORIZED.value() }
    
    val objectMapper = ObjectMapper()
    val error: ErrorBody =
      if (e is AuthValidateException) {
        ErrorBody(e.code, e.message)
      } else {
        ErrorBody("COMMON", e.message ?: "인증/인가 과정 중 에러 발생.")
      }
    val result = objectMapper.writeValueAsString(error)
    
    /**
     * 한글 출력을 위해 getWriter() 사용
     */
    response.writer.write(result)
  }
}
