package com.somnus.microservice.gateway.config.manager;

import com.somnus.microservice.commons.base.utils.JwtTokenUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.Collection;
/**
 * @author kevin.liu
 * @title: TokenAuthenticationManager
 * @projectName gateway
 * @description: token 认证处理
 * @date 2021/11/15 15:26
 */
@Primary
@Component
public class TokenAuthenticationManager implements ReactiveAuthenticationManager {

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .map(auth -> JwtTokenUtil.parseJwtRsa256(auth.getPrincipal().toString()))
                .map(claims -> {
                    Collection<? extends GrantedAuthority> roles = (Collection<? extends GrantedAuthority>) claims.get("roles");
                    return new UsernamePasswordAuthenticationToken(
                            claims.get("username"),
                            claims.get("realname"),
                            roles
                    );
                });
    }
}
