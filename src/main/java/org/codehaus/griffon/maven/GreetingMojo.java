package org.codehaus.griffon.maven;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.griffon.launcher.GriffonLauncher;
import org.codehaus.griffon.launcher.RootLoader;

/**
 * Says "Hi" to the user.
 * 
 * @goal sayhi
 */
public class GreetingMojo extends AbstractMojo {
	// Assuming we know which script to run..
	File scriptFile = new File(
			"/Users/nielsbechnielsen/Tools/griffon-1.2.0/scripts/Package.groovy");
	File baseDir = new File(".");

	public void execute() throws MojoExecutionException{
		getLog().info("Hello, world.");
		
		RootLoader rootLoader;
		try {
			rootLoader = new RootLoader(createCP());
		} catch (MalformedURLException e) {
			throw new MojoExecutionException("Unable to create CP", e);
		}
		GriffonLauncher launcher = new GriffonLauncher(rootLoader, null, ".");
		launcher.launch("package", "", "dev");
		
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