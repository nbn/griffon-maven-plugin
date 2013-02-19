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

import org.codehaus.griffon.launcher.GriffonLauncher;

/**
 * Holds a singleton instance for the duration of a running maven execution.
 * This prevents loading multiple instances in different classloaders which
 * eventually would lead to PermGen error.
 * (Actually also speeds up the execution some)
 * 
 * Note, that it is not threadsafe in any way..
 * 
 * @author nielsbechnielsen
 *
 */
public class GriffonLauncherHolder {
	
	private static GriffonLauncher _instance;
	
	public static GriffonLauncher get() {
		return _instance;
	}
	
	public static void set(GriffonLauncher launcher) {
		_instance=launcher;
	}

}
