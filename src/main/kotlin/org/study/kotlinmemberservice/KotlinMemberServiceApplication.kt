package org.study.kotlinmemberservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing


@EnableJpaAuditing
@SpringBootApplication
class KotlinMemberServiceApplication

fun main(args: Array<String>) {
  runApplication<KotlinMemberServiceApplication>(*args)
}
