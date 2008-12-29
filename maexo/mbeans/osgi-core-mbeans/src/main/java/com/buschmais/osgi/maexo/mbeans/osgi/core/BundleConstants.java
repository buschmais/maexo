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

public class BundleConstants {

	/**
	 * object name properties: type and name
	 */
	public static final String OBJECTNAME_TYPE_PROPERTY = "type";
	public static final String OBJECTNAME_NAME_PROPERTY = "name";
	public static final String OBJECTNAME_VERSION_PROPERTY = "version";

	/**
	 * value of the type property
	 */
	public static final String OBJECTNAME_TYPE_VALUE = "Bundle";

	/**
	 * MBean description
	 */
	public static final String MBEAN_DESCRIPTION = "Bundle MBean";

	/**
	 * attribute: id
	 */
	public static final String ATTRIBUTE_ID_NAME = "bundleId";
	public static final String ATTRIBUTE_ID_DESCRIPTION = "The unique identifier of this bundle.";

	/**
	 * attribute: state
	 */
	public static final String ATTRIBUTE_STATE_NAME = "state";
	public static final String ATTRIBUTE_STATE_DESCRIPTION = "An element of UNINSTALLED,INSTALLED, RESOLVED,STARTING, STOPPING,ACTIVE.";

	/**
	 * attribute: stateName
	 */
	public static final String ATTRIBUTE_STATENAME_NAME = "stateAsName";
	public static final String ATTRIBUTE_STATENAME_DESCRIPTION = "An element of UNINSTALLED,INSTALLED, RESOLVED,STARTING, STOPPING,ACTIVE.";

	/**
	 * attribute: headers
	 */
	public static final String TABULARTYPE_HEADERS_NAME = "headers";
	public static final String TABULARTYPE_HEADERS_DESCRIPTION = "TabularType representing a bundle's Manifest headers and values.";
	public static final String COMPOSITETYPE_HEADER_ENTRY = "headerEntry";
	public static final String COMPOSITETYPE_HEADER_ENTRY_DESCRIPTION = "bundle header entry";
	public static final String COMPOSITETYPE_HEADER_NAME = "name";
	public static final String COMPOSITETYPE_HEADER_VALUE = "value";
	public static final String ATTRIBUTE_HEADERS_NAME = "headers";
	public static final String ATTRIBUTE_HEADERS_DESCRIPTION = "A TabularData object containing this bundle's Manifest headers and values.";

	/**
	 * attribute: lastModified
	 */
	public static final String ATTRIBUTE_LASTMODIFIED_NAME = "lastModified";
	public static final String ATTRIBUTE_LASTMODIFIED_DESCRIPTION = "The time when this bundle was last modified.";

	/**
	 * attribute: lastModifiedAsDate
	 */
	public static final String ATTRIBUTE_LASTMODIFIEDASDATE_NAME = "lastModifiedAsDate";
	public static final String ATTRIBUTE_LASTMODIFIEDASDATE_DESCRIPTION = "The time when this bundle was last modified.";

	/**
	 * attribute: location
	 */
	public static final String ATTRIBUTE_LOCATION_NAME = "location";
	public static final String ATTRIBUTE_LOCATION_DESCRIPTION = "The string representation of this bundle's location identifier.";

	/**
	 * attribute: registeredServices
	 */
	public static final String ATTRIBUTE_REGISTEREDSERVICES_NAME = "registeredServices";
	public static final String ATTRIBUTE_REGISTEREDSERVICES_DESCRIPTION = "This bundle's ObjectName list for all services it has registered or null if this bundle has no registered services.";

	/**
	 * attribute: servicesInUse
	 */
	public static final String ATTRIBUTE_SERVICESINUSE_NAME = "servicesInUse";
	public static final String ATTRIBUTE_SERVICESINUSE_DESCRIPTION = "This bundle's ObjectName list for all services it is using or returns null if this bundle is not using any services.";

	/**
	 * operation: start
	 */
	public static final String OPERATION_START_NAME = "start";
	public static final String OPERATION_START_DESCRIPTION = "Start the bundle";

	/**
	 * operation: stop
	 */
	public static final String OPERATION_STOP_NAME = "stop";
	public static final String OPERATION_STOP_DESCRIPTION = "Stop the bundle";

	/**
	 * operation: update
	 */
	public static final String OPERATION_UPDATE_NAME = "update";
	public static final String OPERATION_UPDATE_DESCRIPTION = "Update the bundle";

	/**
	 * operation: update from url
	 */
	public static final String OPERATION_UPDATEFROMURL_NAME = "update";
	public static final String OPERATION_UPDATEFROMURL_DESCRIPTION = "Update the bundle from the given url";
	public static final String OPERATION_UPDATEFROMURL_URL_PARAMETER = "url";
	public static final String OPERATION_UPDATEFROMURL_URL_DESCRIPTION = "url";

	/**
	 * operation: uninstall
	 */
	public static final String OPERATION_UNINSTALL_NAME = "uninstall";
	public static final String OPERATION_UNINSTALL_DESCRIPTION = "Uninstall the bundle";
}
