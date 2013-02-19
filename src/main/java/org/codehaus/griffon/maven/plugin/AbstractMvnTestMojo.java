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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Base class for executing griffon tests via maven.
 *
 * @author Created for Grails by <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @author Ported to Griffon by <a href="mailto:nielsbechnielsen@gmail.com">Niels Bech Nielsen</a>
 * @since 1.3
 */
public abstract class AbstractMvnTestMojo extends AbstractGriffonMojo {

    /**
     * Set this to 'true' to bypass unit tests entirely. Its use is
      * NOT RECOMMENDED, but quite convenient on occasion.
      * @since 1.3
      */
	@Parameter(property="skipTests")
     private boolean skipTests;

     /**
      * Set this to 'true' to bypass unit tests entirely. Its use is
      * NOT RECOMMENDED, but quite convenient on occasion.
      *
     * @since 1.3
     */
	@Parameter(property="griffon.test.skip")
    private boolean skip;

    /**
     * Set this to 'true' to bypass unit tests entirely. Its use is
     * NOT RECOMMENDED, but quite convenient on occasion.
     *
     * @since 1.3
     */
	@Parameter(property="maven.test.skip")
    private Boolean mavenSkip;

    /**
     * Set this to "true" to ignore a failure during testing. Its use is NOT RECOMMENDED, but quite convenient on
     * occasion.
     *
     * @since 1.3
     */
	@Parameter(property="maven.test.failure.ignore")
    private boolean testFailureIgnore;

    @Override
	public void execute() throws MojoExecutionException, MojoFailureException {
        if (skipTests || skip || (mavenSkip != null && mavenSkip.booleanValue())) {
            getLog().info("Tests are skipped.");
            return;
        }

        // -----------------------------------------------------------------------
        // If the current environment is test or production, default to not run
        // the tests
        // -----------------------------------------------------------------------

        String environment = getEnvironment();
		if (mavenSkip == null && environment != null) {
            if (environment.equals("test") || environment.startsWith("prod")) {
                getLog().info("Skipping tests as the current environment is set to test or production.");
                getLog().info("Set maven.test.skip to false to prevent this behaviour");

                return;
            }
        }
		
		try {
			int result = runGriffon("TestApp", getTestArguments());
			switch (result) {
			case 0:
				getLog().debug("Griffon test execution successful");
				break;
			case 1:
				getLog().debug("Griffon test execution returned failure");
				if (!testFailureIgnore)
					throw new MojoFailureException("Unit tests failed");
				break;
			default:
				getLog().warn("Griffon text execution returned unexpected result " + result);
				
			}
		} catch (MojoExecutionException me) {
			if (!testFailureIgnore) {
				throw me;
			} else {
				getLog().debug("Griffon test execution throw exception, which will be ignored", me);
			}
		}
	 
	}

    // -- Template method..
	protected abstract String getTestArguments();

}
