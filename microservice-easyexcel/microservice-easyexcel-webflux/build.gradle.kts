plugins {
    id("com.somnus.common")
}

dependencies {
    api(project(":microservice-easyexcel:microservice-easyexcel-aop"))
    optional("org.springframework.boot:spring-boot-starter-webflux")
}
