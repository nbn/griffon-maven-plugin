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
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * Compiles a Griffon project.
 *
 * @author Created for Grails by <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @author Ported to Griffon by <a href="mailto:nielsbechnielsen@gmail.com">Niels Bech Nielsen</a>
 * @since 1.3
 */
@Mojo(name="maven-compile", requiresProject=true, requiresDependencyResolution = ResolutionScope.COMPILE)
public class MvnCompileMojo extends AbstractGriffonMojo {

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		runGriffon("compile", "");
	}

}
