package resmed.hi.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf().disable()
            .formLogin().disable().httpBasic().and()
            .authorizeExchange()
            .pathMatchers("/actuator/*")
            .permitAll()
            .pathMatchers("/**").authenticated();
        return http.build();
    }
}
