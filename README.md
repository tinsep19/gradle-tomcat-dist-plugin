# gradle-tomcat-dist-plugin

This is a Gradle plugin to use Tomcat.
This plugin provides following tasks:

- InstallTomcat : Install the specified version of Tomcat
- TomcatExec : Call catalina.sh with arguments and ENV
- InstallDist : Distribution plugin provides. to use as CATALINA_BASE


## Usage

```build.gradle
plugins { id 'com.github.tinsep19.tomcat-dist' version "X.Y.Z" }
tomcatDistribution {
  version = "8.5.43"
  distributionsDir = file("tomcat-dist") 
}
```

You should add '/tomcat-dist/' in .gitignore, because installTomcat task
will download and unzip the tomcat distribution file into the `$project/tomcat-dist` dir.



