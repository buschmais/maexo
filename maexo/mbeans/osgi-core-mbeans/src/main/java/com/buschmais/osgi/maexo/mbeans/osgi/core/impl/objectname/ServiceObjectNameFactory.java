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
import org.osgi.framework.ServiceReference;

import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameFactory;
import com.buschmais.osgi.maexo.mbeans.osgi.core.ServiceConstants;

/**
 * Object name factory implementation for service references.
 */
public final class ServiceObjectNameFactory implements ObjectNameFactory {

	/**
	 * The separator token to use if several object classes are implemented by
	 * the service.
	 */
	private static final char SEPARATOR_OBJECTCLASS = '|';

	/**
	 * {@inheritDoc}
	 */
	public ObjectName getObjectName(Object resource,
			Map<String, Object> properties) {
		ServiceReference serviceReference = (ServiceReference) resource;

		// id
		Long id = (Long) serviceReference.getProperty(Constants.SERVICE_ID);
		// object classes
		String[] objectClasses = (String[]) serviceReference
				.getProperty(Constants.OBJECTCLASS);
		StringBuilder objectClassValue = new StringBuilder();
		boolean firstItem = true;
		for (String objectClass : objectClasses) {
			if (firstItem) {
				firstItem = false;
			} else {
				objectClassValue.append(SEPARATOR_OBJECTCLASS);
			}
			objectClassValue.append(objectClass);
		}
		String objectName = String.format(ServiceConstants.OBJECTNAME_FORMAT,
				id, objectClassValue.toString());
		try {
			return new ObjectName(objectName);
		} catch (Exception e) {
			throw new IllegalArgumentException(String.format(
					"Cannot create object name instance for '%s'", objectName),
					e);
		}
	}
}
