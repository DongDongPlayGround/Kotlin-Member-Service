package org.study.kotlinmemberservice.product.dto

data class ProductDTO(
  val id: Long?,
  var name: String,
  var price: Int?,
  var memberId: Long?
)