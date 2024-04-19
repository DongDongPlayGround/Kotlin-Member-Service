package org.study.kotlinmemberservice.common.config

import org.hibernate.cfg.AvailableSettings
import org.hibernate.context.spi.CurrentTenantIdentifierResolver
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.ObjectUtils
import org.study.kotlinmemberservice.common.authority.CustomUser

@Component
internal class TenantIdentifierConfig : CurrentTenantIdentifierResolver<Any?>, HibernatePropertiesCustomizer {

  override fun resolveCurrentTenantIdentifier(): Long? {
    val authentication = SecurityContextHolder.getContext().authentication ?: return -1
    if(authentication.principal is CustomUser) {
      val tenantId: Long = (authentication.principal as CustomUser).memberId
      return if (!ObjectUtils.isEmpty(tenantId)) {
        tenantId
      } else {
        // 그냥 에러를 터뜨리고 싶지만 일단..
        -1
      }
    }
    return -1
  }

  override fun validateExistingCurrentSessions(): Boolean {
    return true
  }

  override fun customize(hibernateProperties: MutableMap<String, Any>) {
    hibernateProperties[AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER] = this
  }
}