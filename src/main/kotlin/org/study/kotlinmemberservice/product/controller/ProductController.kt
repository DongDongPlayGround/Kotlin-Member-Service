package org.study.kotlinmemberservice.product.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.study.kotlinmemberservice.product.dto.ProductDTO
import org.study.kotlinmemberservice.product.service.ProductCommandService
import org.study.kotlinmemberservice.product.service.ProductQueryService

@RestController
@RequestMapping("/product")
class ProductController(
  private val productCommandService: ProductCommandService,
  private val productQueryService: ProductQueryService
) {
  
  @PostMapping()
  fun createProduct(
    @RequestBody productDTO: ProductDTO
  ): ProductDTO = productCommandService.createProduct(productDTO)
  
  @PutMapping()
  fun updateProduct(
    @RequestParam(name = "id") id: Long,
    @RequestBody productDTO: ProductDTO
  ): ProductDTO = productCommandService.updateProduct(id, productDTO)
  
  @GetMapping()
  fun getProductById(
    @RequestParam(name = "id") id: Long
  ): ProductDTO? = productQueryService.getProductById(id)
  
  @GetMapping("/all")
  fun getAllProducts(): List<ProductDTO> = productQueryService.getAllProducts();
}