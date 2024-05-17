package org.study.kotlinmemberservice.common.exception

class AuthValidateException(
  val code: String,
  override val message: String
): RuntimeException() {
  
  companion object{
    fun alreadyLogout(): AuthValidateException = AuthValidateException(code = "AUTH-01", message = "로그아웃된 유효하지 않은 엑세스 토큰입니다.")
    fun notExistAuthoritiesKey(): AuthValidateException = AuthValidateException(code = "AUTH-02", message = "AUTHORITIES_KEY 가 없는 잘못된 토큰입니다.")
    fun notExistMemberKey(): AuthValidateException = AuthValidateException(code = "AUTH-03", message = "MEMBER_KEY 가 없는 잘못된 토큰입니다.")
    fun invalidRefreshToken(): AuthValidateException = AuthValidateException(code = "AUTH-04", message = "유효하지 않은 refreshToken 입니다.")
//    fun securityException(): AuthValidateException = AuthValidateException(code = "AUTH-05", message = "시큐리티 규칙을 위반한 예외입니다.")
    fun malformedJwtException(): AuthValidateException = AuthValidateException(code = "AUTH-06", message = "JWT 구조가 올바르지 않습니다.")
    fun expiredJwtException(): AuthValidateException = AuthValidateException(code = "AUTH-07", message = "만료기간이 지난 토큰입니다")
    fun unsupportedJwtException(): AuthValidateException = AuthValidateException(code = "AUTH-08", message = "지원되지 않는 토큰 형식입니다")
    fun signatureException(): AuthValidateException = AuthValidateException(code = "AUTH-09", message = "JWT 서명 실패한 변조된 데이터일 수 있습니다.")
    fun illegalArgumentException(): AuthValidateException = AuthValidateException(code = "AUTH-10", message = "올바르지 않은 매개변수입니다.")
    fun usernameNotFoundException(): AuthValidateException = AuthValidateException(code = "AUTH-11", message = "해당 유저를 찾을 수 없습니다.")
    
  }
  
}