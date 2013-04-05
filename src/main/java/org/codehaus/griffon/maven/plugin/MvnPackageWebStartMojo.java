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

import org.apache.maven.plugins.annotations.Mojo;

/**
 * Package a Griffon application as a webstart file.
 *
 * @author <a href="mailto:nielsbechnielsen@gmail.com">Niels Bech Nielsen</a>
 * @since 1.3
 */
@Mojo(name="maven-package-webstart", requiresProject=true)
public class MvnPackageWebStartMojo extends MvnPackageApplicationMojo {

	
	// Zip is both the name to package and the extension for after..
	protected String getPackageType() {
		return "webstart";
	}	
	
	@Override
	protected String getPackageExtension() {
		return "zip";
	}
}
