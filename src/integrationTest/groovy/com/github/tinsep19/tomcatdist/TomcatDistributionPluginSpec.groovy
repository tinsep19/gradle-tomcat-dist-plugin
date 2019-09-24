package com.github.tinsep19.tomcatdist

import org.gradle.api.Project
import org.gradle.api.distribution.plugins.DistributionPlugin
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Unroll

class TomcatDistributionPluginSpec extends Specification {
    def projectWithPlugin() {
        def project = ProjectBuilder.builder().build()
        project.with {
            apply(plugin: 'com.github.tinsep19.tomcat-dist')
            tomcatDistribution {
                version = "8.0.53"
                distributionsDir = file("tomcat-dist")
            }
        }
        return project
    }

    def "TomcatDistributionPlugin has plugin-id 'com.github.tinsep19.tomcat-dist'" () {
        expect:
        projectWithPlugin().plugins.hasPlugin(TomcatDistributionPlugin)
    }

    def "TomcatDistributionPlugin use with DistributionPlugin"() {
        when:
        def project = projectWithPlugin()

        then:
        project.plugins.hasPlugin('distribution')
        project.plugins.hasPlugin('com.github.tinsep19.tomcat-dist')
    }

    def "TomcatDistributionPlugin define extensions.tomcatDistribution" () {
        when:
        Project project = projectWithPlugin()

        then:
        project.extensions.getByName("tomcatDistribution") in TomcatDistributionPluginExtension

        when:
        TomcatDistributionPluginExtension extension = project.extensions.getByName("tomcatDistribution")

        then:
        extension.version.get() == "8.0.53"
        extension.distributionsDir.get().asFile == project.file("tomcat-dist")
    }

    @Unroll
    def "TomcatDistributionPlugin provides #taskName type: #taskClass" () {
        expect:
        projectWithPlugin().tasks.getByName(taskName) in taskClass

        where:
        taskName          || taskClass
        "installTomcat"     || InstallTomcat
        "tomcatStart"       || TomcatExec
        "tomcatJpdaStart"   || TomcatExec
        "tomcatStop"        || TomcatExec
    }

    def "installTomcat can not specify property" () {
        when:
        def project = ProjectBuilder.builder().build()
        project.with {
            apply(plugin: 'com.github.tinsep19.tomcat-dist')
            tomcatDistribution {
                version = "8.0.53"
                distributionsDir = file("tomcat-dist")
            }
            tasks.withType(InstallTomcat) {
                version = "8.0.54"
            }
        }

        then:
        thrown(RuntimeException)
    }

    @Unroll
    def "#taskName has #args as args" () {
        when:
        TomcatExec task = projectWithPlugin().tasks.getByName(taskName)

        then:
        task.args.get() == args

        where:
        taskName || args
        "tomcatStart" || ["start"]
        "tomcatStop" || ["stop"]
        "tomcatJpdaStart" || ["jpda", "start"]
    }
}