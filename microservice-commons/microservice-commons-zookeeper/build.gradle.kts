plugins {
    id("com.somnus.common")
}

dependencies {
    api(project(":microservice-commons:microservice-commons-base"))
    api("org.apache.curator:curator-recipes")
}
