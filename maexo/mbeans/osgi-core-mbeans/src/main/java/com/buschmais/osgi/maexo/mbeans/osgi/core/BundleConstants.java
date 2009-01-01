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
package com.buschmais.osgi.maexo.mbeans.osgi.core;

import javax.management.openmbean.ArrayType;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.OpenMBeanParameterInfo;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularType;

public class BundleConstants {

	/**
	 * MBean description
	 */
	public static final String MBEAN_DESCRIPTION = "Bundle MBean";

	public static final String OBJECTNAME_FORMAT = "com.buschmais.osgi.maexo:type=Bundle,name=%s,version=%s,";

	/**
	 * attribute: id
	 */
	public static final OpenMBeanAttributeInfoSupport ID = new OpenMBeanAttributeInfoSupport(
			"bundleId", "The unique identifier of this bundle.",
			SimpleType.INTEGER, true, false, false);

	/**
	 * attribute: state
	 */
	public static final OpenMBeanAttributeInfoSupport STATE = new OpenMBeanAttributeInfoSupport(
			"state",
			"An element of UNINSTALLED,INSTALLED,RESOLVED,STARTING,STOPPING,ACTIVE.",
			SimpleType.INTEGER, true, false, false);

	/**
	 * attribute: stateName
	 */
	public static final OpenMBeanAttributeInfoSupport STATENAME = new OpenMBeanAttributeInfoSupport(
			"stateAsName",
			"An element of UNINSTALLED,INSTALLED,RESOLVED,STARTING,STOPPING,ACTIVE.",
			SimpleType.STRING, true, false, false);

	/**
	 * attribute: headers
	 */
	public static final String[] ITEM_NAMES = new String[] { "name", "value" };
	public static final CompositeType HEADER_TYPE = createCompositeType(
			"headerEntry", "bundle header entry", ITEM_NAMES, ITEM_NAMES,
			new OpenType[] { SimpleType.STRING, SimpleType.STRING });
	public static final TabularType HEADERS_TYPE = createTabularType("headers",
			"TabularType representing a bundle's Manifest headers and values.",
			HEADER_TYPE, new String[] { "name" });
	public static final OpenMBeanAttributeInfoSupport HEADER = new OpenMBeanAttributeInfoSupport(
			"headers",
			"A TabularData object containing this bundle's Manifest headers and values.",
			HEADERS_TYPE, true, false, false);

	/**
	 * attribute: lastModified
	 */
	public static final OpenMBeanAttributeInfoSupport LASTMODIFIED = new OpenMBeanAttributeInfoSupport(
			"lastModified", "The time when this bundle was last modified.",
			SimpleType.LONG, true, false, false);

	/**
	 * attribute: lastModifiedAsDate
	 */
	public static final OpenMBeanAttributeInfoSupport LASTMODIFIEDASDATE = new OpenMBeanAttributeInfoSupport(
			"lastModifiedAsDate",
			"The time when this bundle was last modified.", SimpleType.DATE,
			true, false, false);

	/**
	 * attribute: location
	 */
	public static final OpenMBeanAttributeInfoSupport LOCATION = new OpenMBeanAttributeInfoSupport(
			"location",
			"The string representation of this bundle's location identifier.",
			SimpleType.STRING, true, false, false);

	/**
	 * attribute: registeredServices
	 */
	public static final OpenMBeanAttributeInfoSupport REGISTEREDSERVICES = new OpenMBeanAttributeInfoSupport(
			"registeredServices",
			"This bundle's ObjectName list for all services it has registered or null if this bundle has no registered services.",
			createArrayType(1, SimpleType.OBJECTNAME), true, false, false);

	/**
	 * attribute: servicesInUse
	 */
	public static final OpenMBeanAttributeInfoSupport SERVICESINUSE = new OpenMBeanAttributeInfoSupport(
			"servicesInUse",
			"This bundle's ObjectName list for all services it is using or returns null if this bundle is not using any services.",
			createArrayType(1, SimpleType.OBJECTNAME), true, false, false);

	/**
	 * operation: start
	 */
	public static final OpenMBeanOperationInfoSupport START = new OpenMBeanOperationInfoSupport(
			"start", "Start the bundle",
			new OpenMBeanParameterInfoSupport[] {}, SimpleType.VOID,
			OpenMBeanOperationInfoSupport.ACTION_INFO);

	/**
	 * operation: stop
	 */
	public static final OpenMBeanOperationInfoSupport STOP = new OpenMBeanOperationInfoSupport(
			"stop", "Stop the bundle", new OpenMBeanParameterInfoSupport[] {},
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION_INFO);

	/**
	 * operation: update
	 */
	public static final OpenMBeanOperationInfoSupport UPDATE = new OpenMBeanOperationInfoSupport(
			"update", "Update the bundle",
			new OpenMBeanParameterInfoSupport[] {}, SimpleType.VOID,
			OpenMBeanOperationInfoSupport.ACTION_INFO);

	/**
	 * operation: update from url
	 */
	public static final OpenMBeanOperationInfoSupport UPDATEFROMURL = new OpenMBeanOperationInfoSupport(
			"update", "Update the bundle from the given url",
			new OpenMBeanParameterInfo[] { new OpenMBeanParameterInfoSupport(
					"url", "URL", SimpleType.STRING) }, SimpleType.VOID,
			OpenMBeanOperationInfoSupport.ACTION_INFO);

	/**
	 * operation: uninstall
	 */
	// FIXME@DM there is no uninstall operation defined in
	// com.buschmais.osgi.maexo.mbeans.osgi.core.Bundle
	public static final String OPERATION_UNINSTALL_NAME = "uninstall";
	public static final String OPERATION_UNINSTALL_DESCRIPTION = "Uninstall the bundle";

	private static CompositeType createCompositeType(String typeName,
			String description, String[] itemNames, String[] itemDescriptions,
			OpenType[] itemTypes) {
		try {
			return new CompositeType(typeName, description, itemNames,
					itemDescriptions, itemTypes);
		} catch (OpenDataException e) {
			// let the exception slip as otherwise the user will see a
			// ClassDefNotFoundException
		}
		return null;
	}

	private static TabularType createTabularType(String typeName,
			String description, CompositeType rowType, String[] indexNames) {
		try {
			return new TabularType(typeName, description, rowType, indexNames);
		} catch (OpenDataException e) {
			// let the exception slip as otherwise the user will see a
			// ClassDefNotFoundException
		}
		return null;
	}

	private static ArrayType createArrayType(int dimension, OpenType elementType) {
		try {
			return new ArrayType(dimension, elementType);
		} catch (OpenDataException e) {
			// let the exception slip as otherwise the user will see a
			// ClassDefNotFoundException
		}
		return null;
	}
}
