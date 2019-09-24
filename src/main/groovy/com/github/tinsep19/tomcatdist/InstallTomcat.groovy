package com.github.tinsep19.tomcatdist

import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import javax.inject.Inject

class InstallTomcat extends DefaultTask {
    @Input
    final Provider<String> version

    @Internal
    final Provider<Directory> distributionsDir

    @OutputDirectory
    final Provider<Directory> installDir

    @Inject
    InstallTomcat(DirectoryProperty distsDir, Property<String> installVersion){
        version = installVersion
        distributionsDir = distsDir
        installDir = distsDir.dir(installVersion.map{ "apache-tomcat-${it}" })
    }

    @TaskAction
    void install() {
        def dep = project.dependencies.create(version.map{ "org.apache.tomcat:tomcat:${it}@zip" }.get())
        def config = project.configurations.detachedConfiguration(dep)
        project.copy {
            from(project.zipTree(config.singleFile))
            into (distributionsDir)
        }
    }

}
