plugins {
    id("java-gradle-plugin")
    kotlin("jvm") version ("1.7.20")
}

repositories {
    maven { setUrl("https://maven.aliyun.com/repository/public") }
    maven { setUrl("https://plugins.gradle.org/m2/") }
    maven { setUrl("https://repo.spring.io/release") }
}

ext {
    set("springBootVersion", "2.7.4")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:${project.ext["springBootVersion"]}")
}

gradlePlugin {
    plugins {
        create("bomPlugin") {
            id = "com.somnus.bom"
            implementationClass = "com.somnus.microservice.BomPlugin"
        }
        create("modulePlugin") {
            id = "com.somnus.module"
            implementationClass = "com.somnus.microservice.ModulePlugin"
        }
        create("commonPlugin") {
            id = "com.somnus.common"
            implementationClass = "com.somnus.microservice.CommonPlugin"
        }
        create("rootProjectPlugin") {
            id = "com.somnus.root"
            implementationClass = "com.somnus.microservice.RootProjectPlugin"
        }
        create("servicePlugin") {
            id = "com.somnus.service"
            implementationClass = "com.somnus.microservice.ServicePlugin"
        }
    }
}
