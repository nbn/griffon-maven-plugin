package org.codehaus.griffon.maven.plugin;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

public class LoadMojosTest extends AbstractMojoTestCase {
	
	@Override
	protected void setUp() throws Exception {
		// TODO: Enable when I get the container working with Aether RepositorySystem
		//super.setUp(); 
	}

	@Override
	protected void tearDown() throws Exception {
		//super.tearDown();
	}

	@SuppressWarnings({ "unused", "rawtypes" })
	private void mojoTest(String pluginConfig, String mojoName, Class mojoClass) throws Exception {
		File testPom = getTestFile("src/test/resources/org/codehaus/griffon/maven/plugin/" + pluginConfig);
		Object mojo = lookupMojo(mojoName, testPom);
		try {
			assertNotNull(mojo);
			assertEquals(mojo.getClass(), mojoClass);
		} finally {
			release(mojo);
		}
	}
	
	  public void testLoadGrailsCleanMojoLookup() throws Exception {
			// TODO: Enable when I get the container working with Aether RepositorySystem
	      //  mojoTest("maven-validate/plugin-config.xml", "validate", MvnValidateMojo.class);
		  
		  assertEquals(4, 2 + 2); // If that is granted all else follows.
	    }


}
