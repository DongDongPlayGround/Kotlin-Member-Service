package org.study.kotlinmemberservice.member

import jakarta.persistence.*
import org.study.kotlinmemberservice.common.BaseEntity
import org.study.kotlinmemberservice.common.enums.Gender
import org.study.kotlinmemberservice.common.enums.MemberStatus
import java.time.LocalDate

@Entity
@Table(
  uniqueConstraints = [UniqueConstraint(name = "uk_member_email", columnNames = ["email"])]
)
class Member(
  @Column(nullable = false, updatable = false)
  val email: String,
  
  val telephone: String,
  
  var password: String,
  
  @Column(columnDefinition = "varchar(255)")
  @Enumerated(EnumType.STRING)
  var gender: Gender? = null,
  
  @Column(columnDefinition = "varchar(255)")
  @Enumerated(EnumType.STRING)
  var status: MemberStatus,
  
  var birth: LocalDate? = null,
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null
) : BaseEntity() {
  
  @OneToMany(mappedBy = "member", cascade = [CascadeType.ALL], orphanRemoval = true)
  var memberRoleSet: MutableSet<MemberRole> = mutableSetOf()
  
  companion object {
    fun create(
      email: String,
      telephone: String,
      password: String,
      gender: Gender?,
      birth: LocalDate?,
      memberRole: MutableSet<MemberRole>
    ): Member {
      val member = Member(email, telephone, password, gender, MemberStatus.NORMAL, birth)
      setMemberRoleSet(memberRole, member)
      return member
    }
    
    private fun setMemberRoleSet(memberRole: MutableSet<MemberRole>, member: Member) {
      if(member.memberRoleSet.isNotEmpty()) member.memberRoleSet.clear()
      memberRole.forEach { mr: MemberRole ->
        member.memberRoleSet.add(mr)
        mr.member = member
      }
    }
  }
  
  fun update(
    password: String,
    gender: Gender?,
    status: MemberStatus,
    birth: LocalDate?
  ): Member {
    this.password = password
    this.gender = gender
    this.status = status
    this.birth = birth
    return this;
  }
  
  fun updateRole(
    memberRole: MutableSet<MemberRole>
  ): Member {
    setMemberRoleSet(memberRole, this)
    return this;
  }
}