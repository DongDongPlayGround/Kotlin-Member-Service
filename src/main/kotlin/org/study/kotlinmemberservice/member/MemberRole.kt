package org.study.kotlinmemberservice.member

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(value = [AuditingEntityListener::class])
class MemberRole(
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  var member: Member? = null,
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role_id")
  var role: Role,
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null
) {
  
  companion object {
    fun create(role: Role): MemberRole {
      return MemberRole(role = role)
    }
  }

//  fun setMember(member: Member){
//    this.member = member
//    member.memberRoleList.add(this)
//  }
}