package org.study.kotlinmemberservice.member.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.study.kotlinmemberservice.common.authority.TokenInfo
import org.study.kotlinmemberservice.member.dto.LoginDTO
import org.study.kotlinmemberservice.member.service.AuthService

@RestController
@RequestMapping("/auth")
class AuthController(
  private val authService: AuthService
) {
  
  @PostMapping("/login")
  fun login(
    @RequestBody @Valid loginDTO: LoginDTO
  ): TokenInfo {
    return authService.login(loginDTO)
  }
  
  @PostMapping("/logout")
  fun logout(httpServletRequest: HttpServletRequest) {
    return authService.logout(httpServletRequest)
  }
  
  @PostMapping("/reissue")
  fun reissue(httpServletRequest: HttpServletRequest): TokenInfo {
    return authService.reissue(httpServletRequest)
  }
  
  @PostMapping("/reissue/update")
  fun reissueAndUpdate(httpServletRequest: HttpServletRequest): TokenInfo {
    return authService.remake(httpServletRequest)
  }
}