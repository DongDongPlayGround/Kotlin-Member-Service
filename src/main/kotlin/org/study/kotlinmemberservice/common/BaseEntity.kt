package org.study.kotlinmemberservice.common

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(value = [AuditingEntityListener::class])
abstract class BaseEntity(
  @CreatedBy
  var createdBy: String = "",
  @CreatedDate
  var createdDate: LocalDateTime = LocalDateTime.now(),
  @LastModifiedBy
  var modifiedBy: String = "",
  @LastModifiedDate
  var modifiedDate: LocalDateTime = LocalDateTime.now()
)