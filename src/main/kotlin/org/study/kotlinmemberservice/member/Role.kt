package org.study.kotlinmemberservice.member

import jakarta.persistence.*
import org.study.kotlinmemberservice.common.enums.RoleType

@Entity
class  Role(
  @Column(columnDefinition = "varchar(255)")
  @Enumerated(EnumType.STRING)
  var roleType: RoleType,
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null
)