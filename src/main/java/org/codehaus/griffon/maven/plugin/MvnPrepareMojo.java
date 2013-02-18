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

import java.io.File;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;

/**
 * Prepare Griffon environment for Maven execution.
 *
 * @author <a href="mailto:nielsbechnielsen@gmail.com">Niels Bech Nielsen</a>
 * @description prepare a griffon scripting environment in buildDirectory
 * @since 1.3
 */
@Mojo(name="prepare", defaultPhase=LifecyclePhase.INITIALIZE, requiresDependencyResolution=ResolutionScope.RUNTIME)
public class MvnPrepareMojo extends AbstractGriffonMojo {
	//-- Parameters ------------------------------
	
	
	
	
	/**
	 * Specify a dependency for griffon templates.
	 * 
	 * @since 1.3
	 */
	@Parameter
	private Dependency templateDependency;

	
	//-- Collaborators ------------------------------
	
	/**
    * The Zip archiver.
    * @parameter  expression="${component.org.codehaus.plexus.archiver.UnArchiver#zip}"
    */
	@Component(role=org.codehaus.plexus.archiver.UnArchiver.class, hint="zip")
    private ZipUnArchiver zipUnArchiver;


	//-- Helper methods ------------------------------

	private Dependency getTemplateDependency() {
		if (templateDependency != null)
			return templateDependency;
		
		return super.getGriffonlikeDependency("griffon-template", "zip");
	}
	
	private boolean validGriffonScriptHome() {
		return griffonScriptHome != null && 
				griffonScriptHome.isDirectory() && 
				griffonScriptHome.canRead();
	}
	//-- Execution ------------------------------

    public void execute() throws MojoExecutionException, MojoFailureException {
		if (validGriffonScriptHome()) {
			getLog().debug("GriffonScriptHome already exists as " + griffonScriptHome.getAbsolutePath());
			return;
		}
   		if (!griffonScriptHome.mkdirs())
   			throw new MojoFailureException("Unable to create directory specified by griffonScriptHome : " + griffonScriptHome.getAbsolutePath());
    		
       	try {
			Dependency dependency = getTemplateDependency();
			File file= super.resolveDependencyFile(dependency);
			zipUnArchiver.setSourceFile(file);
			zipUnArchiver.setDestDirectory(griffonScriptHome);
			zipUnArchiver.extract();
		} catch (Exception e) {
			throw new MojoExecutionException("Unable to prepare griffon environment", e);
		}
    }

}
