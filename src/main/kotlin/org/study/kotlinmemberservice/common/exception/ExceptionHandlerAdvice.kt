package org.study.kotlinmemberservice.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.study.kotlinmemberservice.common.config.logger


@RestControllerAdvice(basePackages = ["org.study"])
class ExceptionHandlerAdvice {
  val log = logger()
  
  @ExceptionHandler(AuthValidateException::class)
  fun handleAuthValidateException(e: AuthValidateException): ResponseEntity<ErrorBody> {
    log.error("[AuthValidateException] CODE > ${e.code} || MSG > ${e.message}")
    return ResponseEntity<ErrorBody>(ErrorBody(e.code, e.message), HttpStatus.UNAUTHORIZED)
  }
  
  @ExceptionHandler(DomainException::class)
  fun handleDomainException(e: DomainException): ResponseEntity<ErrorBody> {
    log.error("[DomainException] CODE > ${e.code} || MSG > ${e.message}")
    return ResponseEntity<ErrorBody>(ErrorBody(e.code, e.message), HttpStatus.INTERNAL_SERVER_ERROR)
  }
  
  @ExceptionHandler(BadCredentialsException::class)
  fun handleBadCredentialsException(e: BadCredentialsException): ResponseEntity<ErrorBody> {
    log.error("[BadCredentialsException] CODE > BadCredentialsException || MSG > ${e.message}")
    return ResponseEntity<ErrorBody>(ErrorBody("INVALID", e.message!!), HttpStatus.UNAUTHORIZED)
  }
  
  @ExceptionHandler(RuntimeException::class)
  fun handleRuntimeException(e: RuntimeException): ResponseEntity<ErrorBody> {
    log.error("[RuntimeException] CODE > COMMON || MSG > ${e.message}")
    return ResponseEntity<ErrorBody>(ErrorBody("COMMON", e.message!!), HttpStatus.INTERNAL_SERVER_ERROR)
  }
}

data class ErrorBody(
  val code: String,
  val message: String
)