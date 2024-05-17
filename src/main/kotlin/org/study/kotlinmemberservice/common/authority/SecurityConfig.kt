package org.study.kotlinmemberservice.common.authority

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpMethod
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
  private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
  private val jwtTokenProvider: JwtTokenProvider,
  private val redisTemplate: RedisTemplate<String, String>
) {
  
  
  @Bean
  fun filterChain(http: HttpSecurity): SecurityFilterChain {
    http.httpBasic { it.disable() } // rest api 만 고려, 기본 설정 해제
      .csrf { it.disable() }
      .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
      .authorizeHttpRequests {
        it.requestMatchers(HttpMethod.POST, "/member").permitAll() // 회원 가입
          .requestMatchers("/auth/login").permitAll() // 로그인
          .anyRequest().authenticated()
      }
      .addFilterBefore(
        JwtAuthenticationFilter(jwtTokenProvider, redisTemplate), // 이 앞부분이 통과하면 뒤의 User~Filter 은 진행하지 않는다
        UsernamePasswordAuthenticationFilter::class.java
      )
      .exceptionHandling { it.authenticationEntryPoint(jwtAuthenticationEntryPoint) }
      .cors { }
    
    return http.build()
  }
  
  @Bean
  fun webSecurityCustomizer(): WebSecurityCustomizer {
    return WebSecurityCustomizer { web: WebSecurity ->
      web.ignoring().requestMatchers(
        "/h2-console/**",
        "/favicon.ico",
        // -- Static resources
        "/css/**", "/images/**", "/js/**",
        // -- Swagger UI v2
        "/v2/api-docs", "/swagger-resources/**",
        "/swagger-ui/index.html", "/webjars/**", "/swagger/**","/swagger-ui/**",
        // -- Swagger UI v3 (Open API)
        "/v3/api-docs/**"
      )
    }
  }
  
  @Bean
  fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
  
  // hasPermission 이 항상 denyAllPermissionEvaluator 로 인식하는 것에 대한 조치
  @Bean
  fun createExpressionHandler(): MethodSecurityExpressionHandler {
    val expressionHandler = DefaultMethodSecurityExpressionHandler()
    expressionHandler.setPermissionEvaluator(CustomPermissionEvaluator())
    return expressionHandler
  }
}