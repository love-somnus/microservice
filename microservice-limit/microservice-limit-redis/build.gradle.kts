plugins {
    id("com.somnus.common")
}

dependencies {
    api(project(":microservice-limit:microservice-limit-aop"))
    api(project(":microservice-commons:microservice-commons-redis"))
}
