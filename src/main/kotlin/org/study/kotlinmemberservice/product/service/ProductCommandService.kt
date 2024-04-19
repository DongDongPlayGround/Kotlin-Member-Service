package org.study.kotlinmemberservice.product.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.study.kotlinmemberservice.product.Product
import org.study.kotlinmemberservice.product.dto.ProductDTO
import org.study.kotlinmemberservice.product.mapper.ProductMapper
import org.study.kotlinmemberservice.product.repository.ProductCommandRepository

@Service
class ProductCommandService(
  private val productCommandRepository: ProductCommandRepository,
  private val productMapper: ProductMapper
) {
  
  fun createProduct(productDTO: ProductDTO): ProductDTO {
    return Product.create(productDTO.name, productDTO.price)
      .let { productCommandRepository.save(it) }
      .let { productMapper.toDTO(it) }
  }
  
  @Transactional
  fun updateProduct(id: Long, productDTO: ProductDTO): ProductDTO {
    return productCommandRepository.findById(id)
      .map { it.update(productDTO.name, productDTO.price) }
      .map { productMapper.toDTO(it) }
      .orElse(null)
  }
}