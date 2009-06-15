/*
 * Copyright 2009 buschmais GbR
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
package com.buschmais.maexo.mbeans.osgi.compendium;

import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;
import javax.management.openmbean.SimpleType;

import org.osgi.service.cm.ConfigurationAdmin;

import com.buschmais.maexo.framework.commons.mbean.dynamic.OpenTypeFactory;

/**
 * Class holding constants for {@link ConfigurationAdminMBean}s.
 */
public class ConfigurationAdminMBeanConstants {

	/**
	 * Constructor.
	 */
	private ConfigurationAdminMBeanConstants() {
	}

	/** MBean description. */
	public static final String MBEAN_DESCRIPTION = "Configuration Admin MBean";

	/** MBean object name format. */
	public static final String OBJECTNAME_FORMAT = "com.buschmais.maexo:type=ConfigurationAdmin,id=%s";

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationAdmin#createFactoryConfiguration(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport CREATEFACTORYCONFIGURATION_BY_FACTORYPID = new OpenMBeanOperationInfoSupport(
			"createFactoryConfiguration",
			"Create a new factory Configuration object with a new PID.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"factoryPid", "PID of factory (not null).",
					SimpleType.STRING) }, SimpleType.OBJECTNAME,
			OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationAdmin#createFactoryConfiguration(String, String)}.
	 */
	public static final OpenMBeanOperationInfoSupport CREATEFACTORYCONFIGURATION_BY_FACTORYPID_AND_LOCATION = new OpenMBeanOperationInfoSupport(
			"createFactoryConfiguration",
			"Create a new factory Configuration object with a new PID.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("factoryPid",
							"PID of factory (not null).", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("location",
							"A bundle location string, or null.",
							SimpleType.STRING) }, SimpleType.OBJECTNAME,
			OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationAdmin#createFactoryConfiguration(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETCONFIGURATION_BY_PID = new OpenMBeanOperationInfoSupport(
			"getConfiguration",
			"Get an existing or new Configuration object from the persistent store.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"pid", "Persistent identifier.", SimpleType.STRING) },
			SimpleType.OBJECTNAME, OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationAdmin#createFactoryConfiguration(String, String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETCONFIGURATION_BY_PID_AND_LOCATION = new OpenMBeanOperationInfoSupport(
			"getConfiguration",
			"Get an existing Configuration object from the persistent store, or create a new Configuration object.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("pid",
							"Persistent identifier.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("location",
							"A bundle location string, or null.",
							SimpleType.STRING) }, SimpleType.OBJECTNAME,
			OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationAdmin#listConfigurations(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport LISTCONFIGURATIONS = new OpenMBeanOperationInfoSupport(
			"listConfigurations",
			"List the current Configuration objects which match the filter.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"filter",
					"A filter string, or null to retrieve all Configuration objects.",
					SimpleType.STRING) }, OpenTypeFactory.createArrayType(1,
					SimpleType.OBJECTNAME), OpenMBeanOperationInfoSupport.INFO);
}
