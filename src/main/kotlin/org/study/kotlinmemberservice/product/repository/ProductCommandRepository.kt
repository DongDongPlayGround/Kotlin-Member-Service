package org.study.kotlinmemberservice.product.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.study.kotlinmemberservice.product.Product

interface ProductCommandRepository: JpaRepository<Product, Long>