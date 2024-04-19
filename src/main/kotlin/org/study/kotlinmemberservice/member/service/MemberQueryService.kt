package org.study.kotlinmemberservice.member.service

import org.springframework.stereotype.Service
import org.study.kotlinmemberservice.member.dto.MemberDTO
import org.study.kotlinmemberservice.member.dto.MemberDTOWithRole
import org.study.kotlinmemberservice.member.dto.MemberRoleDTO
import org.study.kotlinmemberservice.member.mapper.MemberMapper
import org.study.kotlinmemberservice.member.mapper.RoleMapper
import org.study.kotlinmemberservice.member.repository.MemberQueryRepository
import java.util.stream.Collectors
import javax.print.PrintService

@Service
class MemberQueryService(
  private val queryRepository: MemberQueryRepository,
  private val memberMapper: MemberMapper,
  private val roleMapper: RoleMapper
) {
  
  fun getMemberById(id: Long): MemberDTO {
    val member = queryRepository.findMemberById(id) ?: throw NoSuchElementException()
    return memberMapper.toDto(member)
  }
  
  fun getMemberByIdWithRole(id: Long): MemberDTOWithRole {
    val member = queryRepository.findMemberByIdWithRole(id) ?: throw NoSuchElementException()
    return memberMapper.toDtoWithRole(member)
  }
  
  // TODO : 추후 페이지네이션 추가
  fun getAllMember(): List<MemberDTO> {
    return queryRepository.findAllMembers().stream()
      .map { memberMapper.toDto(it) }
      .collect(Collectors.toList())
  }
  
  fun getRoleById(id: Long): MemberRoleDTO {
    val role = queryRepository.findRoleById(id) ?: throw NoSuchElementException()
    return roleMapper.toDto(role)
  }
  
  fun getAllRole(): List<MemberRoleDTO> {
    return queryRepository.findAllRoles().stream()
      .map { roleMapper.toDto(it) }
      .collect(Collectors.toList())
  }
  
  fun getAllRoleByIdList(idList: List<Long>): List<MemberRoleDTO> {
    return queryRepository.findAllRoleByIdList(idList).stream()
      .map { roleMapper.toDto(it) }
      .collect(Collectors.toList())
  }
}