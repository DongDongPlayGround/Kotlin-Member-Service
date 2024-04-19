package org.study.kotlinmemberservice.product

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.study.kotlinmemberservice.common.TenantEntity

@Entity
class Product(
  var name: String,
  var price: Int? = 0,
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,
) : TenantEntity() {
  companion object {
    fun create(name: String, price: Int?): Product {
      return Product(name, price)
    }
  }
  
  fun update(name: String, price: Int?): Product {
    this.name = name
    this.price = price
    return this;
  }
  
  constructor():this("")
}