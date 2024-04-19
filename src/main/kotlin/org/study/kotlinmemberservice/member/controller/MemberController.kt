package org.study.kotlinmemberservice.member.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.study.kotlinmemberservice.member.dto.MemberDTO
import org.study.kotlinmemberservice.member.dto.MemberDTOWithRole
import org.study.kotlinmemberservice.member.dto.MemberRoleDTO
import org.study.kotlinmemberservice.member.service.MemberQueryService
import org.study.kotlinmemberservice.member.service.MemberCommandService

@RestController
@RequestMapping("/member")
class MemberController(
  private val memberService: MemberCommandService,
  private val memberQueryService: MemberQueryService
) {
  
  @PostMapping("")
  @PreAuthorize("hasPermission(#memberDTOWithRole, 'SIGN_UP')")
  fun signUpMember(
    @RequestBody memberDTOWithRole: MemberDTOWithRole
  ): MemberDTOWithRole {
    return memberService.signUp(memberDTOWithRole)
  }
  
  
  @PutMapping("")
  @PreAuthorize("hasPermission(#id, null, 'MEMBER_UPDATE')")
  fun updateMember(
    @RequestParam(name = "id") id: Long,
    @RequestBody memberDTO: MemberDTO
  ): MemberDTO {
    return memberService.updateMember(id, memberDTO)
  }
  
  @PutMapping("/role")
  fun updateMemberRole(
    @RequestParam(name = "id") id: Long,
    @RequestParam(name = "roleIdList") roleIdList: List<Long>
  ): MemberDTOWithRole {
    return memberService.updateMemberRole(id, roleIdList)
  }
  
  @GetMapping("")
  fun getMemberById(
    @RequestParam(name = "id") id: Long
  ): MemberDTO {
    return memberQueryService.getMemberById(id)
  }
  
  @GetMapping("/with-role")
  fun getMemberByIdWithRole(
    @RequestParam(name = "id") id: Long
  ): MemberDTOWithRole {
    return memberQueryService.getMemberByIdWithRole(id)
  }
  
  @GetMapping("/all")
  fun getAllMember(): List<MemberDTO> {
    return memberQueryService.getAllMember()
  }
  
  @GetMapping("/role")
  fun getRoleById(
    @RequestParam(name = "roleId") roleId: Long
  ): MemberRoleDTO {
    return memberQueryService.getRoleById(roleId)
  }
  
  @GetMapping("/role/range")
  fun getAllRoleByIdList(
    @RequestParam(name = "roleIdList") roleIdList: List<Long>
  ): List<MemberRoleDTO> {
    return memberQueryService.getAllRoleByIdList(roleIdList)
  }
  
  @GetMapping("/role/all")
  fun getAllRole(): List<MemberRoleDTO> {
    return memberQueryService.getAllRole()
  }
}