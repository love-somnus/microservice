package com.somnus.microservice.xxljob.selector;

import com.somnus.microservice.autoconfigure.selector.AbstractImportSelector;
import com.somnus.microservice.autoconfigure.selector.RelaxedPropertyResolver;
import com.somnus.microservice.xxljob.annotation.EnableXxlJob;

/**
 * @author kevin.liu
 * @title: XxlJobConfigurationSelector
 * @projectName neshpub
 * @description: TODO
 * @date 2022/8/29 10:45
 */
public class XxlJobConfigurationSelector extends AbstractImportSelector<EnableXxlJob> {

    @Override
    protected boolean isEnabled() {
        return new RelaxedPropertyResolver(getEnvironment()).getProperty("xxl.job.enabled", Boolean.class, Boolean.TRUE);
    }
}
