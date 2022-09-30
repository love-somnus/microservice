plugins {
    id("com.somnus.common")
}

dependencies {
    api(project(":microservice-starter"))
    api(project(":microservice-commons:microservice-commons-base"))
}
