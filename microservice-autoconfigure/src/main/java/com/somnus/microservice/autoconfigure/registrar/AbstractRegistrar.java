package com.somnus.microservice.autoconfigure.registrar;

import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
/**
 * @author Kevin
 * @packageName com.somnus.microservice.autoconfigure.registrar
 * @title: AbstractRegistrar
 * @description: TODO
 * @date 2019/6/14 9:57
 */
@Slf4j
@NoArgsConstructor
public abstract class AbstractRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware {

    private ResourceLoader  resourceLoader;

    private ClassLoader     classLoader;

    private Environment     environment;

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata metadata, @NonNull BeanDefinitionRegistry registry) {
        registerAnnotations(metadata, registry);
    }

    public void registerAnnotations(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);

        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(getAnnotationClass());
        scanner.addIncludeFilter(annotationTypeFilter);
        Set<String> basePackages = getBasePackages(metadata);

        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    // verify annotated class is an interface
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();

                    Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(getAnnotationClass().getCanonicalName());
                    registerAnnotation(registry, annotationMetadata, attributes);
                }
            }
        }
    }

    private void registerAnnotation(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
        String className = annotationMetadata.getClassName();

        log.info("Found annotation [{}] in {} ", getAnnotationClass().getSimpleName(), className);

        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(getBeanClass());
        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();

        customize(registry, annotationMetadata, attributes, definition);

        try {
            definition.addPropertyValue("interfaze", Class.forName(className));
        } catch (ClassNotFoundException e) {
            log.error("Get interface for name error", e);
        }
        definition.addPropertyValue("interceptor", getInterceptor(beanDefinition.getPropertyValues()));

        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

        String alias = className;

        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className, new String[] { alias });
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(@NonNull AnnotatedBeanDefinition beanDefinition) {
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().getInterfaceNames().length == 1 && Annotation.class.getName().equals(beanDefinition.getMetadata().getInterfaceNames()[0])) {
                        try {
                            Class<?> target = ClassUtils.forName(beanDefinition.getMetadata().getClassName(), AbstractRegistrar.this.classLoader);

                            return !target.isAnnotation();
                        } catch (Exception ex) {
                            log.error("Could not load target class: {}" + beanDefinition.getMetadata().getClassName(), ex);
                        }
                    }

                    return true;
                }

                return false;
            }
        };
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(getEnableAnnotationClass().getCanonicalName());

        Set<String> basePackages = new HashSet<>();

        if(Objects.nonNull(attributes) && attributes.containsKey("value")){
            for (String pkg : (String[]) attributes.get("value")) {
                if (StringUtils.hasText(pkg)) {
                    basePackages.add(pkg);
                }
            }
        }
        if(Objects.nonNull(attributes) && attributes.containsKey("basePackages")){
            for (String pkg : (String[]) attributes.get("basePackages")) {
                if (StringUtils.hasText(pkg)) {
                    basePackages.add(pkg);
                }
            }
        }
        if(Objects.nonNull(attributes) && attributes.containsKey("basePackageClasses")){
            for (Class<?> clazz : (Class<?>[]) attributes.get("basePackageClasses")) {
                basePackages.add(ClassUtils.getPackageName(clazz));
            }
        }

        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }

        return basePackages;
    }

    protected void customize(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata, Map<String, Object> attributes, BeanDefinitionBuilder definition) {
        for (Map.Entry<String, Object> attribute : attributes.entrySet()) {
            definition.addPropertyValue(attribute.getKey(), attribute.getValue());
        }
    }

    protected abstract Class<? extends Annotation> getEnableAnnotationClass();

    protected abstract Class<? extends Annotation> getAnnotationClass();

    protected abstract Class<?> getBeanClass();

    protected abstract MethodInterceptor getInterceptor(MutablePropertyValues annotationValues);
}