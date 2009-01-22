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

import com.buschmais.maexo.framework.commons.mbean.dynamic.OpenTypeFactory;


/**
 * Class holding all constants for FrameworkMBeans.
 */
public final class FrameworkConstants {

	/**
	 * Private Constructor.
	 */
	private FrameworkConstants() {

	}

	/** MBean description. */
	public static final String MBEAN_DESCRIPTION = "Framework MBean";

	/** MBean object name format. */
	public static final String OBJECTNAME_FORMAT = "com.buschmais.maexo:type=Framework";

	/** MBean attribute info for {@link FrameworkMBean#getBootDelegation()}. */
	public static final OpenMBeanAttributeInfoSupport BOOTDELEGATION = new OpenMBeanAttributeInfoSupport(
			"bootDelegation",
			"The value of the framework's boot delegation property.",
			SimpleType.STRING, true, false, false);

	/**
	 * MBean attribute info for {@link FrameworkMBean#getExecutionEnvironment()}
	 * .
	 */
	public static final OpenMBeanAttributeInfoSupport EXECUTIONENVIRONMENT = new OpenMBeanAttributeInfoSupport(
			"executionEnvironment",
			"The value of the framework's execution environment property.",
			SimpleType.STRING, true, false, false);
	
	/** MBean attribute info for {@link FrameworkMBean#getLanguage()}. */
	public static final OpenMBeanAttributeInfoSupport LANGUAGE = new OpenMBeanAttributeInfoSupport(
			"language", "The value of the framework's language property.",
			SimpleType.STRING, true, false, false);
	
	/** MBean attribute info for {@link FrameworkMBean#getOsName()}. */
	public static final OpenMBeanAttributeInfoSupport OSNAME = new OpenMBeanAttributeInfoSupport(
			"osName", "The value of the framework's OS name property.",
			SimpleType.STRING, true, false, false);
	
	/** MBean attribute info for {@link FrameworkMBean#getOsVersion()}. */
	public static final OpenMBeanAttributeInfoSupport OSVERSION = new OpenMBeanAttributeInfoSupport(
			"osVersion", "The value of the framework's OS version property.",
			SimpleType.STRING, true, false, false);
	
	/** MBean attribute info for {@link FrameworkMBean#getProcessor()}. */
	public static final OpenMBeanAttributeInfoSupport PROCESSOR = new OpenMBeanAttributeInfoSupport(
			"processor", "The value of the framework's processor property.",
			SimpleType.STRING, true, false, false);
	
	/** MBean attribute info for {@link FrameworkMBean#getSystemPackages()}. */
	public static final OpenMBeanAttributeInfoSupport SYSTEMPACKAGES = new OpenMBeanAttributeInfoSupport(
			"systemPackages",
			"The value of the framework's system packages property.",
			SimpleType.STRING, true, false, false);
	
	/** MBean attribute info for {@link FrameworkMBean#getVendor()}. */
	public static final OpenMBeanAttributeInfoSupport VENDOR = new OpenMBeanAttributeInfoSupport(
			"vendor", "The value of the framework's vendor property.",
			SimpleType.STRING, true, false, false);
	
	/** MBean attribute info for {@link FrameworkMBean#getVersion()}. */
	public static final OpenMBeanAttributeInfoSupport VERSION = new OpenMBeanAttributeInfoSupport(
			"version", "The value of the framework's version property.",
			SimpleType.STRING, true, false, false);
	
	/** MBean attribute info for {@link FrameworkMBean#getBundles()}. */
	public static final OpenMBeanAttributeInfoSupport BUNDLES = new OpenMBeanAttributeInfoSupport(
			"bundles", "The array of ObjectName of all installed bundles",
			OpenTypeFactory.createArrayType(1, SimpleType.OBJECTNAME), true,
			false, false);
	
	/** MBean attribute info for {@link FrameworkMBean#getServices()}. */
	public static final OpenMBeanAttributeInfoSupport SERVICES = new OpenMBeanAttributeInfoSupport(
			"services", "The array of ObjectName of all registered services.",
			OpenTypeFactory.createArrayType(1, SimpleType.OBJECTNAME), true,
			false, false);

	/**
	 * MBean operation info for operation
	 * {@link FrameworkMBean#getServices(String, String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETSERVICES_BY_OBJECTCLASS = new OpenMBeanOperationInfoSupport(
			"getServices",
			"Returns an array of ObjectName representing services which match the specified criteria.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"objectClass",
							"The class name with which the service was registered or null for all services.",
							SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("filter",
							"The filter criteria.", SimpleType.STRING) },
							OpenTypeFactory.createArrayType(1, SimpleType.OBJECTNAME),
			OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation
	 * {@link FrameworkMBean#installBundle(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport INSTALLBUNDLE_BY_LOCATION = new OpenMBeanOperationInfoSupport(
			"installBundle",
			"Installs a bundle from the specified location string.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"location",
					"The location identifier of the bundle to install.",
					SimpleType.STRING) }, SimpleType.OBJECTNAME,
			OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation
	 * {@link FrameworkMBean#installBundle(String, byte[])}.
	 */
	public static final OpenMBeanOperationInfoSupport INSTALLBUNDLE_BY_BYTEARRAY = new OpenMBeanOperationInfoSupport(
			"installBundle",
			"Installs a bundle from the specified input byte array.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport(
							"location",
							"The location identifier of the bundle to install.",
							SimpleType.STRING),
					new OpenMBeanParameterInfoSupport(
							"input",
							"The byte array from which this bundle will be read.",
							OpenTypeFactory.createArrayType(1, SimpleType.BYTE)) },
			SimpleType.OBJECTNAME, OpenMBeanOperationInfoSupport.INFO);
	

}
