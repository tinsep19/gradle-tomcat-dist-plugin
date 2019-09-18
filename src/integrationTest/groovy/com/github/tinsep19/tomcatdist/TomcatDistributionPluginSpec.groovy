package com.github.tinsep19.tomcatdist

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Unroll

class TomcatDistributionPluginSpec extends Specification {
    def projectWithPlugin() {
        def project = ProjectBuilder.builder().build()
        project.with {
            apply(plugin: 'com.github.tinsep19.tomcat-dist')
        }
        return project
    }
    def "TomcatDistributionPlugin provides plugin-id 'com.github.tinsep19.tomcat-dist'"() {
        expect:
        projectWithPlugin().plugins.getPlugin('com.github.tinsep19.tomcat-dist') in TomcatDistributionPlugin
    }
}