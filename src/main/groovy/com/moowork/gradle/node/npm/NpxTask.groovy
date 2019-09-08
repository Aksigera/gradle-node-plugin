package com.moowork.gradle.node.npm

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecResult

class NpxTask
    extends DefaultTask
{
    protected NpxExecRunner runner

    private List<?> args = []

    private ExecResult result

    private String command

    NpxTask()
    {
        this.runner = new NpxExecRunner( this.project )
        dependsOn( NpmSetupTask.NAME )

        this.project.afterEvaluate {
            afterEvaluate( this.project.node.nodeModulesDir )
        }
    }

    void afterEvaluate(nodeModulesDir) {
        if ( !this.runner.workingDir )
        {
            setWorkingDir( nodeModulesDir )
        }

        if ( !this.runner.workingDir.exists() )
        {
            this.runner.workingDir.mkdirs();
        }
    }

    void setArgs( final Iterable<?> value )
    {
        this.args = value.asList()
    }

    @Input
    String getCommand() {
        return command
    }

    void setCommand(String cmd )
    {
        this.command = cmd
    }

    @Input
    List<?> getArgs()
    {
        return this.args
    }

    void setEnvironment( final Map<String, ?> value )
    {
        this.runner.environment << value
    }

    void setWorkingDir( final Object value )
    {
        this.runner.workingDir = value
    }

    void setIgnoreExitValue( final boolean value )
    {
        this.runner.ignoreExitValue = value
    }

    void setExecOverrides( final Closure closure )
    {
        this.runner.execOverrides = closure
    }

    @Nested
    NpxExecRunner getRunner()
    {
        return runner
    }

    @Internal
    ExecResult getResult()
    {
        return this.result
    }

    @TaskAction
    void exec()
    {
        if ( this.command != null )
        {
            this.runner.arguments.addAll( this.command )
        }

        this.runner.arguments.addAll( this.args )
        this.result = this.runner.execute()
    }
}
