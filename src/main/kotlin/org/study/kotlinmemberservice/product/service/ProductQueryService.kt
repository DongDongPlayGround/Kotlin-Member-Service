package org.study.kotlinmemberservice.product.service

import org.springframework.stereotype.Service
import org.study.kotlinmemberservice.product.dto.ProductDTO
import org.study.kotlinmemberservice.product.mapper.ProductMapper
import org.study.kotlinmemberservice.product.repository.ProductCommandRepository
import org.study.kotlinmemberservice.product.repository.ProductQueryRepository

@Service
class ProductQueryService(
  private val productQueryRepository: ProductQueryRepository,
  private val productCommandRepository: ProductCommandRepository,
  private val productMapper: ProductMapper
) {
  
  fun getProductById(id: Long): ProductDTO? {
    return productCommandRepository.findById(id)
      .map { productMapper.toDTO(it)}
      .orElse(null)
//    return productQueryRepository.findProductById(id)
//      ?.let { productMapper.toDTO(it) }
  }
  
  fun getAllProducts(): List<ProductDTO> =
    productQueryRepository.findAllProducts().map { productMapper.toDTO(it) }
  
}