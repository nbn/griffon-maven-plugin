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
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;

public interface GriffonServices {
    /**
     * Sets the basedir that all commands are executed from
     *
     * @since 1.3.0
     */
    void setBasedir(File basedir);
    /**
     * Read a griffon project descriptor (application.properties) from a file.
     *
     * @since 1.3.0
     * @return A Griffon Project Descriptor
     * @throws Exception
     *                 if a problem occurs
     */
    GriffonProject readProjectDescriptor() throws MojoExecutionException;

    /**
     * Write a grails project descriptor (application.properties) in a file.
     *
     * @since 1.3.0
     * @param projectDir
     *                The Griffon project directory.
     * @param griffonProjectDescriptor
     *                The descriptor to write.
     * @throws FileNotFoundException
     *                 If the project directory isn't found.
     * @throws IOException
     *                 If a problem occurs during the write.
     */
    void writeProjectDescriptor(File projectDir, GriffonProject griffonProjectDescriptor)
        throws MojoExecutionException;

}
