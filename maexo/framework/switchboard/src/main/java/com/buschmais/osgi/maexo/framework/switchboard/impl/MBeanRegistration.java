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
package com.buschmais.osgi.maexo.framework.switchboard.impl;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class MBeanRegistration {

	/**
	 * The object name property
	 */
	private static final String SERVICE_PROPERTY_OBJECTNAME = "objectName";

	private ObjectName objectName;

	private Object mbean;

	public MBeanRegistration(BundleContext bundleContext,
			ServiceReference serviceReference)
			throws MalformedObjectNameException, NullPointerException {
		// get object name from service properties
		this.objectName = (ObjectName) serviceReference
				.getProperty(ObjectName.class.getName());
		if (this.objectName == null) {
			String name = (String) serviceReference
					.getProperty(SERVICE_PROPERTY_OBJECTNAME);
			this.objectName = new ObjectName(name);
		}
		this.mbean = bundleContext.getService(serviceReference);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mbean == null) ? 0 : mbean.hashCode());
		result = prime * result
				+ ((objectName == null) ? 0 : objectName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MBeanRegistration other = (MBeanRegistration) obj;
		if (mbean == null) {
			if (other.mbean != null)
				return false;
		} else if (!mbean.equals(other.mbean))
			return false;
		if (objectName == null) {
			if (other.objectName != null)
				return false;
		} else if (!objectName.equals(other.objectName))
			return false;
		return true;
	}

	/**
	 * @return the objectName
	 */
	public ObjectName getObjectName() {
		return objectName;
	}

	/**
	 * @return the mbean
	 */
	public Object getMbean() {
		return mbean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.objectName + " (" + this.mbean + ")";
	}

}
