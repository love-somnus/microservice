plugins {
    id("com.somnus.common")
}

dependencies {
    api(project(":microservice-autoconfigure"))
    api("com.xuxueli:xxl-job-core")
    api(project(":microservice-commons:microservice-commons-base"))
}
