package com.github.tinsep19.tomcatdist

import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory

import javax.inject.Inject
import java.security.Provider

/**
 * tomcatDistribution {
 *   version = "8.0.53"
 *   distributionsDir = project.file("tomcat-dist")
 * }
 *
 * Task
 *
 */

class TomcatDistributionPluginExtension {
    final DEFAULT_TOMCAT_DISTRIBUTIONS_DIRNAME = "tomcat-dist"
    final Property<String> version
    final DirectoryProperty distributionsDir
    final MapProperty<String, String> environment

    @Inject
    TomcatDistributionPluginExtension(ObjectFactory objects, ProjectLayout layout, ProviderFactory provider) {
        version = objects.property(String)
        distributionsDir = objects.directoryProperty()
                .convention(layout.projectDirectory.dir(DEFAULT_TOMCAT_DISTRIBUTIONS_DIRNAME))
        environment = objects.mapProperty(String, String)
    }
}