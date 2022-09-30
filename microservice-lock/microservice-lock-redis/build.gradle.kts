plugins {
    id("com.somnus.common")
}

dependencies {
    api(project(":microservice-lock:microservice-lock-aop"))
    api(project(":microservice-commons:microservice-commons-redis"))
}
