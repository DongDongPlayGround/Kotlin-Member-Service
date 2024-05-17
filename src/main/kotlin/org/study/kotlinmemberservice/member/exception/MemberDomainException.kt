package org.study.kotlinmemberservice.member.exception

import org.study.kotlinmemberservice.common.exception.DomainException

class MemberDomainException(
  code: String,
  message: String
): DomainException(code, message) {
  
  companion object {
    fun alreadyExistEmail(): MemberDomainException = MemberDomainException(code = "MEMBER-01", message = "이미 존재하는 이메일입니다.")
  }
}