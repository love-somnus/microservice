package com.somnus.microservice

import com.somnus.microservice.compile.CompileArgsPlugin
import com.somnus.microservice.dependency.ManagementPlugin
import com.somnus.microservice.tasks.DeleteExpand
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * <p>
 * ModulePlugin
 * </p>
 *
 * @author livk
 * @date 2022/8/10
 */
class ModulePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(CompileArgsPlugin::class.java)
        project.pluginManager.apply(RootProjectPlugin::class.java)
    }
}
