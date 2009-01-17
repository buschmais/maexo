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

import java.util.Map;

import javax.management.ObjectName;

import org.osgi.framework.Constants;

import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameFactory;
import com.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelConstants;

/**
 * Object name factory implementation for the start level service.
 */
public final class StartLevelObjectNameFactory implements ObjectNameFactory {

	/**
	 * Returns the object name for the given resource.
	 * 
	 * @see com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameFactory
	 *      #getObjectName(java.lang.Object, java.util.Map)
	 * 
	 * @param resource
	 *            the resource
	 * @param properties
	 *            must contain a {@link Constants.SERVICE_ID} entry
	 * @exception IllegalArgumentException
	 *                if the {@link Constants.SERVICE_ID} entry is missing
	 * @return the object name for the given resource
	 */
	public ObjectName getObjectName(Object resource,
			Map<String, Object> properties) throws IllegalArgumentException {
		if (properties == null) {
			throw new IllegalArgumentException(
					"Parameter properties must not be null");
		}

		// id
		Long id = (Long) properties.get(Constants.SERVICE_ID);
		if (id == null) {
			throw new IllegalArgumentException("No service id provided");
		}
		String objectName = String.format(StartLevelConstants.OBJECTNAME_FORMAT, id);
		try {
			return new ObjectName(objectName);
		} catch (Exception e) {
			throw new IllegalArgumentException(String.format("Cannot create object name instance for '%s'", objectName), e);
		}
	}
}
