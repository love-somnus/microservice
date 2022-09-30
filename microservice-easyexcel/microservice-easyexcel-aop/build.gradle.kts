plugins {
    id("com.somnus.common")
}

dependencies {
    api(project(":microservice-autoconfigure"))
    api("org.springframework.boot:spring-boot-starter-validation")
    api("com.alibaba:easyexcel")
}
