plugins {
    id("com.somnus.common")
}

dependencies {
    api(project(":microservice-limit:microservice-limit-local"))
    api(project(":microservice-limit:microservice-limit-redis"))
    api(project(":microservice-limit:microservice-limit-redisson"))
}
