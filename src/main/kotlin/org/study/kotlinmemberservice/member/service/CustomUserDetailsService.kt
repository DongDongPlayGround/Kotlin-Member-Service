package org.study.kotlinmemberservice.member.service

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.study.kotlinmemberservice.member.Member
import org.study.kotlinmemberservice.common.authority.CustomUser
import org.study.kotlinmemberservice.member.repository.MemberCommandRepository

@Service
class CustomUserDetailsService(
  private val memberRepository: MemberCommandRepository
) : UserDetailsService {
  
  override fun loadUserByUsername(username: String): UserDetails {
    // 로그인 id 로 db 에서 정보 조회해오고
    return memberRepository.findByEmail(username)
      // 그 유저가 있으면 UserDetails , 아니면 에러
      ?.let { createUserDetails(it) } ?: throw UsernameNotFoundException("")
  }
  
  /* authenticate 객체인 CustomUser 를 만드는 과정 */
  private fun createUserDetails(member: Member): UserDetails =
    CustomUser(
      member.id!!,
      member.email,
      member.password,
      member.memberRoleSet.map { SimpleGrantedAuthority(it.role.roleType.name) }
    )
}