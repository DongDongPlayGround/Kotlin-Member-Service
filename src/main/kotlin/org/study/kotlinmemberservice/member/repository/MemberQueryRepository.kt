package org.study.kotlinmemberservice.member.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.study.kotlinmemberservice.member.*

@Repository
class MemberQueryRepository(
  private val jpaQueryFactory: JPAQueryFactory
) {
  
  val member: QMember = QMember.member
  val memberRole: QMemberRole = QMemberRole.memberRole
  val role: QRole = QRole.role
  
  fun findMemberById(id: Long): Member? {
    return jpaQueryFactory.selectFrom(member)
      .where(member.id.eq(id))
      .fetchFirst()
  }
  
  // TODO 이거 테스트 필요 : 권한 잘 나오는지
  fun findMemberByIdWithRole(id: Long): Member? {
    return jpaQueryFactory.selectFrom(member)
      .leftJoin(member.memberRoleSet, memberRole)
      .leftJoin(memberRole.role, role)
      .where(member.id.eq(id))
      .fetchFirst()
  }
  
  fun findAllMembers(): List<Member> {
    return jpaQueryFactory.selectFrom(member).fetch()
  }
  
  
  fun findRoleById(id: Long): Role? {
    return jpaQueryFactory.selectFrom(role)
      .where(role.id.eq(id))
      .fetchFirst()
  }
  
  fun findAllRoleByIdList(idList: List<Long?>): List<Role> {
    return jpaQueryFactory.selectFrom(role)
      .where(role.id.`in`(idList))
      .fetch()
  }
  
  fun findAllRoles(): List<Role> {
    return jpaQueryFactory.selectFrom(role).fetch()
  }
}