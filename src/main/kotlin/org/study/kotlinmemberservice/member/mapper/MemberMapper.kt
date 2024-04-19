package org.study.kotlinmemberservice.member.mapper

import org.springframework.stereotype.Component
import org.study.kotlinmemberservice.member.Member
import org.study.kotlinmemberservice.member.MemberRole
import org.study.kotlinmemberservice.member.dto.MemberDTO
import org.study.kotlinmemberservice.member.dto.MemberDTOWithRole
import org.study.kotlinmemberservice.member.dto.MemberRoleDTO
import java.util.stream.Collectors

@Component
class MemberMapper {
  
  fun toDomain(memberDTO: MemberDTO): Member {
    return Member(
      memberDTO.email,
      memberDTO.telephone,
      memberDTO.password,
      memberDTO.gender,
      memberDTO.status,
      memberDTO.birth,
      memberDTO.id
    )
  }
  
  fun toDto(member: Member): MemberDTO {
    return MemberDTO(
      member.id,
      member.email,
      member.telephone,
      member.password,
      member.gender,
      member.status,
      member.birth,
      member.createdBy,
      member.createdDate,
      member.modifiedBy,
      member.modifiedDate
    )
  }
  fun toDtoWithRole(member: Member): MemberDTOWithRole {
    val memberRoleDTOSet: MutableSet<MemberRoleDTO> = member.memberRoleSet.stream()
      .map { MemberRoleDTO(it.role.id, it.role.roleType) }
      .collect(Collectors.toSet())
    
    return MemberDTOWithRole(
      member.id,
      member.email,
      member.telephone,
      member.password,
      member.gender,
      member.status,
      member.birth,
      member.createdBy,
      member.createdDate,
      member.modifiedBy,
      member.modifiedDate,
      memberRoleDTOSet
    )
  }
}