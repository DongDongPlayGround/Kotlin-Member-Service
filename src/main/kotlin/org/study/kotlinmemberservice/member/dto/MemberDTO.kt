package org.study.kotlinmemberservice.member.dto

import org.study.kotlinmemberservice.common.enums.Gender
import org.study.kotlinmemberservice.common.enums.MemberStatus
import org.study.kotlinmemberservice.common.enums.RoleType
import java.time.LocalDate
import java.time.LocalDateTime

data class MemberDTO(
  val id: Long? = null,
  val email: String,
  val telephone: String,
  var password: String,
  var gender: Gender? = null,
  var status: MemberStatus,
  var birth: LocalDate? = null,
  val createdBy: String?,
  val createdDate: LocalDateTime?,
  var modifiedBy: String?,
  var modifiedDate: LocalDateTime?
)

data class MemberDTOWithRole(
  val id: Long? = null,
  val email: String,
  val telephone: String,
  var password: String,
  var gender: Gender? = null,
  var status: MemberStatus,
  var birth: LocalDate? = null,
  var createdBy: String?,
  var createdDate: LocalDateTime?,
  var modifiedBy: String?,
  var modifiedDate: LocalDateTime?,
  val memberRoleDTOSet: MutableSet<MemberRoleDTO>
)

data class MemberRoleDTO(
  val id: Long? = null,
  val role: RoleType
)