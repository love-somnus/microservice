package com.somnus.microservice.xxljob.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.somnus.microservice.xxljob.selector.XxlJobConfigurationSelector;
import org.springframework.context.annotation.Import;
/**
 * @packageName com.netease.microservice.elasticjob.annotation
 * @title: EnableXxlJob
 * @description: XxlJob 开启注解 <p>在Spring Boot 启动类上加此注解开启自动配置<p> 这个和
 * org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
 *   com.netease.microservice.xxljob.autoconfigure.XxlJobAutoConfiguration
 * 的作用是一样的，都是为了把XxlJobAutoConfiguration引入
 * @date 2021/10/25 17:56
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({XxlJobConfigurationSelector.class})
public @interface EnableXxlJob {

}