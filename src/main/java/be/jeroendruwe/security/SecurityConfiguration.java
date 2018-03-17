package be.jeroendruwe.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange()
                .pathMatchers("/users/{username}")
                .access((mono, context) -> mono
                        .map(auth -> auth.getName().equals(context.getVariables().get("username")))
                        .map(AuthorizationDecision::new))
                .anyExchange().authenticated()
                .and().httpBasic()
                .and().formLogin()
                .and().build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        UserDetails user2 = User.withDefaultPasswordEncoder()
                .username("user2")
                .password("password2")
                .roles("USER")
                .build();

        return new MapReactiveUserDetailsService(user, user2);
    }

}
