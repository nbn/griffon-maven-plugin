![Griffon][logo] Maven plugin
===============================

[Griffon][1] is a desktop application development platform for the JVM.

The Griffon Maven plugin is an extension to Griffon to fascilitate [Maven][2] as an
alternative build mechanism.

The plugin is templated from the [grails-maven-plugin][3], whos merits we
hereby recognize.

The plugin is currently under development. Find missing pieces and examples under project-resources.

Using the maven plugin
=======================
Look inside the project-resources directory and find the example-pom.xml
Use it as a template for your project.
Remember to align artifactId with griffon application name as posted in application.properties
Remember to align version with griffon application version as posted in application.properties
run 
  mvn install

At present there are no griffon binaries version 1.3.0, so you will have to build and install those locally
and you would also need to install the template artifact for the specific griffon version (See the batch file)


[logo]: http://media.xircles.codehaus.org/_projects/griffon/_logos/medium.png
[1]: http://griffon-framework.org
[2]: http://http://maven.apache.org/ 
[3]: http://github.com/grails/grails-maven
