plugins {
    id("com.somnus.common")
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-webflux")
    api("org.springframework.cloud:spring-cloud-starter-openfeign")
    api("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    api("org.springframework.cloud:spring-cloud-starter-bootstrap")
    api("org.springframework.boot:spring-boot-starter-integration")
    api("com.squareup.okhttp3:okhttp")
    api("org.springdoc:springdoc-openapi-webflux-ui")
    api(project(":microservice-nacos"))
    api("com.playtika.reactivefeign:feign-reactor-cloud")
    api("com.playtika.reactivefeign:feign-reactor-webclient")
    api("com.playtika.reactivefeign:feign-reactor-spring-configuration")
    api("net.logstash.logback:logstash-logback-encoder")
    api("com.github.danielwegener:logback-kafka-appender")
}
