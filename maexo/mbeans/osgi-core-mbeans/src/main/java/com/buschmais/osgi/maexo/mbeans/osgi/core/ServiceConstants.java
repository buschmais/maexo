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

public class ServiceConstants {

	/**
	 * object name properties: id, type and name
	 */
	public static final String OBJECTNAME_ID_PROPERTY = "id";
	public static final String OBJECTNAME_TYPE_PROPERTY = "type";
	public static final String OBJECTNAME_NAME_PROPERTY = "name";
	public static final String OBJECTNAME_PID_PROPERTY = "pid";

	/**
	 * definition of the service property type;
	 */
	public static final String OBJECTNAME_TYPE_VALUE = "service";

	/**
	 * MBean description
	 */
	public static final String MBEAN_DESCRIPTION = "Service MBean";

	/**
	 * attribute: bundle
	 */
	public static final String ATTRIBUTE_BUNDLE_NAME = "bundle";
	public static final String ATTRIBUTE_BUNDLE_DESCRIPTION = "The bundle which registered this service";

	/**
	 * attribute: id
	 */
	public static final String ATTRIBUTE_ID_NAME = "id";
	public static final String ATTRIBUTE_ID_DESCRIPTION = "The id of this service";

	/**
	 * attribute: description
	 */
	public static final String ATTRIBUTE_DESCRIPTION_NAME = "description";
	public static final String ATTRIBUTE_DESCRIPTION_DESCRIPTION = "The description of this service";

	/**
	 * attribute: objectClass
	 */
	public static final String ATTRIBUTE_OBJECTCLASS_NAME = "objectClass";
	public static final String ATTRIBUTE_OBJECTCLASS_DESCRIPTION = "The object class(es) this services implements";

	/**
	 * attribute: pid
	 */
	public static final String ATTRIBUTE_PID_NAME = "pid";
	public static final String ATTRIBUTE_PID_DESCRIPTION = "The persistent id of this service";

	/**
	 * attribute: properties
	 */
	public static final String ATTRIBUTE_PROPERTIES_NAME = "properties";
	public static final String ATTRIBUTE_PROPERTIES_DESCRIPTION = "The properties of this service";
	public static final String COMPOSITETYPE_PROPERTIES_ENTRY = "propertyEntry";
	public static final String COMPOSITETYPE_PROPERTIES_ENTRY_DESCRIPTION = "A service property entry";
	public static final String COMPOSITETYPE_PROPERTIES_NAME = "name";
	public static final String COMPOSITETYPE_PROPERTIES_VALUE = "value";
	public static final String TABULARTYPE_PROPERTIES_NAME = "properties";
	public static final String TABULARTYPE_PROPERTIES_DESCRIPTION = "The service properties";

	/**
	 * attribute:ranking
	 */
	public static final String ATTRIBUTE_RANKING_NAME = "ranking";
	public static final String ATTRIBUTE_RANKING_DESCRIPTION = "The ranking of this service";

	/**
	 * attribute: usingBundles
	 */
	public static final String ATTRIBUTE_USINGBUNDLES_NAME = "usingBundles";
	public static final String ATTRIBUTE_USINGBUNDLES_DESCRIPTION = "The bundle which are currrently using this service";

	/**
	 * attribute: vendor
	 */
	public static final String ATTRIBUTE_VENDOR_NAME = "vendor";
	public static final String ATTRIBUTE_VENDOR_DESCRIPTION = "The vendor of this service";

}
