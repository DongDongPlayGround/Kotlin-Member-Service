package org.study.kotlinmemberservice.member.service

import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.study.kotlinmemberservice.member.Member
import org.study.kotlinmemberservice.member.MemberRole
import org.study.kotlinmemberservice.member.dto.MemberDTO
import org.study.kotlinmemberservice.member.dto.MemberDTOWithRole
import org.study.kotlinmemberservice.member.exception.MemberDomainException
import org.study.kotlinmemberservice.member.mapper.MemberMapper
import org.study.kotlinmemberservice.member.repository.MemberQueryRepository
import org.study.kotlinmemberservice.member.repository.MemberCommandRepository
import java.util.stream.Collectors

@Service
class MemberCommandService(
  private val memberCommandRepository: MemberCommandRepository,
  private val memberQueryRepository: MemberQueryRepository,
  private val memberMapper: MemberMapper,
  private val passwordEncoder: PasswordEncoder,
) {
  
  @Transactional
  fun signUp(memberDTO: MemberDTOWithRole): MemberDTOWithRole {
    if (memberCommandRepository.existsByEmail(memberDTO.email))
      throw MemberDomainException.alreadyExistEmail()
    
    val member = memberCommandRepository.save(
      Member.create(
        memberDTO.email,
        memberDTO.telephone,
        passwordEncoder.encode(memberDTO.password),
        memberDTO.gender,
        memberDTO.birth,
        _getMemberRoleHashSet(_getRoleIdList(memberDTO))
      )
    )
    return memberMapper.toDtoWithRole(member)
  }
  
  private fun _getMemberRoleHashSet(roleIdList: List<Long?>): MutableSet<MemberRole> {
    return memberQueryRepository.findAllRoleByIdList(roleIdList)
      .stream()
      .map { MemberRole.create(it) }
      .collect(Collectors.toSet())
  }
  
  private fun _getRoleIdList(memberDTO: MemberDTOWithRole): List<Long?> {
    return memberDTO.memberRoleDTOSet.stream().map { it.id }.collect(Collectors.toList())
  }
  
  @Transactional
  fun updateMember(id: Long, memberDTO: MemberDTO): MemberDTO {
    val result = memberCommandRepository.findById(id)
      .map { member ->
        member.update(
          passwordEncoder.encode(memberDTO.password),
          memberDTO.gender,
          memberDTO.status,
          memberDTO.birth
        )
      }
      .map { member -> memberMapper.toDto(member) }
      .orElse(null)
    return result
  }
  
  @Transactional
  fun updateMemberRole(id: Long, roleIdList: List<Long>): MemberDTOWithRole {
    val result = memberCommandRepository.findById(id)
      .map { member ->
        member.updateRole(
          _getMemberRoleHashSet(roleIdList)
        )
      }
      .map { member -> memberMapper.toDtoWithRole(member) }
      .orElse(null)
    return result
  }
  
}