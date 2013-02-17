package org.codehaus.griffon.maven.plugin;

import java.io.File;
import java.net.MalformedURLException;
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
import org.codehaus.griffon.maven.tools.GriffonServices;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.collection.CollectRequest;
import org.sonatype.aether.collection.CollectResult;
import org.sonatype.aether.collection.DependencyCollectionException;
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

	/**
	 * The version of the scripts needed for the execution. If not specified,
	 * the mojo will use same version as the plugin
	 * 
	 * @since 1.3
	 */
	@Parameter(property="griffonScriptVersion")
	private String griffonScriptVersion;

	/**
	 * Specify the target space for the griffon templates.
	 * 
	 * @since 1.3
	 */
	@Parameter(property="griffonScriptHome", defaultValue="${project.build.directory}/template")
	protected File griffonScriptHome;

	/**
	 * The directory where is launched the mvn command.
	 * 
	 * @since 1.3
	 */
	@Parameter(defaultValue="${basedir}", required=true)
	private File basedir;

	/**
	 * POM
	 * 
	 */
	@Parameter(defaultValue="${project}", readonly=true)
	protected MavenProject project;

	@Component
	private GriffonServices griffonServices;

	/**
	 * plugin.
	 * 
	 */
	@Parameter(defaultValue="${plugin}", readonly = true)
	private PluginDescriptor pluginDescriptor;

	@Component
	private RepositorySystem repoSystem;

	@Parameter(defaultValue = "${repositorySystemSession}", readonly = true)
	private RepositorySystemSession repoSession;

	@Parameter(defaultValue = "${project.remotePluginRepositories}", readonly = true)
	private List<RemoteRepository> remoteRepos;

	/**
	 * Returns the {@code GriffonServices} instance used by the plugin with the
	 * base directory of the services object set to the configured base
	 * directory.
	 * 
	 * @return The underlying {@code GriffonServices} instance.
	 */
	protected GriffonServices getGriffonServices() {
		griffonServices.setBasedir(basedir);
		return griffonServices;
	}

	/**
	 * Returns the configured base directory for this execution of the plugin.
	 * 
	 * @return The base directory.
	 */
	protected File getBasedir() {
		return this.basedir;
	}

	protected Dependency getGriffonlikeDependency(String artifactId, String artifactType) {

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

	protected final void runGriffon(String target, String args) throws MojoExecutionException {
		this.runGriffon(target, args, "dev");
	}

	protected final void runGriffon(String target, String args, String environment) throws MojoExecutionException {

		getLog().debug("About to run 'griffon " + target + " " + args + "' for environment " + environment);

		getLog().info("Using classpath :");
		URL[] urls = createCP();
		for (URL url : urls) {
			getLog().info("url = " + url.toExternalForm());
		}
		// System.exit(2);

		RootLoader rootLoader = new RootLoader(createCP());
		GriffonLauncher launcher = new GriffonLauncher(rootLoader, griffonScriptHome.getAbsolutePath(), getBasedir()
				.getAbsolutePath());
		launcher.setClassesDir(new File(project.getBuild().getOutputDirectory()));
		launcher.setTestClassesDir(new File(project.getBuild().getTestOutputDirectory()));
		launcher.launch(target, args, environment);

	}

	private URL[] createCP() throws MojoExecutionException {
		Dependency coreDependency = getGriffonlikeDependency("griffon-cli", "jar");
		Artifact artifact = new DefaultArtifact(coreDependency.getGroupId(), coreDependency.getArtifactId(),
				coreDependency.getType(), coreDependency.getVersion());
		DependencyFilter classpathFilter = DependencyFilterUtils.classpathFilter(JavaScopes.RUNTIME);
		CollectRequest collectRequest = new CollectRequest();
		collectRequest.setRoot(new org.sonatype.aether.graph.Dependency(artifact, JavaScopes.RUNTIME));
		collectRequest.setRepositories(remoteRepos);

		getLog().info("Repositories");
		for (RemoteRepository element : remoteRepos) {
			getLog().info("repo :" + element.toString());
		}
		getLog().info("************");
		
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
