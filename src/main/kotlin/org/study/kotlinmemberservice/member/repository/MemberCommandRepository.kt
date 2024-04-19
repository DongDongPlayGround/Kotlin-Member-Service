package org.study.kotlinmemberservice.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.study.kotlinmemberservice.member.Member

interface MemberCommandRepository: JpaRepository<Member, Long> {
  
  fun findByEmail(email: String): Member?
  
  fun existsByEmail(email: String): Boolean
}