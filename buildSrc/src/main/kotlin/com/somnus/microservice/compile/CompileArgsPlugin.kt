package com.somnus.microservice.compile

import com.somnus.microservice.info.ManifestPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test

/**
 * <p>
 * CompileArgsPlugin
 * </p>
 *
 * @author livk
 * @date 2022/7/7
 */
abstract class CompileArgsPlugin : Plugin<Project> {
    companion object {
        val COMPILER_ARGS = ArrayList<String>()
        val UTF_8 = "UTF-8"

        init {
//            COMPILER_ARGS.addAll(
//                listOf(
//                    "-Xlint:-options",
//                    "-Xlint:rawtypes",
//                    "-Xlint:deprecation",
//                    "-Xlint:unchecked"
//                )
//            )
        }
    }

    override fun apply(project: Project) {
        project.pluginManager.apply(JavaPlugin::class.java)
        project.pluginManager.apply(ManifestPlugin::class.java)
        val javaCompile = project.tasks
            .getByName(JavaPlugin.COMPILE_JAVA_TASK_NAME) as JavaCompile
        javaCompile.options.compilerArgs.addAll(COMPILER_ARGS)
        javaCompile.options.encoding = UTF_8

        project.tasks.withType(Test::class.java) {
            it.useJUnitPlatform()
        }
    }
}
