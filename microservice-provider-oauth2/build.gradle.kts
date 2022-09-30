plugins {
    id("com.somnus.service")
}

dependencies {
    implementation(project(":microservice-provider-api:microservice-provider-oauth2-api"))
    implementation(project(":microservice-starter"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(project(":microservice-commons:microservice-commons-security"))
    implementation(project(":microservice-commons:microservice-commons-core"))
    implementation(project(":microservice-commons:microservice-commons-redis"))
}
