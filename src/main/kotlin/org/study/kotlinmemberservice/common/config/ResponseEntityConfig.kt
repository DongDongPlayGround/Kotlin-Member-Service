package org.study.kotlinmemberservice.common.config

import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice(basePackages = ["org.study"])
class ResponseEntityConfig : ResponseBodyAdvice<Any> {
  
  /*언제 객체를 감싸서 보낼지에 대한 메소드. true 시 감싸서 보낸다*/
  override fun supports(
    returnType: MethodParameter,
    converterType: Class<out HttpMessageConverter<*>>
  ): Boolean {
    return !returnType.method.toString().contains("ExceptionHandlerAdvice")
  }
  
  override fun beforeBodyWrite(
    body: Any?,
    returnType: MethodParameter,
    selectedContentType: MediaType,
    selectedConverterType: Class<out HttpMessageConverter<*>>,
    request: ServerHttpRequest,
    response: ServerHttpResponse
  ): Any? {
    
    val status: HttpStatus? = HttpStatus.resolve((response as ServletServerHttpResponse).servletResponse.status)
    return status?.equals(HttpStatus.OK)?.let {
      if(it) ResponseEntity<Any>(body, status)
      else ResponseEntity<Any>(status)
    }
  }
  
}