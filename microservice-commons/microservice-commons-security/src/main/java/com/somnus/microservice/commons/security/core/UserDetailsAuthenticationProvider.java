package com.somnus.microservice.commons.security.core;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.somnus.microservice.commons.base.utils.WebUtils;
import com.somnus.microservice.commons.security.core.constant.SecurityConstants;
import com.somnus.microservice.commons.security.core.exception.BadCaptchaException;
import com.somnus.microservice.commons.security.service.Oauth2UserDetailsService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author kevin.liu
 * @title: DaoAuthenticationProvider
 * @projectName microservice
 * @description: TODO
 * @date 2022/6/14 15:32
 */
public class UserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    /**
     * The plaintext password used to perform PasswordEncoder#matches(CharSequence,
     * String)} on when the user is not found to avoid SEC-2056.
     */
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    private final static BasicAuthenticationConverter basicConvert = new BasicAuthenticationConverter();

    @Getter
    private PasswordEncoder passwordEncoder;

    /**
     * The password used to perform {@link PasswordEncoder#matches(CharSequence, String)}
     * on when the user is not found to avoid SEC-2056. This is necessary, because some
     * {@link PasswordEncoder} implementations will short circuit if the password is not
     * in a valid format.
     */
    private volatile String userNotFoundEncodedPassword;

    @Getter
    @Setter
    private UserDetailsService userDetailsService;

    @Setter
    private UserDetailsPasswordService userDetailsPasswordService;

    public UserDetailsAuthenticationProvider() {
        setMessageSource(SpringUtil.getBean("securityMessageSource"));
        setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        String grantType = WebUtils.getRequest().get().getParameter(OAuth2ParameterNames.GRANT_TYPE);

        if (authentication.getCredentials() == null) {
            this.logger.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(
                    this.messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.badCredentials",
                            "Bad credentials"
                    )
            );
        }

        if (StrUtil.equals(SecurityConstants.SMS, grantType)) {
            String code = authentication.getCredentials().toString();
            if(!StrUtil.equals(code, "123456")){
                this.logger.debug("Failed to authenticate since code does not match stored value");
                throw new BadCaptchaException(
                        this.messages.getMessage(
                                "AbstractUserDetailsAuthenticationProvider.badCaptcha",
                                "Bad captcha"
                        )
                );
            }
        }

        if (StrUtil.equals(AuthorizationGrantType.PASSWORD.getValue(), grantType)) {
            String presentedPassword = authentication.getCredentials().toString();
            String encodedPassword = extractEncodedPassword(userDetails.getPassword());
            if (!this.passwordEncoder.matches(presentedPassword, encodedPassword)) {
                this.logger.debug("Failed to authenticate since password does not match stored value");
                throw new BadCredentialsException(
                        this.messages.getMessage(
                                "AbstractUserDetailsAuthenticationProvider.badCredentials",
                                "Bad credentials"
                        )
                );
            }
        }

    }

    @SneakyThrows
    @Override
    protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
        prepareTimingAttackProtection();
        HttpServletRequest request = WebUtils.getRequest().orElseThrow(
                (Supplier<Throwable>) () -> new InternalAuthenticationServiceException("web request is empty"));

        Map<String, String> paramMap = ServletUtil.getParamMap(request);
        String grantType = paramMap.get(OAuth2ParameterNames.GRANT_TYPE);
        String clientId = paramMap.get(OAuth2ParameterNames.CLIENT_ID);

        if (StrUtil.isBlank(clientId)) {
            clientId = basicConvert.convert(request).getName();
        }

        Map<String, Oauth2UserDetailsService> userDetailsServiceMap = SpringUtil.getBeansOfType(Oauth2UserDetailsService.class);

        String finalClientId = clientId;
        Optional<Oauth2UserDetailsService> optional = userDetailsServiceMap.values().stream()
                .filter(service -> service.support(finalClientId, grantType))
                .max(Comparator.comparingInt(Ordered::getOrder));

        optional.orElseThrow((() -> new InternalAuthenticationServiceException("UserDetailsService error , not register")));

        try {
            UserDetails loadedUser = optional.get().loadUserByUsername(username);
            Optional.ofNullable(loadedUser).orElseThrow(() ->
                    new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation"));
            return loadedUser;
        }
        catch (UsernameNotFoundException ex) {
            mitigateAgainstTimingAttack(authentication);
            throw ex;
        }
        catch (InternalAuthenticationServiceException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        boolean upgradeEncoding = this.userDetailsPasswordService != null
                && this.passwordEncoder.upgradeEncoding(user.getPassword());
        if (upgradeEncoding) {
            String presentedPassword = authentication.getCredentials().toString();
            String newPassword = this.passwordEncoder.encode(presentedPassword);
            user = this.userDetailsPasswordService.updatePassword(user, newPassword);
        }
        return super.createSuccessAuthentication(principal, authentication, user);
    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
        }
    }

    private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }
    }

    /**
     * Sets the PasswordEncoder instance to be used to encode and validate passwords. If
     * not set, the password will be compared using
     * {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}
     * @param passwordEncoder must be an instance of one of the {@code PasswordEncoder}
     * types.
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
        this.userNotFoundEncodedPassword = null;
    }

    private String extractEncodedPassword(String prefixEncodedPassword) {
        int start = prefixEncodedPassword.indexOf(SecurityConstants.DEFAULT_ID_SUFFIX);
        return prefixEncodedPassword.substring(start + SecurityConstants.DEFAULT_ID_SUFFIX.length());
    }
}