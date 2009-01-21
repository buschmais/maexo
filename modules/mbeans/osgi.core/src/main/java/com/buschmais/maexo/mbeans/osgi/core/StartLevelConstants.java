/*
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
package com.buschmais.maexo.mbeans.osgi.core;

import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;
import javax.management.openmbean.SimpleType;

/**
 * Class holding all constants for StartLevelMBeans.
 */
public final class StartLevelConstants {

	/**
	 * Private Constructor.
	 */
	private StartLevelConstants() {

	}

	/** MBean object name format. */
	public static final String OBJECTNAME_FORMAT = "com.buschmais.maexo:type=StartLevel,id=%s";

	/** MBean description. */
	public static final String MBEAN_DESCRIPTION = "StartLevel MBean";

	/** MBean attribute info for {@link StartLevelMBean#getStartLevel()}. */
	public static final OpenMBeanAttributeInfoSupport STARTLEVEL = new OpenMBeanAttributeInfoSupport(
			"startLevel", "The active start level value of the Framework.",
			SimpleType.INTEGER, true, true, false);

	/**
	 * MBean attribute info for
	 * {@link StartLevelMBean#getInitialBundleStartLevel()}.
	 */
	public static final OpenMBeanAttributeInfoSupport INITIALBUNDLE_STARTLEVEL = new OpenMBeanAttributeInfoSupport(
			"initialBundleStartLevel",
			"The initial start level value for Bundles.", SimpleType.INTEGER,
			true, true, false);

	/**
	 * MBean operation info for operation Operation
	 * {@link StartLevelMBean#getBundleStartLevel(javax.management.ObjectName)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETBUNDLESTARTLEVEL_BY_OBJECTNAME = new OpenMBeanOperationInfoSupport(
			"getBundleStartLevel",
			"Return the assigned start level value for the specified Bundle.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"objectName", "The object name of the bundle",
					SimpleType.OBJECTNAME) }, SimpleType.INTEGER,
			OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation Operation
	 * {@link StartLevelMBean#getBundleStartLevel(Long)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETBUNDLESTARTLEVEL_BY_ID = new OpenMBeanOperationInfoSupport(
			"getBundleStartLevel",
			"Return the assigned start level value for the specified Bundle.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"id", "The id of the bundle", SimpleType.LONG) },
			SimpleType.INTEGER, OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation Operation
	 * {@link StartLevelMBean#setBundleStartLevel(javax.management.ObjectName, Integer)}
	 * .
	 */
	public static final OpenMBeanOperationInfoSupport SETBUNDLESTARTLEVEL_BY_OBJECTNAME = new OpenMBeanOperationInfoSupport(
			"setBundleStartLevel",
			"Assign a start level value to the specified Bundle.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("objectName",
							"The object name of the bundle",
							SimpleType.OBJECTNAME),
					new OpenMBeanParameterInfoSupport("startLevel",
							"The new start level for the specified Bundle.",
							SimpleType.INTEGER) }, SimpleType.VOID,
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation Operation
	 * {@link StartLevelMBean#setBundleStartLevel(Long, Integer)}.
	 */
	public static final OpenMBeanOperationInfoSupport SETBUNDLESTARTLEVEL_BY_ID = new OpenMBeanOperationInfoSupport(
			"setBundleStartLevel",
			"Assign a start level value to the specified Bundle.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("id",
							"The id of the bundle", SimpleType.LONG),
					new OpenMBeanParameterInfoSupport("startLevel",
							"The new start level for the specified Bundle.",
							SimpleType.INTEGER) }, SimpleType.VOID,
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation Operation
	 * {@link StartLevelMBean#isBundlePersistentlyStarted(javax.management.ObjectName)}
	 * .
	 */
	public static final OpenMBeanOperationInfoSupport ISBUNDLEPERSISTENTLYSTARTED_BY_OBJECTNAME = new OpenMBeanOperationInfoSupport(
			"isBundlePersistentlyStarted",
			"Return the persistent state of the specified bundle.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"objectName", "The object name of the bundle",
					SimpleType.OBJECTNAME) }, SimpleType.BOOLEAN,
			OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation Operation
	 * {@link StartLevelMBean#isBundlePersistentlyStarted(Long)}.
	 */
	public static final OpenMBeanOperationInfoSupport ISBUNDLEPERSISTENTLYSTARTED_BY_ID = new OpenMBeanOperationInfoSupport(
			"isBundlePersistentlyStarted",
			"Return the persistent state of the specified bundle.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"id", "The id of the bundle", SimpleType.LONG) },
			SimpleType.BOOLEAN, OpenMBeanOperationInfoSupport.INFO);
}
