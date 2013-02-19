/*
 * Copyright 2007 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.griffon.maven.plugin;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.util.DirectoryScanner;

/**
 * Package a Griffon application.
 *
 * @author <a href="mailto:nielsbechnielsen@gmail.com">Niels Bech Nielsen</a>
 * @since 1.3
 */
@Mojo(name="maven-package-app", requiresProject=true)
public class MvnPackageApplicationMojo extends AbstractGriffonMojo {

    /**
     * Name of the application jar to create. 
     * @since 1.3
     */
	@Parameter(property="griffon.jar.name", defaultValue="${project.build.finalName}")
	protected String jarName;

	
    /**
     * Target platform to use other than the default one.
     * Whenever a target platform is explicitly specified it will be used as artifact classifier
     *  
     * @since 1.3
     */
	@Parameter(property="griffon.target.platform")
     private String platform;

	@Override
	protected String getDefaultEnvironment() {
		return "prod";
	}

	@Component
	private MavenProjectHelper projectHelper;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		String name="--name=" + jarName;
		String args = name + " " + getPackageType();
		if (platform != null)
			args = "--platform=" + platform + " " + args;
		runGriffon("package", args);

		File packageFile = getPackageFile();
		getLog().info("Collected griffon artifact from " + packageFile);
		if (platform != null) {
			// Attach with classifier
			projectHelper.attachArtifact( getProject(), getPackageExtension(), platform, packageFile);
		} else {
			// Attach without classifier
			getProject().getArtifact().setFile(packageFile);
		}
	}

	protected String getPackageType() {
		return "jar";
	}
	
	protected String getPackageExtension() {
		return getPackageType();
	}

	protected File getPackageFile() throws MojoExecutionException {
		return findUnique("**/" + jarName + "*."+getPackageExtension(), getStandardExcludes());
		
	}
	protected String[] getStandardExcludes() {
		return new String[] {
			"griffon-app", "lib", "src", "scripts", "test", "wrapper"	
		};
	}
	
	protected final File findUnique(String include, String[] excludes) throws MojoExecutionException {
		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setBasedir(getBasedir());
		scanner.setCaseSensitive(true);
		scanner.setIncludes(new String[] { include });
		if (excludes != null)
			scanner.setExcludes(excludes);
		scanner.scan();
		String[] files = scanner.getIncludedFiles();
		if (files == null || files.length == 0) 
			throw new MojoExecutionException("Scanner found no match for pattern " + include);
		if (files.length > 1)
			getLog().warn("Scanner found multiple matches for pattern " + include + ", using first match");
		return new File(files[0]);
	}
}
