plugins {
    id("com.somnus.service")
}

dependencies {
    implementation(project(":microservice-provider-api:microservice-provider-cpc-api"))
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation(project(":microservice-starter"))
    implementation(project(":microservice-commons:microservice-commons-core"))
}
