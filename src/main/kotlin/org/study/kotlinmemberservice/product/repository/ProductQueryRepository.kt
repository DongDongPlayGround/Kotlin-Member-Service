package org.study.kotlinmemberservice.product.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.study.kotlinmemberservice.product.Product
import org.study.kotlinmemberservice.product.QProduct

@Repository
class ProductQueryRepository(
  private val jpaQueryFactory: JPAQueryFactory,
) {
  val product: QProduct = QProduct.product
  
  fun findProductById(id: Long): Product? {
    return jpaQueryFactory.selectFrom(product)
      .where(product.id.eq(id))
      .fetchFirst()
  }
  
  fun findAllProducts(): List<Product> {
    return jpaQueryFactory.selectFrom(product)
      .fetch()
  }
}