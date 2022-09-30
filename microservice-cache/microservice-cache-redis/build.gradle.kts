plugins {
    id("com.somnus.common")
}

dependencies {
    api(project(":microservice-cache:microservice-cache-aop"))
    api(project(":microservice-commons:microservice-commons-redis"))
}
