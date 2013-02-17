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
package org.codehaus.griffon.maven.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.IOUtil;

/**
 * @author Created for Grails by <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @author Ported to Griffon by <a href="mailto:nielsbechnielsen@gmail.com">Niels Bech Nielsen</a>
 * @since 1.3.0
 */
@Component(role=GriffonServices.class)
public class DefaultGriffonServices extends AbstractLogEnabled implements GriffonServices {

    private File _basedir;

  private File getBasedir() {
      if (_basedir != null) {
          return _basedir;
      }

      throw new RuntimeException("The basedir has to be set before any of the service methods are invoked.");
  }
  
  public void setBasedir(final File basedir) {
      this._basedir = basedir;
  }




	@Override
	public GriffonProject readProjectDescriptor() throws MojoExecutionException {
        // Load existing Griffon properties
        FileInputStream fis = null;
        try {
            final Properties properties = new Properties();
            fis = new FileInputStream(new File(getBasedir(), "application.properties"));
            properties.load(fis);

            final GriffonProject griffonProject = new GriffonProject();
            griffonProject.setAppGriffonVersion(properties.getProperty("app.griffon.version"));
            griffonProject.setAppName(properties.getProperty("app.name"));
            griffonProject.setAppVersion(properties.getProperty("app.version"));

            return griffonProject;
        } catch (final IOException e) {
            throw new MojoExecutionException("Unable to read griffon project descriptor.", e);
        } finally {
            IOUtil.close(fis);
        }
	}

	@Override
	public void writeProjectDescriptor(File projectDir,
			GriffonProject griffonProjectDescriptor)
			throws MojoExecutionException {
        final String description = "Griffon Descriptor updated by griffon-maven-plugin on " + new Date();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(projectDir, "application.properties"));
            final Properties properties = new Properties();
            properties.setProperty("app.griffon.version", griffonProjectDescriptor.getAppGriffonVersion());
            properties.setProperty("app.name", griffonProjectDescriptor.getAppName());
            properties.setProperty("app.version", griffonProjectDescriptor.getAppVersion());
            properties.store(fos, description);
        } catch (final IOException e) {
            throw new MojoExecutionException("Unable to write griffon project descriptor.", e);
        } finally {
            IOUtil.close(fos);
        }
	}

}
