package be.tr.democracy.rest.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity(prePostEnabled = true)
open class SecurityConfig(
    @Value("\${ADMIN_USERNAME}") private val adminUsername: String,
    @Value("\${ADMIN_PASSWORD}") private val adminPassword: String,
) {

    @Bean
    open fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http
            .authorizeExchange {
                it.anyExchange().permitAll()
            }
            .httpBasic {}
            .formLogin { it.disable() }
            .csrf { it.disable() }

        return http.build()
    }

    @Bean
    open fun userDetailsService() = InMemoryUserDetailsManager(
        User.builder()
            .username(adminUsername)
            .password(adminPassword)
            .roles("MANAGER")
            .build()
    )
}
