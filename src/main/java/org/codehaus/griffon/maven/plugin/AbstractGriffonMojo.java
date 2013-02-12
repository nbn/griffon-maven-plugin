package org.codehaus.griffon.maven.plugin;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.griffon.maven.tools.GriffonServices;

public abstract class AbstractGriffonMojo extends AbstractMojo {

	/**
	 * The directory where is launched the mvn command.
	 * 
	 * @parameter default-value="${basedir}"
	 * @required
	 */
	private File basedir;

    /**
     * @component
     * @readonly
     */
    private GriffonServices griffonServices;

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

	protected final void runGriffon(String target, String args)
			throws MojoExecutionException {
		
		throw new MojoExecutionException ("Don't know how to run griffon "+ target + " " + args);

	}
}
