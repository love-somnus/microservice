package com.somnus.microservice.oauth2.service;

import com.somnus.microservice.commons.core.support.IService;
import com.somnus.microservice.oauth2.model.domain.Oauth2ClientDetails;

import java.util.Optional;

public interface Oauth2ClientDetailsService extends IService<Oauth2ClientDetails> {

    Optional<Oauth2ClientDetails> findByClientId(String clientId);
}
