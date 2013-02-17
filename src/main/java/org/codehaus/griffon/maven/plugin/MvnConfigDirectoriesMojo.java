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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Set sources/tests directories to be compatible with the directories layout used by griffon.
 *
 * @author Created for Grails by <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @author Ported to Griffon by <a href="mailto:nielsbechnielsen@gmail.com">Niels Bech Nielsen</a>
 * @description Set sources/tests directories to be compatible with the directories layout used by griffon.
 * @since 1.3
 */
@Mojo(name="config-directories", defaultPhase=LifecyclePhase.GENERATE_SOURCES, requiresProject=true)
public class MvnConfigDirectoriesMojo extends AbstractGriffonMojo {

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
        final File projectDir = getBasedir();
        // Add sources directories
        this.project.addCompileSourceRoot((new File(projectDir, "/grails-app/conf")).getAbsolutePath());
        this.project.addCompileSourceRoot((new File(projectDir, "/grails-app/controllers")).getAbsolutePath());
        this.project.addCompileSourceRoot((new File(projectDir, "/grails-app/domain")).getAbsolutePath());
        this.project.addCompileSourceRoot((new File(projectDir, "/grails-app/services")).getAbsolutePath());
        this.project.addCompileSourceRoot((new File(projectDir, "/grails-app/taglib")).getAbsolutePath());
        this.project.addCompileSourceRoot((new File(projectDir, "/grails-app/utils")).getAbsolutePath());
        this.project.addCompileSourceRoot((new File(projectDir, "/src/groovy")).getAbsolutePath());
        this.project.addCompileSourceRoot((new File(projectDir, "src/java")).getAbsolutePath());

        // Add tests directories
        this.project.addTestCompileSourceRoot((new File(projectDir, "test/unit")).getAbsolutePath());
        this.project.addTestCompileSourceRoot((new File(projectDir, "test/integration")).getAbsolutePath());

	}

}
