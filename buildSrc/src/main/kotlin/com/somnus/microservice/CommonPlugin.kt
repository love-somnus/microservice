package com.somnus.microservice

import com.somnus.microservice.ModulePlugin
import com.somnus.microservice.compile.ResourcesPlugin
import com.somnus.microservice.info.ManifestPlugin
import com.somnus.microservice.maven.DeployedPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin

/**
 * <p>
 * CommonPlugin
 * </p>
 *
 * @author livk
 * @date 2022/8/10
 */
class CommonPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(JavaLibraryPlugin::class.java)
        project.pluginManager.apply(ModulePlugin::class.java)
        project.pluginManager.apply(ResourcesPlugin::class.java)
        project.pluginManager.apply(ManifestPlugin::class.java)
        project.pluginManager.apply(DeployedPlugin::class.java)
    }
}
