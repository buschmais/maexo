/**
 * Copyright 2008 buschmais GbR
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package com.buschmais.osgi.maexo.mbeans.osgi.core;

import org.osgi.service.startlevel.StartLevel;

public class StartLevelConstants {

	/**
	 * object name properties: type
	 */
	public static final String OBJECTNAME_TYPE_PROPERTY = "type";

	/**
	 * value of the type property
	 */
	public static final String OBJECTNAME_TYPE_VALUE = StartLevel.class
			.getName();

	/**
	 * MBean description
	 */
	public static final String MBEAN_DESCRIPTION = "StartLevel MBean";

	/**
	 * attribute: initialBundleStartLevel
	 */
	public static final String ATTRIBUTE_INITIALBUNDLESTARTLEVEL_NAME = "initialBundleStartLevel";
	public static final String ATTRIBUTE_INITIALBUNDLESTARTLEVEL_DESCRIPTION = "The initial start level value for Bundles.";

	/**
	 * attribute: startLevel
	 */
	public static final String ATTRIBUTE_STARTLEVEL_NAME = "startLevel";
	public static final String ATTRIBUTE_STARTLEVEL_DESCRIPTION = "The active start level value of the Framework.";

	/**
	 * getBundleStartLevel isBundlePersistentlyStarted setBundleStartLevel
	 * setInitialBundleStartLevel
	 */

	/**
	 * operation: getBundleStartLevel
	 */
	public static final String OPERATION_GETBUNDLESTARTLEVEL_NAME = "getBundleStartLevel";
	public static final String OPERATION_GETBUNDLESTARTLEVEL_DESCRIPTION = "Return the assigned start level value for the specified Bundle.";
	public static final String OPERATION_GETBUNDLESTARTLEVEL_OBJECTNAME_PARAMETER = "objectName";
	public static final String OPERATION_GETBUNDLESTARTLEVEL_OBJECTNAME_DESCRIPTION = "The object name of the bundle";
	public static final String OPERATION_GETBUNDLESTARTLEVEL_ID_PARAMETER = "id";
	public static final String OPERATION_GETBUNDLESTARTLEVEL_ID_DESCRIPTION = "The id of the bundle";

	/**
	 * operation: setBundleStartLevel
	 */
	public static final String OPERATION_SETBUNDLESTARTLEVEL_NAME = "setBundleStartLevel";
	public static final String OPERATION_SETBUNDLESTARTLEVEL_DESCRIPTION = "Assign a start level value to the specified Bundle.";
	public static final String OPERATION_SETBUNDLESTARTLEVEL_OBJECTNAME_PARAMETER = "objectName";
	public static final String OPERATION_SETBUNDLESTARTLEVEL_OBJECTNAME_DESCRIPTION = "The object name of the bundle";
	public static final String OPERATION_SETBUNDLESTARTLEVEL_ID_PARAMETER = "id";
	public static final String OPERATION_SETBUNDLESTARTLEVEL_ID_DESCRIPTION = "The id of the bundle";
	public static final String OPERATION_SETBUNDLESTARTLEVEL_STARTLEVEL_PARAMETER = "startLevel";
	public static final String OPERATION_SETBUNDLESTARTLEVEL_STARTLEVEL_DESCRIPTION = "The new start level for the specified Bundle.";

	/**
	 * operation: isBundlePersistentlyStarted
	 */
	public static final String OPERATION_ISBUNDLEPERSISTENTLYSTARTED_NAME = "isBundlePersistentlyStarted";
	public static final String OPERATION_ISBUNDLEPERSISTENTLYSTARTED_DESCRIPTION = "Return the persistent state of the specified bundle.";
	public static final String OPERATION_ISBUNDLEPERSISTENTLYSTARTED_OBJECTNAME_PARAMETER = "objectName";
	public static final String OPERATION_ISBUNDLEPERSISTENTLYSTARTED_OBJECTNAME_DESCRIPTION = "The object name of the bundle";
	public static final String OPERATION_ISBUNDLEPERSISTENTLYSTARTED_ID_PARAMETER = "id";
	public static final String OPERATION_ISBUNDLEPERSISTENTLYSTARTED_ID_DESCRIPTION = "The id of the bundle";
}
