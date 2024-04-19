package org.study.kotlinmemberservice.common

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreRemove
import jakarta.persistence.PreUpdate
import org.hibernate.annotations.TenantId
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.security.core.context.SecurityContextHolder
import org.study.kotlinmemberservice.common.authority.CustomUser
import java.io.Serializable

@MappedSuperclass
//@EntityListeners(value = [AuditingEntityListener::class])
abstract class TenantEntity(
  @TenantId
  var memberId: Long? = null
) : Serializable {
  
  /**
  @PrePersist
  @PreUpdate
  fun prePersistOrUpdate() {
    this.memberId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).memberId
  }
  */
  
  // 멤버 a 가 생성한 것을 다른 사람이 지우려하면 에러 내는 방향으로 사용 가능할 듯
  // 그러나 지금은 tenant 실험을 위한 것이니 주석
  /**
  @PreRemove
  fun preRemove() {
    val securityHolderMemberId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).memberId
    val isRemovable = this.memberId?.equals(securityHolderMemberId) ?: throw RuntimeException("memberId 가 없어서 에러")
    if (!isRemovable) throw RuntimeException()
  }
   */
}