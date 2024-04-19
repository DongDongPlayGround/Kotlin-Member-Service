package org.study.kotlinmemberservice.common.authority

data class TokenInfo(
  val grantType: String,
  val accessToken: String,
  val refreshToken: String
)