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

import java.util.Arrays;
import java.util.List;

import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularType;

import com.buschmais.maexo.framework.commons.mbean.dynamic.OpenTypeFactory;

/**
 * Class holding all constants for ServiceMBeans.
 */
public final class ServiceMBeanConstants {

	/**
	 * Private Constructor.
	 */
	private ServiceMBeanConstants() {

	}

	/** MBean object name format. */
	public static final String OBJECTNAME_FORMAT = "com.buschmais.maexo:type=Service,name=%s,id=%s";

	/** MBean description. */
	public static final String MBEAN_DESCRIPTION = "Service MBean";

	/** Constant name. */
	private static final String SERVICEPROPERTY_ITEM_NAME = "name";

	/** Constant value. */
	private static final String SERVICEPROPERTY_ITEM_VALUE = "value";

	/** Service property items. */
	public static final List<String> SERVICEPROPERTY_ITEMS = Arrays
			.asList(new String[] { SERVICEPROPERTY_ITEM_NAME,
					SERVICEPROPERTY_ITEM_VALUE });

	/** MBean attribute info for {@link ServiceMBean#getBundle()}. */
	public static final OpenMBeanAttributeInfoSupport BUNDLE = new OpenMBeanAttributeInfoSupport(
			"bundle",
			"The object name of the bundle that registered the service; null if that service has already been unregistered.",
			SimpleType.OBJECTNAME, true, false, false);

	/** MBean attribute info for {@link ServiceMBean#getDescription()}. */
	public static final OpenMBeanAttributeInfoSupport DESCRIPTION = new OpenMBeanAttributeInfoSupport(
			"description",
			"The service property named \"service.description\" identifying a service's description.",
			SimpleType.STRING, true, false, false);

	/** MBean attribute info for {@link ServiceMBean#getId()}. */
	public static final OpenMBeanAttributeInfoSupport ID = new OpenMBeanAttributeInfoSupport(
			"id",
			"The service property named \"service.id\" identifying a service's registration number.",
			SimpleType.LONG, true, false, false);

	/** MBean attribute info for {@link ServiceMBean#getObjectClass()}. */
	public static final OpenMBeanAttributeInfoSupport OBJECTCLASS = new OpenMBeanAttributeInfoSupport(
			"objectClass",
			"The service property named \"objectClass\" identifying all of the class names under which a service was registered in the Framework.",
			OpenTypeFactory.createArrayType(1, SimpleType.STRING), true, false,
			false);

	/** MBean attribute info for {@link ServiceMBean#getPid()}. */
	public static final OpenMBeanAttributeInfoSupport PID = new OpenMBeanAttributeInfoSupport(
			"pid",
			"The service property named \"service.pid\" identifying a service's persistent identifier.",
			SimpleType.STRING, true, false, false);

	/** Composite type representing one properties entry. */
	public static final CompositeType PROPERTIES_ROW_TYPE = OpenTypeFactory
			.createCompositeType("propertyEntry", "A service property entry",
					SERVICEPROPERTY_ITEMS.toArray(new String[0]),
					SERVICEPROPERTY_ITEMS.toArray(new String[0]),
					new OpenType[] { SimpleType.STRING, SimpleType.STRING });

	/** Tabular type containing properties entries as composite type. */
	public static final TabularType PROPERTIES_TYPE = OpenTypeFactory
			.createTabularType("properties", "The service properties",
					ServiceMBeanConstants.PROPERTIES_ROW_TYPE,
					new String[] { "name" });

	/** MBean attribute info for {@link ServiceMBean#getProperties()}. */
	public static final OpenMBeanAttributeInfoSupport PROPERTIES = new OpenMBeanAttributeInfoSupport(
			"properties",
			"The properties of the Dictionary object of the service.",
			ServiceMBeanConstants.PROPERTIES_TYPE, true, false, false);

	/** MBean attribute info for {@link ServiceMBean#getRanking()}. */
	public static final OpenMBeanAttributeInfoSupport RANKING = new OpenMBeanAttributeInfoSupport(
			"ranking",
			"The service property named \"service.ranking\" identifying a service's ranking number.",
			SimpleType.INTEGER, true, false, false);

	/** MBean attribute info for {@link ServiceMBean#getUsingBundles()}. */
	public static final OpenMBeanAttributeInfoSupport USINGBUNDLES = new OpenMBeanAttributeInfoSupport(
			"usingBundles",
			"The bundle which are currrently using this service",
			OpenTypeFactory.createArrayType(1, SimpleType.OBJECTNAME), true,
			false, false);

	/** MBean attribute info for {@link ServiceMBean#getVendor()}. */
	public static final OpenMBeanAttributeInfoSupport VENDOR = new OpenMBeanAttributeInfoSupport(
			"vendor",
			"the service property named \"service.vendor\" identifying a service's vendor.",
			SimpleType.STRING, true, false, false);
}
