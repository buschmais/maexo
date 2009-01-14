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
package com.buschmais.osgi.maexo.framework.switchboard.impl;

import javax.management.MalformedObjectNameException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Represents an mbean notification listener which is registered with the
 * switchboard.
 * 
 * @see SwitchBoardImpl
 * @see javax.management.MBeanServer
 */
public final class NotificationListenerRegistration {

	/**
	 * The object name property.
	 */
	private static final String SERVICE_PROPERTY_OBJECTNAME = "objectName";

	/**
	 * The handback property.
	 */
	private static final String SERVICE_PROPERTY_HANDBACK = "handback";

	private ObjectName objectName;

	private NotificationListener notificationListener;

	private NotificationFilter notificationFilter;

	private Object handback;

	/**
	 * Constructor.
	 * <p>
	 * The constructor extracts the notification listener, the mbean object
	 * name, the notification filter and the handback context object from the
	 * provided service reference and the bundle context.
	 * 
	 * @param bundleContext
	 *            the bundle context
	 * @param serviceReference
	 *            the service reference
	 * @throws MalformedObjectNameException
	 *             if the object name has a syntax error
	 * @exception NullPointerException
	 *                if the object name is <code>null</code>
	 */
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

	/**
	 * {@inheritDoc}
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		NotificationListenerRegistration other = (NotificationListenerRegistration) obj;
		if (handback == null) {
			if (other.handback != null) {
				return false;
			}
		} else if (!handback.equals(other.handback)) {
			return false;
		}
		if (notificationFilter == null) {
			if (other.notificationFilter != null) {
				return false;
			}
		} else if (!notificationFilter.equals(other.notificationFilter)) {
			return false;
		}
		if (notificationListener == null) {
			if (other.notificationListener != null) {
				return false;
			}
		} else if (!notificationListener.equals(other.notificationListener)) {
			return false;
		}
		if (objectName == null) {
			if (other.objectName != null) {
				return false;
			}
		} else if (!objectName.equals(other.objectName)) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("%s (listener: %s, filter: %s, handback: %s)",
				this.objectName, this.notificationListener,
				this.notificationFilter, this.handback);
	}

}
