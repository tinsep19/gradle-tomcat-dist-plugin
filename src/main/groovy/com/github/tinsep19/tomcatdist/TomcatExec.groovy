package com.github.tinsep19.tomcatdist

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

import javax.inject.Inject

class TomcatExec extends DefaultTask {
    @InputDirectory
    final DirectoryProperty catalinaHome

    @InputDirectory
    final DirectoryProperty catalinaBase

    @InputDirectory
    final DirectoryProperty javaHome

    @Input
    final MapProperty<String, String> environment

    @Input
    final ListProperty<String> args

    @Inject
    TomcatExec(ObjectFactory objects) {
        catalinaBase = objects.directoryProperty()
        catalinaHome = objects.directoryProperty()
        javaHome = objects.directoryProperty()
        environment = objects.mapProperty(String, String)
        args = objects.listProperty(String)
    }

    List<String> generateLaunchCommand() {
        if(org.gradle.internal.os.OperatingSystem.current().isWindows()) {
            ["cmd", "/k", catalinaHome.file("bin/catalina.bat").get().asFile.absolutePath ]
        } else {
            [catalinaHome.file("bin/catalina.sh").get().asFile.absolutePath]
        }
    }

    @TaskAction
    void execute() {
        File workingDir = project.file(catalinaBase)
        def commandLine = generateLaunchCommand() + args.get()
        def process = new ProcessBuilder()

        println(commandLine)
        println(project.file(catalinaBase).absolutePath)
        println(project.file(catalinaHome).absolutePath)
        println(project.file(javaHome).absolutePath)

        process.command(commandLine)

        process.environment().clear()
        process.environment().putAll(environment.get())
        // Override BootEnv
        process.environment().put("CATALINA_BASE", project.file(catalinaBase).absolutePath)
        process.environment().put("CATALINA_HOME", project.file(catalinaHome).absolutePath)
        process.environment().put("JAVA_HOME", project.file(javaHome).absolutePath)
        process.directory(workingDir)
        process.start()
    }
}
