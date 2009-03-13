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
package com.buschmais.maexo.framework.switchboard.impl;

import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;

/**
 * Represents an MBean notification listener which is registered with the
 * switchboard.
 *
 * @see SwitchBoardImpl
 * @see javax.management.MBeanServer
 */
public final class NotificationListenerRegistration {

	/** The object name. */
	private ObjectName objectName;

	/** The notification listener. */
	private final NotificationListener notificationListener;

	/** The notification filter. */
	private final NotificationFilter notificationFilter;

	/** The handback. */
	private final Object handback;

	/**
	 * Constructor.
	 *
	 * @param notificationListener
	 *            The notification listener.
	 * @param objectName
	 *            The object name.
	 * @param notificationFilter
	 *            The notification filter (optional),
	 * @param handback
	 *            The handback object.
	 */
	public NotificationListenerRegistration(
			NotificationListener notificationListener, ObjectName objectName,
			NotificationFilter notificationFilter, Object handback) {
		this.notificationListener = notificationListener;
		this.handback = handback;
		this.notificationFilter = notificationFilter;
		this.objectName = objectName;
	}

	/**
	 * Returns the object name.
	 *
	 * @return The object name.
	 */
	public ObjectName getObjectName() {
		return objectName;
	}

	/**
	 * Returns the notification listener.
	 *
	 * @return The notification listener.
	 */
	public NotificationListener getNotificationListener() {
		return notificationListener;
	}

	/**
	 * Returns the notification filter.
	 *
	 * @return The notification filter.
	 */
	public NotificationFilter getNotificationFilter() {
		return notificationFilter;
	}

	/**
	 * Returns the handback.
	 *
	 * @return The handback.
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
