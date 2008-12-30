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
package com.buschmais.osgi.maexo.mbeans.osgi.core.impl.objectname;

import java.util.Dictionary;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.management.ObjectName;

import org.osgi.framework.Constants;

import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameFactory;
import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameFactoryException;
import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameHelper;
import com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminConstants;
import com.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelConstants;

/**
 * Object name factory implementation for the package admin service.
 */
public class PackageAdminObjectNameFactory implements ObjectNameFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameFactory
	 * #getObjectName(java.lang.Object, java.util.Dictionary)
	 */
	public ObjectName getObjectName(Object resource,
			Dictionary<String, Object> properties) {
		if (properties == null) {
			throw new ObjectNameFactoryException("Parameter properties must not be null");
		}
		// create object name properties as linked hash map to maintain
		// insertion order
		Map<String, Object> objectNameProperties = new LinkedHashMap<String, Object>();
		// type
		objectNameProperties.put(
				PackageAdminConstants.OBJECTNAME_TYPE_PROPERTY,
				PackageAdminConstants.OBJECTNAME_TYPE_VALUE);
		// id
		Long id = (Long) properties.get(Constants.SERVICE_ID);
		objectNameProperties
				.put(StartLevelConstants.OBJECTNAME_ID_PROPERTY, id);
		return ObjectNameHelper.assembleObjectName(objectNameProperties);
	}
}
