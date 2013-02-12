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

/**
 * Validate consistency between Griffon and Maven settings.
 *
 * @author <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @author <a href="mailto:nielsbechnielsen@gmail.com">Niels Bech Nielsen</a>
 * @description Determines whether the current directory contains a Griffon application or not, and creates one in the latter case.
 * @goal init
 * @phase initialize
 * @requiresDependencyResolution runtime
 * @since 0.1
 */
public class MvnInitializeMojo extends AbstractGriffonMojo {

	
    /**
     * Set this to 'true' to skip initializing a non-griffon project. 
     *
    * @parameter expression="${griffon.create.init.skip}"
    * @since 0.3
    */
   private boolean skip;

   
    /**
     * The artifact id of the project.
     *
     * @parameter expression="${project.artifactId}"
     * @required
     * @readonly
     */
    private String artifactId;

    /**
     * The version id of the project.
     *
     * @parameter expression="${project.version}"
     * @required
     * @readonly
     */
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
