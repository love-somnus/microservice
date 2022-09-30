plugins {
    id("com.somnus.common")
}

dependencies {
    api("org.redisson:redisson")
    api(project(":microservice-commons:microservice-commons-base"))
}
