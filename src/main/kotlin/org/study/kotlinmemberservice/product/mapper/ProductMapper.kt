package org.study.kotlinmemberservice.product.mapper

import org.springframework.stereotype.Component
import org.study.kotlinmemberservice.product.Product
import org.study.kotlinmemberservice.product.dto.ProductDTO

@Component
class ProductMapper {
  
  fun toDTO(product: Product): ProductDTO {
    return ProductDTO(product.id, product.name, product.price, product.memberId)
  }
}