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
package com.buschmais.osgi.maexo.framework.commons.mbean.objectname;

import javax.management.ObjectName;

public interface ObjectNameFactory {

	public static final String DEFAULT_DOMAIN = "com.buschmais.osgi.maexo";

	public static final String SERVICE_PROPERTY_RESOURCEINTERFACE = "resourceInterface";

	public static final String PROPERTY_ID = "id";

	public static final String PROPERTY_NAME = "name";

	public static final String PROPERTY_TYPE = "type";

	/**
	 * Returns the object name for the given resource
	 * 
	 * @param resource
	 *            the resource
	 * @return the object name
	 */
	public ObjectName getObjectName(Object resource);

}
