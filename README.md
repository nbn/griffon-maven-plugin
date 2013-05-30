![Griffon][logo] Maven plugin
===============================

[Griffon][1] is a desktop application development platform for the JVM.

The Griffon Maven plugin is an extension to Griffon to fascilitate [Maven][2] as an
alternative build mechanism.

The plugin is templated from the [grails-maven-plugin][3], whos merits we
hereby recognize.


Using the maven plugin
=======================

Here is an example pom for the griffon sample project FileViewer:

<?xml version="1.0" encoding="utf-8"?>
<!-- Copyright 2007 the original author or authors. Licensed under the Apache
    License, Version 2.0 (the "License"); you may not use this file except in
    compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software distributed
    under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
    OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License. -->
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>org.griffon</groupId>
    <!--
         ArtifactId and version should match with application.properties. Otherwise it will fail.
    -->
    <artifactId>FileViewer</artifactId>
    <version>0.1</version>

    <!-- 
        Using the griffon-zip packaging to get the entire zip file as a product.
        Alternatively, you can use either griffon-app or griffon-webstart
        as packaging
    -->
    <packaging>griffon-zip</packaging>


    <!-- This project depends on Griffon and all its derivatives -->
    <dependencies>
        <dependency>
                <groupId>org.codehaus.griffon</groupId>
                <artifactId>griffon-cli</artifactId>
                <version>1.3.0</version>
        </dependency>
    </dependencies>

<!-- 
    The repositories and plugin repositories specified here can also be included
    in the maven settings.xml file if you wish. Shown here for completion purposes.
-->
<repositories>
      <!-- 
           Griffon use the non-maven-org editions of spring bundles.
           So we need to include this repository to transitive resolve
           any griffon artifacts
      -->
      <repository>
        <id>com.springsource.repository.bundles.release</id>
        <name>EBR Spring Release Repository</name>
        <url>http://repository.springsource.com/maven/bundles/release</url>
      </repository>
      <!--
          This repository contains additional dependencies used by the griffon-maven-plugin
      -->
      <repository>
        <id>bintray-nbn-maven-griffon-maven-plugin</id>
        <name>Bintray repository</name>
        <url>http://dl.bintray.com/nbn/maven</url>
      </repository>
</repositories>

<!-- 
   The plugin is only posted here at the moment, so need to include it
-->
<pluginRepositories>
      <pluginRepository>
        <id>bintray-nbn-maven-griffon-maven-plugin</id>
        <name>Bintray repository</name>
        <url>http://dl.bintray.com/nbn/maven</url>
      </pluginRepository>
</pluginRepositories>

    <build>
        <plugins>
            <plugin>
                <groupId>org.codehaus.griffon</groupId>
                <artifactId>griffon-maven-plugin</artifactId>
                <version>1.3.0</version>
                <extensions>true</extensions>
                <configuration>
                  <!-- <griffonEnv>is mojo dependent(test is test, package is prod), but can be locked in</griffonEnv -->
                  <!-- <griffonScriptHome>${basedir}/template</griffonScriptHome> -->
                  <!-- <griffonScriptVersion>Should match version used (default follows plugin version)</grifffonScriptVersion> -->
                  <!-- <workDir>Script work dir. Defaults to ~/.griffon</workDir> -->
                </configuration>
            </plugin>

            <!-- 
                The Griffon scripts use dist and staging for builds, so add these
                to the default clean target along with our plugin template directory
            -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-clean-plugin</artifactId>
                <version>2.5</version>
                <configuration>
                  <filesets>
                    <fileset>
                      <directory>template</directory>
                    </fileset>
                    <fileset>
                      <directory>dist</directory>
                    </fileset>
                    <fileset>
                      <directory>staging</directory>
                    </fileset>
                  </filesets>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>


[logo]: http://media.xircles.codehaus.org/_projects/griffon/_logos/medium.png
[1]: http://griffon-framework.org
[2]: http://http://maven.apache.org/ 
[3]: http://github.com/grails/grails-maven
