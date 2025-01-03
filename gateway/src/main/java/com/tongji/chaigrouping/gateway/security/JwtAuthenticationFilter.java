package com.tongji.chaigrouping.gateway.security;

import com.tongji.chaigrouping.gateway.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.logging.Logger;

@Component
public class JwtAuthenticationFilter implements WebFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("allowedRoutes")
    private List<String> allowedRoutes;

    private final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String requestURI = exchange.getRequest().getPath().value();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        if (allowedRoutes.stream().anyMatch(route -> pathMatcher.match(route, requestURI))) {
            return chain.filter(exchange);
        }

        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                int userId = jwtTokenUtil.tryParseToken(token);
                exchange.getRequest().mutate().header("X-User-Id", String.valueOf(userId)).build();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, null);
                SecurityContext context = new SecurityContextImpl(authentication);
                return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
            } catch (Exception e) {
                logger.warning("Invalid token: " + e.getMessage());
            }
        }
        exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(401));
        return exchange.getResponse().setComplete();
    }
}
