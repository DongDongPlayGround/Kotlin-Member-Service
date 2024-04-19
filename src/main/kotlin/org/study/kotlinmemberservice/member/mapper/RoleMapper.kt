package org.study.kotlinmemberservice.member.mapper

import org.springframework.stereotype.Component
import org.study.kotlinmemberservice.member.Role
import org.study.kotlinmemberservice.member.dto.MemberRoleDTO

@Component
class RoleMapper {
  
  fun toDomain(roleDTO: MemberRoleDTO): Role {
    return Role(
      roleDTO.role,
      roleDTO.id
    )
  }
  
  fun toDto(role: Role): MemberRoleDTO {
    return MemberRoleDTO(
      role.id,
      role.roleType
    )
  }
}