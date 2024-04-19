package org.study.kotlinmemberservice.common.authority

import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import org.study.kotlinmemberservice.member.dto.MemberDTOWithRole
import java.io.Serializable

@Component
class CustomPermissionEvaluator: PermissionEvaluator {
  
  override fun hasPermission(
    authentication: Authentication?,
    targetDomainObject: Any?,
    permission: Any?
  ): Boolean {
    val customUser: CustomUser = authentication?.principal as CustomUser
    return when(targetDomainObject){
      is MemberDTOWithRole -> customUser.authorities.contains(SimpleGrantedAuthority(permission as String))
      else -> false
    }
  }
  
  override fun hasPermission(
    authentication: Authentication?,
    targetId: Serializable?,
    targetType: String?,
    permission: Any?
  ): Boolean {
    val customUser: CustomUser = authentication?.principal as CustomUser
    if (customUser.memberId != targetId) throw RuntimeException("본인만 접근 가능")
    
    return customUser.authorities.contains(SimpleGrantedAuthority(permission as String))
  }
}
