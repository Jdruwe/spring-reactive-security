package be.jeroendruwe.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;

@Configuration
public class WebConfiguration {

    public Mono<ServerResponse> message(ServerRequest request) {
        Mono<String> principalPublisher = request.principal().map(p -> "Hello, " + p.getName() + "!");
        return ServerResponse.ok().body(principalPublisher, String.class);
    }

    public Mono<ServerResponse> username(ServerRequest request) {
        Mono<UserDetails> detailsMono = request.principal().map(p -> UserDetails.class.cast(Authentication.class.cast(p).getPrincipal()));
        return ServerResponse.ok().body(detailsMono, UserDetails.class);
    }

    @Bean
    RouterFunction<?> routes() {
        return route(GET("/message"), this::message)
                .andRoute(GET("/users/{username}"), this::username);
    }

}
