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
package com.buschmais.osgi.maexo.mbeans.osgi.core.impl.objectname;

import java.util.Properties;

import javax.management.ObjectName;

import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameFactory;
import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameHelper;

/**
 * Object name factory implementation which supports service references
 */
public class ServiceObjectNameFactory implements ObjectNameFactory {

	private static final String PROPERTY_TYPE_SERVICE = "service";

	private static final String PROPERTY_PID = "pid";

	private static final char SEPARATOR_OBJECTCLASS = '|';

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.maexo.core.registry.ObjectNameFactory#getObjectName(java
	 * .lang.Object)
	 */
	public ObjectName getObjectName(Object resource) {
		ServiceReference serviceReference = (ServiceReference) resource;
		Properties objectNameProperties = new Properties();
		// type
		objectNameProperties.setProperty(ObjectNameFactory.PROPERTY_TYPE,
				PROPERTY_TYPE_SERVICE);
		// id
		Long id = (Long) serviceReference.getProperty(Constants.SERVICE_ID);
		objectNameProperties.put(ObjectNameFactory.PROPERTY_ID, id);
		// object classes
		String[] objectClasses = (String[]) serviceReference
				.getProperty(Constants.OBJECTCLASS);
		StringBuilder objectClassValue = new StringBuilder();
		for (String objectClass : objectClasses) {
			if (objectClassValue.length() > 0) {
				objectClassValue.append(SEPARATOR_OBJECTCLASS);
			}
			objectClassValue.append(objectClass);
		}
		objectNameProperties.put(ObjectNameFactory.PROPERTY_NAME,
				objectClassValue);
		// pid
		String pid = (String) serviceReference
				.getProperty(Constants.SERVICE_PID);
		if (pid != null) {
			objectNameProperties.put(PROPERTY_PID, pid);
		}
		return ObjectNameHelper.getObjectName(objectNameProperties);
	}
}
