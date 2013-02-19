/*
 * Copyright 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.griffon.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.codehaus.griffon.maven.tools.GriffonProject;

/**
 * Validate consistency between Griffon and Maven settings.
 *
 * @author Created for Grails by <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @author Ported to Griffon by <a href="mailto:nielsbechnielsen@gmail.com">Niels Bech Nielsen</a>
 * @description Validate consistency between Griffon (application.properties) and Maven (pom.xml) settings.
 * @since 1.3
 */
@Mojo(name="validate", defaultPhase=LifecyclePhase.VALIDATE, requiresDependencyResolution=ResolutionScope.RUNTIME)
public class MvnValidateMojo extends AbstractGriffonMojo {

    /**
     * The artifact id of the project.
     *
     */
	@Parameter(defaultValue = "${project.artifactId}", readonly = true)
    private String artifactId;

    /**
     * The version id of the project.
     *
     */
	@Parameter(defaultValue = "${project.version}", readonly = true)
    private String version;

    public void execute() throws MojoExecutionException, MojoFailureException {
        GriffonProject griffonProject;
        try {
            griffonProject = getGriffonServices().readProjectDescriptor();
        } catch (final MojoExecutionException e) {
            getLog().info("No Griffon application found - skipping validation.");
            return;
        }

        if (!artifactId.equals(griffonProject.getAppName())) {
            throw new MojoFailureException("app.name [" + griffonProject.getAppName() + "] in " +
                "application.properties is different of the artifactId [" + artifactId + "] in the pom.xml");
        }

        // We have to set the application version in Griffon settings for old versions		
        if (griffonProject.getAppVersion() == null) {
            griffonProject.setAppVersion(GriffonProject.DEFAULT_APP_VERSION);
            getLog().warn("application.properties didn't contain an app.version property");
            getLog().warn("Setting to default value '" + griffonProject.getAppVersion() + "'.");

            getGriffonServices().writeProjectDescriptor(getBasedir(), griffonProject);

        }
        
        final String pomVersion = version.trim();
        final String griffonVersion = griffonProject.getAppVersion().trim();

        if (!griffonVersion.equals(pomVersion)) {
            throw new MojoFailureException("app.version [" + griffonVersion + "] in " +
                "application.properties is different of the version [" + pomVersion + "] in the pom.xml");
        }

    }
}
