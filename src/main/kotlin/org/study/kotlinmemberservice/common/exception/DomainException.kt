package org.study.kotlinmemberservice.common.exception

abstract class DomainException(
  val code: String,
  override val message: String
):RuntimeException()