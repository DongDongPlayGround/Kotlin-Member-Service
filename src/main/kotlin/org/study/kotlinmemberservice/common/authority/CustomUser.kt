package org.study.kotlinmemberservice.common.authority

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class CustomUser(
  val memberId: Long,
  email: String,
  password: String,
  authorities: Collection<GrantedAuthority>
  
): User(email, password, authorities)