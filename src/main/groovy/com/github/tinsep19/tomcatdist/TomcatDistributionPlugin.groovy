package com.github.tinsep19.tomcatdist

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.distribution.plugins.DistributionPlugin
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Sync
import org.gradle.internal.jvm.Jvm

class TomcatDistributionPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.pluginManager.apply(DistributionPlugin)
        Sync createBase = project.tasks.getByName("installDist")

        Provider<File> defaultCatalinaBase = project.provider { createBase.destinationDir }
        Provider<File> defaultJavaHome = project.provider { Jvm.current().javaHome }

        def extension = project.extensions.create("tomcatDistribution",
                TomcatDistributionPluginExtension);

        def installTomcat = project.tasks.create("installTomcat",
                InstallTomcat,
                extension.distributionsDir,
                extension.version)

        project.tasks.create("tomcatStart", TomcatExec) {
            args = ["start"]
            dependsOn("installTomcat","installDist")
            catalinaHome.convention(installTomcat.installDir)
            catalinaBase.convention(defaultCatalinaBase)
            javaHome.convention(defaultJavaHome)
            environment.convention(extension.environment)
        }

        project.tasks.create("tomcatJpdaStart", TomcatExec) {
            args = ["jpda", "start"]
            dependsOn("installTomcat","installDist")
            catalinaHome.convention(installTomcat.installDir)
            catalinaBase.convention(defaultCatalinaBase)
            javaHome.convention(defaultJavaHome)
            environment.convention(extension.environment)
        }

        project.tasks.create("tomcatStop", TomcatExec) {
            args = ["stop"]
            dependsOn("installTomcat")
            catalinaHome.convention(installTomcat.installDir)
            catalinaBase.convention(defaultCatalinaBase)
            javaHome.convention(defaultJavaHome)
            environment.convention(extension.environment)
        }
    }
}