plugins {
    id("com.somnus.common")
}

dependencies {
    api(project(":microservice-limit:microservice-limit-aop"))
    api("com.google.guava:guava")
}
