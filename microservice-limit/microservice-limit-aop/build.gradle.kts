plugins {
    id("com.somnus.common")
}

dependencies {
    api(project(":microservice-autoconfigure"))
    api(project(":microservice-commons:microservice-commons-base"))
}
