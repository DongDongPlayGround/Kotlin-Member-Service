package org.study.kotlinmemberservice.common.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
  
  @Bean
  fun openAPI(): OpenAPI {
    val jwtSchemeName = "Authorization"
    val jwtRefreshToken = "refreshtoken"
    
    val securityRequirement = SecurityRequirement()
      .addList(jwtSchemeName)
      .addList(jwtRefreshToken)
    val components =
      Components().addSecuritySchemes(
        jwtSchemeName,
        SecurityScheme()
          .name(jwtSchemeName)
          .type(SecurityScheme.Type.HTTP)
          .scheme("bearer")
          .bearerFormat("JWT")
      ).addSecuritySchemes(
        jwtRefreshToken,
        SecurityScheme()
          .name(jwtRefreshToken)
          .type(SecurityScheme.Type.APIKEY)
          .scheme("bearer")
          .bearerFormat("JWT")
          .`in`(SecurityScheme.In.HEADER)
      )
    return OpenAPI()
      .addSecurityItem(securityRequirement)
      .components(components)
  }
}