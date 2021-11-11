package org.apache.dubbo.config.spring.beans.factory.annotation;

import com.alibaba.spring.util.AnnotationUtils;
import feign.Target;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ReflectionUtils;

import java.util.Map;

/**
 * @author Kevin
 * @packageName org.apache.dubbo.config.spring.beans.factory.annotation
 * @title: DubboFeignBuilder
 * @description: TODO
 * @date 2021/11/03 11:14
 */
public class DubboFeignBuilder extends feign.Feign.Builder {

    @Autowired
    private ApplicationContext applicationContext;

    public DubboReference dubboReference;

    final static class DefaultReferenceClass{
        @DubboReference(check = false, protocol = "nacos") String field;
    }

    public DubboFeignBuilder() {
        this.dubboReference = ReflectionUtils.findField(DubboFeignBuilder.DefaultReferenceClass.class,"field").getAnnotation(DubboReference.class);
    }

    @Override
    public <T> T target(Target<T> target) {
        Map<String, Object> map = AnnotationUtils.getAttributes(dubboReference, applicationContext.getEnvironment(), true);
        ReferenceBeanBuilder beanBuilder = ReferenceBeanBuilder
                .create(AnnotationAttributes.fromMap(map), applicationContext)
                .interfaceClass(target.type());
        try {
            T object = (T) beanBuilder.build().getObject();
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}