/*
      Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
*/
package org.codehaus.griffon.maven;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.griffon.launcher.GriffonLauncher;
import org.codehaus.griffon.launcher.RootLoader;

/**
 * Says "Hi" to the user.
 * 
 */
@Mojo(name="sayhi")
public class GreetingMojo extends AbstractMojo {
	
	
        @Parameter (property="project", readonly=true, required=true)
	private MavenProject project;
	
	public void execute() throws MojoExecutionException{
		getLog().info("Hello, world.");
		
		RootLoader rootLoader;
		try {
			getLog().info("Using classpath in project :" + project.getArtifactId());
			URL[] urls = createCP();
			for (URL url : urls) {
				getLog().info("url = " + url.toExternalForm());
			}
			System.exit(2);

			rootLoader = new RootLoader(createCP());
		} catch (MalformedURLException e) {
			throw new MojoExecutionException("Unable to create CP", e);
		}
		String packaging = getGriffonPackagesToPackage();
		GriffonLauncher launcher = new GriffonLauncher(rootLoader, "/Users/nielsbechnielsen/workspace/ws-erhverv/FirstGriffon/template", ".");
		launcher.setClassesDir(new File("/Users/nielsbechnielsen/workspace/ws-erhverv/FirstGriffon/target/griffon-classes"));
		launcher.launch("package", packaging, "dev");
		
	}

	private String getGriffonPackagesToPackage() {
		String packaging = project.getPackaging();
		
		// Get the default maven package
		if ("griffon-app".equals(packaging))
			packaging = "jar";
		else if ("griffon-applet".equals(packaging))
			packaging = "applet";
		else if ("griffon-webstart".equals(packaging))
			packaging = "webstart";
		else if ("griffon-assembly".equals(packaging))
			packaging = "zip";
		
		// Later see if user specify additional packaging
		return packaging;
	}
	
	public URL[] createCP() throws MalformedURLException {
		List<URL> result = new ArrayList<URL>();
		
		File lib = new File("/Users/nielsbechnielsen/Tools/griffon-1.2.0/lib");
		File dist = new File("/Users/nielsbechnielsen/Tools/griffon-1.2.0/dist");
		for (File file : lib.listFiles()) {
			result.add(file.toURI().toURL());
		}
		for (File file : dist.listFiles()) {
			result.add(file.toURI().toURL());
		}
		
		return result.toArray(new URL[result.size()]);
	}

}
