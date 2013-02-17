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
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * Validate consistency between Griffon and Maven settings.
 *
 * @author Created for Grails by <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @author Ported to Griffon by <a href="mailto:nielsbechnielsen@gmail.com">Niels Bech Nielsen</a>
 * @description Determines whether the current directory contains a Griffon application or not, and creates one in the latter case.
 * @since 1.3
 */
@Mojo(name="init", defaultPhase=LifecyclePhase.INITIALIZE, requiresDependencyResolution=ResolutionScope.RUNTIME)
public class MvnInitializeMojo extends AbstractGriffonMojo {

	
    /**
     * Set this to 'true' to skip initializing a non-griffon project. 
     *
    * @since 1.3
    */
	@Parameter(property="griffon.create.init.skip")
   private boolean skip;

   
	@Parameter(defaultValue = "${project.artifactId}", readonly = true)
    private String artifactId;

	@Parameter(defaultValue = "${project.version}", readonly = true)
    private String version;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            getGriffonServices().readProjectDescriptor();
        } catch (final MojoExecutionException ex) {
            // Initialise the app.
        	if (!skip) {
	            getLog().info("Cannot read application info, so initialising new application.");
	            runGriffon("CreateApp", "--inplace --appVersion=" + version + " " + artifactId);
        	}
        }
    }
}
