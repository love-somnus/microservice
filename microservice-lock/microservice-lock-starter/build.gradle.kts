plugins {
    id("com.somnus.common")
}

dependencies {
    api(project(":microservice-lock:microservice-lock-local"))
    api(project(":microservice-lock:microservice-lock-redis"))
    api(project(":microservice-lock:microservice-lock-zookeeper"))
}
