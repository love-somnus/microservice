plugins {
    id("com.somnus.common")
}

dependencies {
    api("com.baomidou:mybatis-plus-boot-starter")
    api("com.github.pagehelper:pagehelper-spring-boot-starter")
    runtimeOnly("mysql:mysql-connector-java")
    optional("javax.persistence:javax.persistence-api")
    api("org.springframework:spring-context-support")
    api(project(":microservice-commons:microservice-commons-base"))
}
