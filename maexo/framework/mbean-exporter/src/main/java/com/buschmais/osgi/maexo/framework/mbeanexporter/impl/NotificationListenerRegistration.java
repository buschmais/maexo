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
package com.buschmais.osgi.maexo.framework.mbeanexporter.impl;

import javax.management.MalformedObjectNameException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class NotificationListenerRegistration {

	/**
	 * the object name property
	 */
	private static final String SERVICE_PROPERTY_OBJECTNAME = "objectName";

	/**
	 * the handback property
	 */
	private static final String SERVICE_PROPERTY_HANDBACK = "handback";

	private ObjectName objectName;

	private NotificationListener notificationListener;

	private NotificationFilter notificationFilter;

	private Object handback;

	public NotificationListenerRegistration(BundleContext bundleContext,
			ServiceReference serviceReference)
			throws MalformedObjectNameException, NullPointerException {
		this.notificationListener = (NotificationListener) bundleContext
				.getService(serviceReference);
		this.objectName = (ObjectName) serviceReference
				.getProperty(ObjectName.class.getName());
		if (this.objectName == null) {
			String name = (String) serviceReference
					.getProperty(SERVICE_PROPERTY_OBJECTNAME);
			this.objectName = new ObjectName(name);
		}
		this.notificationFilter = (NotificationFilter) serviceReference
				.getProperty(NotificationFilter.class.getName());
		this.handback = serviceReference.getProperty(SERVICE_PROPERTY_HANDBACK);
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
		result = prime * result
				+ ((handback == null) ? 0 : handback.hashCode());
		result = prime
				* result
				+ ((notificationFilter == null) ? 0 : notificationFilter
						.hashCode());
		result = prime
				* result
				+ ((notificationListener == null) ? 0 : notificationListener
						.hashCode());
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
		NotificationListenerRegistration other = (NotificationListenerRegistration) obj;
		if (handback == null) {
			if (other.handback != null)
				return false;
		} else if (!handback.equals(other.handback))
			return false;
		if (notificationFilter == null) {
			if (other.notificationFilter != null)
				return false;
		} else if (!notificationFilter.equals(other.notificationFilter))
			return false;
		if (notificationListener == null) {
			if (other.notificationListener != null)
				return false;
		} else if (!notificationListener.equals(other.notificationListener))
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
	 * @return the notificationListener
	 */
	public NotificationListener getNotificationListener() {
		return notificationListener;
	}

	/**
	 * @return the notificationFilter
	 */
	public NotificationFilter getNotificationFilter() {
		return notificationFilter;
	}

	/**
	 * @return the handback
	 */
	public Object getHandback() {
		return handback;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.objectName + " (" + this.notificationListener + ", "
				+ this.notificationFilter + ", " + this.handback + ")";
	}

}
