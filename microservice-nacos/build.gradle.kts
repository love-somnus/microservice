plugins {
    id("com.somnus.common")
}

dependencies {
    api("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    api("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")
    api("com.alibaba.cloud:spring-cloud-starter-alibaba-sentinel")
}
