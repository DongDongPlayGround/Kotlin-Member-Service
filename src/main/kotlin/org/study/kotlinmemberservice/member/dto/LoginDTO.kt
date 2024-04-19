package org.study.kotlinmemberservice.member.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class LoginDTO(
  @NotBlank
  @JsonProperty("email")
  private val _email:String?,
  @NotBlank
  @JsonProperty("password")
  private val _password:String?
) {
  val email:String
    get() = _email!!
  val password:String
    get() = _password!!
}