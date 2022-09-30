plugins {
    id("com.somnus.common")
}

dependencies {
    api(project(":microservice-commons:microservice-commons-base"))
    optional("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.security:spring-security-oauth2-authorization-server")
}
