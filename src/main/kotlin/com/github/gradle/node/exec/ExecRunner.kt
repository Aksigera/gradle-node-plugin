package com.github.gradle.node.exec

import com.github.gradle.node.NodeExtension
import com.github.gradle.node.variant.Variant
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import java.io.File

internal class ExecRunner {
    fun execute(project: Project, execConfiguration: ExecConfiguration) {
        project.exec {
            executable = execConfiguration.executable
            args = execConfiguration.args
            environment = computeEnvironment(execConfiguration)
            isIgnoreExitValue = execConfiguration.ignoreExitValue
            workingDir = computeWorkingDir(project, execConfiguration)
            execConfiguration.execOverrides?.invoke(this)
        }
    }

    private fun computeEnvironment(execConfiguration: ExecConfiguration): Map<String, String> {
        val execEnvironment = mutableMapOf<String, String>()
        execEnvironment += System.getenv()
        execEnvironment += execConfiguration.environment
        if (execConfiguration.additionalBinPath != null) {
            // Take care of Windows environments that may contain "Path" OR "PATH" - both existing
            // possibly (but not in parallel as of now)
            val pathEnvironmentVariableName = if (execEnvironment["Path"] != null) "Path" else "PATH"
            val actualPath = execEnvironment[pathEnvironmentVariableName]
            execEnvironment[pathEnvironmentVariableName] =
                    "${execConfiguration.additionalBinPath}${File.pathSeparator}${actualPath}"
        }
        return execEnvironment
    }

    private fun computeWorkingDir(project: Project, execConfiguration: ExecConfiguration): File? {
        val workingDir = execConfiguration.workingDir ?: NodeExtension[project].nodeModulesDir
        workingDir.mkdirs()
        return workingDir
    }
}
