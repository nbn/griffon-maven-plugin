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

import org.codehaus.plexus.PlexusTestCase;

public class GriffonServicesTest extends PlexusTestCase {

	/** Minimal descriptor to test */
	private GriffonProject griffonDescriptorTest = new GriffonProject();

	protected void setUp() throws Exception {
		super.setUp();
		
		griffonDescriptorTest.setAppGriffonVersion("1.2.0");
		griffonDescriptorTest.setAppName("a-griffon-app");
		griffonDescriptorTest.setAppVersion("1.0-SNAPSHOT");
	}
	
	public void testPomIsValid() throws Exception {
		GriffonServices griffonServices = lookup(GriffonServices.class);
		try {
			griffonServices.setBasedir(getTestFile("."));
		
			griffonServices.writeProjectDescriptor(getTestFile("."), griffonDescriptorTest);
			
			GriffonProject persistedDescriptor = griffonServices.readProjectDescriptor();
			assertEquals("1.2.0",         persistedDescriptor.getAppGriffonVersion());
			assertEquals("a-griffon-app", persistedDescriptor.getAppName());
			assertEquals("1.0-SNAPSHOT",  persistedDescriptor.getAppVersion());
		} finally {
			release(griffonServices);
		}
	}
}
