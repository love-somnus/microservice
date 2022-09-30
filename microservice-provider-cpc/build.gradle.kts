plugins {
    id("com.somnus.service")
}

dependencies {
    implementation(project(":microservice-provider-api:microservice-provider-cpc-api"))
    implementation(project(":microservice-starter"))
    implementation(project(":microservice-commons:microservice-commons-core"))
    implementation(project(":microservice-lock:microservice-lock-starter"))
    implementation(project(":microservice-limit:microservice-limit-starter"))
    implementation(project(":microservice-cache:microservice-cache-starter"))
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-reactor-netty")
}
