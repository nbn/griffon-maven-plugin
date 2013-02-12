package org.codehaus.griffon.maven;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.griffon.launcher.GriffonLauncher;
import org.codehaus.griffon.launcher.RootLoader;

/**
 * Says "Hi" to the user.
 * 
 * @goal sayhi
 */
public class GreetingMojo extends AbstractMojo {
	
	
/**
 * @parameter expression="${project}"
 * @readonly
 * @required
 */
	private MavenProject project;
	
	public void execute() throws MojoExecutionException{
		getLog().info("Hello, world.");
		
		RootLoader rootLoader;
		try {
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