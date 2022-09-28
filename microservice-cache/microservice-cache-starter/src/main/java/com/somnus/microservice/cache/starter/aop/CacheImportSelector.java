package com.somnus.microservice.cache.starter.aop;

import com.somnus.microservice.autoconfigure.selector.AbstractImportSelector;
import com.somnus.microservice.autoconfigure.selector.RelaxedPropertyResolver;
import com.somnus.microservice.cache.starter.annotation.EnableCache;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
/**
 * @author Kevin
 * @date 2019/7/5 16:58
 */
@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class CacheImportSelector extends AbstractImportSelector<EnableCache> {

    @Override
    protected boolean isEnabled() {
        return new RelaxedPropertyResolver(getEnvironment()).getProperty("cache.enabled", Boolean.class, Boolean.TRUE);
    }
}
