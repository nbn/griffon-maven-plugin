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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.griffon.launcher.GriffonLauncher;
import org.codehaus.griffon.launcher.RootLoader;
import org.codehaus.griffon.maven.tools.GriffonLauncherHolder;
import org.codehaus.griffon.maven.tools.GriffonServices;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.collection.CollectRequest;
import org.sonatype.aether.graph.DependencyFilter;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.ArtifactRequest;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.aether.resolution.ArtifactResult;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.util.artifact.JavaScopes;
import org.sonatype.aether.util.filter.DependencyFilterUtils;

/**
 * 
 * @author Created for Grails by <a href="mailto:aheritier@gmail.com">Arnaud
 *         HERITIER</a>
 * @author Ported to Griffon by <a
 *         href="mailto:nielsbechnielsen@gmail.com">Niels Bech Nielsen</a>
 * 
 */
public abstract class AbstractGriffonMojo extends AbstractMojo {
	
	// -- Parameter section --------------------------------------

	/**
	 * The version of the scripts needed for the execution. If not specified,
	 * the mojo will use same version as the plugin
	 * 
	 * @since 1.3
	 */
	@Parameter(property="griffon.script.version")
	private String griffonScriptVersion;

    /**
     * The default Griffon environment to use.
     * This property takes precedence over the griffonEnv property
	 * @since 1.3
     *
     */
	@Parameter(property="griffon.env")
    protected String env;

    /**
     * The default Griffon environment to use.
     * If not set a default environment will be used, depending on which mojo being executed.
	 * @since 1.3
     *
     */
	@Parameter(property="environment")
    protected String griffonEnv;
	
	/**
	 * Specify the work directory while building.
	 * Set the build work dir to something other than default (which is ~/.griffon, I think)
	 * @since 1.3
	 */
	@Parameter(property="griffon.work.dir")
	protected String workDir;

	/**
	 * Specify the target space for the griffon templates.
	 * Normally, this does not need to be changed
	 * 
	 * @since 1.3
	 */
	@Parameter(property="griffon.script.home", defaultValue="${basedir}/template")
	protected File griffonScriptHome;
	/*
	 * Normally, I would place the griffonScriptHome inside ${project.build.directory}, but the Package.groovy script seems to delete
	 * the target directory completely and chances are that ${project.build.directory} == 'target'..(sad smiley)
	 */

	/**
	 * The directory where is launched the mvn command.
	 * Normally, this does not need to be changed
	 * 
	 * @since 1.3
	 */
	@Parameter(defaultValue="${basedir}", required=true)
	private File basedir;

	// -- Component section --------------------------------------
	
	@Parameter(defaultValue="${project}", readonly=true)
	private MavenProject project;

	@Component
	private GriffonServices griffonServices;

	@Parameter(defaultValue="${plugin}", readonly = true)
	private PluginDescriptor pluginDescriptor;

	@Component
	private RepositorySystem repoSystem;

	@Parameter(defaultValue = "${repositorySystemSession}", readonly = true)
	private RepositorySystemSession repoSession;

	@Parameter(defaultValue = "${project.remoteProjectRepositories}", readonly = true)
	private List<RemoteRepository> remoteRepos;


	// -- subclass service section -----------------------------
	
	/**
	 * Returns the {@code GriffonServices} instance used by the plugin with the
	 * base directory of the services object set to the configured base
	 * directory.
	 * 
	 * @return The underlying {@code GriffonServices} instance.
	 */
	protected final GriffonServices getGriffonServices() {
		griffonServices.setBasedir(basedir);
		return griffonServices;
	}

	/**
	 * Returns the configured base directory for this execution of the plugin.
	 * 
	 * @return The base directory.
	 */
	protected final File getBasedir() {
		return this.basedir;
	}
	
    protected final String getEnvironment() {
        if(env == null) {
        	if (griffonEnv == null)
        		return getDefaultEnvironment();
        	else
        		return griffonEnv;
        }
        return env;
     }
    
    protected final MavenProject getProject() {
    	return this.project;
    }
    
    

	protected final Dependency getGriffonlikeDependency(String artifactId, String artifactType) {

		getLog().debug(
				"Searching for a griffon resources (" + artifactId + "," + artifactType + ")using "
						+ pluginDescriptor.getArtifactId());
		Dependency dependency = new Dependency();
		dependency.setGroupId(pluginDescriptor.getGroupId());
		dependency.setArtifactId(artifactId);
		dependency.setType(artifactType);
		if (griffonScriptVersion != null)
			dependency.setVersion(griffonScriptVersion);
		else
			dependency.setVersion(pluginDescriptor.getVersion());
		return dependency;
	}

	// -- subclass service section -----------------------------

	/**
	 * The default environment for this mojo.
	 * Can be overridden in particular mojos should they need another default environment.
	 * @return
	 */
   protected String getDefaultEnvironment() {
    	return "dev";
    }


	// -- Launching Griffon section --------------------------------------

	protected final int runGriffon(String target, String args) throws MojoExecutionException {
		return this.runGriffon(target, args, getEnvironment());
	}

	protected final int runGriffon(String target, String args, String environment) throws MojoExecutionException {
		getLog().debug("About to run 'griffon " + target + " " + args + "' for environment " + environment);

		GriffonLauncher launcher = GriffonLauncherHolder.get();
		if (launcher == null) {
			launcher = createLauncher();
			GriffonLauncherHolder.set(launcher);
		}
		
		return launcher.launch(target, args, environment);

	}

	private GriffonLauncher createLauncher() throws MojoExecutionException {
		RootLoader rootLoader = new RootLoader(createCP());
		
		GriffonLauncher launcher = new GriffonLauncher(rootLoader, 
							griffonScriptHome.getAbsolutePath(), 
							getBasedir().getAbsolutePath());
		
		launcher.setClassesDir(new File(project.getBuild().getOutputDirectory()));
		launcher.setTestClassesDir(new File(project.getBuild().getTestOutputDirectory()));
		if (workDir != null)
			launcher.setGriffonWorkDir(new File(workDir));
		return launcher;
	}

	private URL[] createCP() throws MojoExecutionException {
		Dependency coreDependency = getGriffonlikeDependency("griffon-cli", "jar");
		Artifact artifact = new DefaultArtifact(coreDependency.getGroupId(), coreDependency.getArtifactId(),
				coreDependency.getType(), coreDependency.getVersion());
		DependencyFilter classpathFilter = DependencyFilterUtils.classpathFilter(JavaScopes.RUNTIME);
		CollectRequest collectRequest = new CollectRequest();
		collectRequest.setRoot(new org.sonatype.aether.graph.Dependency(artifact, JavaScopes.RUNTIME));
		collectRequest.setRepositories(remoteRepos);

		try {
			List<URL> cp = new ArrayList<URL>();
			List<ArtifactResult> dependencies = this.repoSystem.resolveDependencies(repoSession, collectRequest, classpathFilter);
			for (ArtifactResult artifactResult : dependencies) {
				cp.add(artifactResult.getArtifact().getFile().toURI().toURL());
			}
			return cp.toArray(new URL[cp.size()]);
		} catch (Exception e) {
			throw new MojoExecutionException("Unable to resolve dependencies for " + coreDependency, e);
		}
	}

	public File resolveDependencyFile(Dependency dependency) throws MojoExecutionException {
		Artifact artifact = new DefaultArtifact(dependency.getGroupId(), dependency.getArtifactId(), dependency.getType(),
				dependency.getVersion());
		ArtifactRequest request = new ArtifactRequest();
		request.setArtifact(artifact);
		request.setRepositories(remoteRepos);

		ArtifactResult result;
		try {
			result = repoSystem.resolveArtifact(repoSession, request);
		} catch (ArtifactResolutionException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
		return result.getArtifact().getFile();
	}
}
