pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { setUrl("https://plugins.gradle.org/m2/") }
        maven { setUrl("https://repo.spring.io/release") }
        maven { setUrl("https://maven.aliyun.com/repository/public") }
    }
}

rootProject.name = "microservice"

fileTree(rootDir) {
    val excludes = gradle.startParameter.projectProperties["excludeProjects"]?.split(",")
    include("**/*.gradle.kts")
    exclude("build", "**/gradle", "settings.gradle.kts", "buildSrc", "/build.gradle.kts", ".", "out")
    if (!excludes.isNullOrEmpty()) {
        exclude(excludes)
    }
}.forEach {
    val buildFilePath = it.parentFile.absolutePath
    val projectPath = buildFilePath.replace(rootDir.absolutePath, "").replace(File.separator, ":")
    include(projectPath)

    val project = findProject(projectPath)
    if (project != null) {
        project.projectDir = it.parentFile
        project.buildFileName = it.name
    }
}
