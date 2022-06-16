package com.somnus.microservice.commons.security.core.principal;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kevin.liu
 * @title: Oauth2User
 * @projectName microservice
 * @description: TODO
 * @date 2022/6/14 15:15
 */
public class Oauth2User extends User implements OAuth2AuthenticatedPrincipal {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    /**
     * 用户ID
     */
    @Getter
    private final Long id;

    /**
     * 手机号
     */
    @Getter
    private final String mobile;

    public Oauth2User(Long id, String username, String password, String mobile, boolean enabled,
                   boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                   Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.mobile = mobile;
    }

    /**
     * Get the OAuth 2.0 token attributes
     * @return the OAuth 2.0 token attributes
     */
    @Override
    public Map<String, Object> getAttributes() {
        return new HashMap<>();
    }

    @Override
    public String getName() {
        return this.getUsername();
    }

}