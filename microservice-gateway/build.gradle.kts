plugins {
    id("com.somnus.service")
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation(project(":microservice-commons:microservice-commons-security"))
    implementation(project(":microservice-starter"))
    implementation(project(":microservice-commons:microservice-commons-redis"))
    implementation("org.springdoc:springdoc-openapi-webflux-ui")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
}
