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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwitchBoardImpl {

	private static Logger logger = LoggerFactory
			.getLogger(SwitchBoardImpl.class);

	private Set<MBeanServerConnectionRegistration> mbeanServerConnections = new HashSet<MBeanServerConnectionRegistration>();

	private Set<MBeanServerRegistration> mbeanServers = new HashSet<MBeanServerRegistration>();

	private Set<MBeanRegistration> mbeans = new HashSet<MBeanRegistration>();

	private Set<NotificationListenerRegistration> notificationListeners = new HashSet<NotificationListenerRegistration>();

	/**
	 * Adds a notification listener on a mbean server connection
	 * 
	 * @param mbeanServerConnection
	 *            the mbean server connection
	 * @param objectName
	 *            the object name
	 * @param notificationListener
	 *            the notification listener
	 * @param handback
	 *            the handback object
	 */
	private void addNotificationListener(
			MBeanServerConnection mbeanServerConnection, ObjectName objectName,
			NotificationListener notificationListener,
			NotificationFilter notificationFilter, Object handback) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("adding notification listener "
						+ notificationListener + " on server "
						+ mbeanServerConnection);
			}
			mbeanServerConnection.addNotificationListener(objectName,
					notificationListener, notificationFilter, handback);
		} catch (Exception e) {
			logger.warn("caught exception while adding notification listener "
					+ notificationListener + ", on mbean server connection"
					+ mbeanServerConnection, e);
		}
	}

	/**
	 * Adds a notification listener
	 * 
	 * @param notificationListenerRegistration
	 *            the notification listener registration
	 */
	public synchronized void addNotificationListener(
			NotificationListenerRegistration notificationListenerRegistration) {
		if (!this.notificationListeners
				.contains(notificationListenerRegistration)) {
			if (logger.isDebugEnabled()) {
				logger.debug("adding notification listener "
						+ notificationListenerRegistration);
			}
			this.notificationListeners.add(notificationListenerRegistration);
			for (MBeanServerConnectionRegistration mbeanServerConnectionRegistration : this.mbeanServerConnections) {
				this.addNotificationListener(mbeanServerConnectionRegistration
						.getMbeanServerConnection(),
						notificationListenerRegistration.getObjectName(),
						notificationListenerRegistration
								.getNotificationListener(),
						notificationListenerRegistration
								.getNotificationFilter(),
						notificationListenerRegistration.getHandback());
			}
		} else {
			logger.warn("notification listener "
					+ notificationListenerRegistration
					+ " is already registered");
		}
	}

	/**
	 * Returns the currently registered mbeans
	 * 
	 * @return the map of mbeans registrations
	 */
	public Set<MBeanRegistration> getMBeans() {
		return Collections.unmodifiableSet(this.mbeans);
	}

	/**
	 * Returns the registered mbean server connections
	 * 
	 * @return the mbean server connection registrations
	 */
	public Set<MBeanServerConnectionRegistration> getMBeanServerConnections() {
		return Collections.unmodifiableSet(this.mbeanServerConnections);
	}

	/**
	 * Returns the registered mbean servers
	 * 
	 * @return the mbean server registrations
	 */
	public Set<MBeanServerRegistration> getMBeanServers() {
		return Collections.unmodifiableSet(this.mbeanServers);
	}

	/**
	 * Returns the currently registered notification listeners
	 * 
	 * @return the set of notification listener registrations
	 */
	public Set<NotificationListenerRegistration> getNotificationListeners() {
		return Collections.unmodifiableSet(this.notificationListeners);
	}

	/**
	 * Registers a mbean on a mbean server
	 * 
	 * @param mbeanServer
	 *            the mbean server
	 * @param objectName
	 *            the name
	 * @param mbean
	 *            the mbean
	 */
	private void registerMBean(MBeanServer mbeanServer, ObjectName objectName,
			Object mbean) {
		if (logger.isDebugEnabled()) {
			logger.debug("registering mbean " + objectName + " on server "
					+ mbeanServer);
		}
		try {
			mbeanServer.registerMBean(mbean, objectName);
		} catch (Exception e) {
			logger.warn("caught exception while registering mbean "
					+ objectName + ", on mbean server " + mbeanServer, e);
		}
	}

	/**
	 * Registers a mbean
	 * 
	 * @param mbeanRegistration
	 *            the mbean registration
	 */
	public synchronized void registerMBean(MBeanRegistration mbeanRegistration) {
		if (!this.mbeans.contains(mbeanRegistration)) {
			if (logger.isDebugEnabled()) {
				logger.debug("registering mbean " + mbeanRegistration);
			}
			this.mbeans.add(mbeanRegistration);
			for (MBeanServerRegistration mbeanServerRegistration : this.mbeanServers) {
				this.registerMBean(mbeanServerRegistration.getMbeanServer(),
						mbeanRegistration.getObjectName(), mbeanRegistration
								.getMbean());
			}
		} else {
			logger
					.warn("mbean " + mbeanRegistration
							+ " is already registered");
		}
	}

	/**
	 * Registers a mbean server connection instance
	 * 
	 * @param mbeanServerConnectionRegistration
	 *            the mbean server connection registration
	 */
	public synchronized void registerMBeanServerConnection(
			MBeanServerConnectionRegistration mbeanServerConnectionRegistration) {
		if (!this.mbeanServerConnections
				.contains(mbeanServerConnectionRegistration)) {
			if (logger.isDebugEnabled()) {
				logger.debug("registering mbean server connection"
						+ mbeanServerConnectionRegistration);
			}
			this.mbeanServerConnections.add(mbeanServerConnectionRegistration);
			for (NotificationListenerRegistration notificationListenerRegistration : this.notificationListeners) {
				this.addNotificationListener(mbeanServerConnectionRegistration
						.getMbeanServerConnection(),
						notificationListenerRegistration.getObjectName(),
						notificationListenerRegistration
								.getNotificationListener(),
						notificationListenerRegistration
								.getNotificationFilter(),
						notificationListenerRegistration.getHandback());
			}
		} else {
			logger.warn("mbean server connection "
					+ mbeanServerConnectionRegistration
					+ " is already registered");
		}
	}

	/**
	 * Registers a mbean server instance
	 * 
	 * @param mbeanServerRegistration
	 *            the mbean server registration
	 */
	public synchronized void registerMBeanServer(
			MBeanServerRegistration mbeanServerRegistration) {
		if (!this.mbeanServers.contains(mbeanServerRegistration)) {
			if (logger.isDebugEnabled()) {
				logger.debug("registering mbean server "
						+ mbeanServerRegistration);
			}
			this.mbeanServers.add(mbeanServerRegistration);
			for (MBeanRegistration mbeanRegistration : this.mbeans) {
				this.registerMBean(mbeanServerRegistration.getMbeanServer(),
						mbeanRegistration.getObjectName(), mbeanRegistration
								.getMbean());
			}
		} else {
			logger.warn("mbean server " + mbeanServerRegistration
					+ " is already registered");
		}
	}

	/**
	 * Removes a notification listener from a mbean server
	 * 
	 * @param mbeanServerConnection
	 *            the mbean server connection
	 * @param objectName
	 *            the object name
	 * @param notificationListener
	 *            the notification listener
	 * @param notificationFilter
	 *            the notification filter
	 * @param handback
	 *            the handback object
	 */
	private void removeNotificationListener(
			MBeanServerConnection mbeanServerConnection, ObjectName objectName,
			NotificationListener notificationListener,
			NotificationFilter notificationFilter, Object handback) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("removing notification listener "
						+ notificationListener
						+ " from mbean server connection "
						+ mbeanServerConnection);
			}
			mbeanServerConnection.removeNotificationListener(objectName,
					notificationListener, notificationFilter, handback);
		} catch (Exception e) {
			logger.warn(
					"caught exception while removing notification listener "
							+ notificationListener
							+ ", from mbean server conncetion "
							+ mbeanServerConnection, e);
		}
	}

	/**
	 * Removes a notification listener
	 * 
	 * @param notificationListenerRegistration
	 *            the notification listener registration
	 * 
	 */
	public synchronized void removeNotificationListener(
			NotificationListenerRegistration notificationListenerRegistration) {
		if (this.notificationListeners.remove(notificationListenerRegistration)) {
			if (logger.isDebugEnabled()) {
				logger.debug("removing notification listener "
						+ notificationListenerRegistration);
			}
			for (MBeanServerConnectionRegistration mbeanServerConnectionRegistration : this.mbeanServerConnections) {
				this.removeNotificationListener(
						mbeanServerConnectionRegistration
								.getMbeanServerConnection(),
						notificationListenerRegistration.getObjectName(),
						notificationListenerRegistration
								.getNotificationListener(),
						notificationListenerRegistration
								.getNotificationFilter(),
						notificationListenerRegistration.getHandback());
			}
		} else {
			logger.warn("notification listener "
					+ notificationListenerRegistration + " is not registered");
		}
	}

	/**
	 * Unregisters a mbean from a mbean server
	 * 
	 * @param mbeanServer
	 *            the mbean server
	 * @param objectName
	 *            the name
	 */
	private void unregisterMBean(MBeanServer mbeanServer, ObjectName objectName) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("unregistering mbean " + objectName
						+ " from server " + mbeanServer);
			}
			mbeanServer.unregisterMBean(objectName);
		} catch (Exception e) {
			logger.warn("caught exception while unregistering mbean "
					+ objectName + ", from mbean server " + mbeanServer, e);
		}
	}

	/**
	 * Unregisters a mbean
	 * 
	 * @param mbeanRegistration
	 *            the mbean registration
	 */
	public synchronized void unregisterMBean(MBeanRegistration mbeanRegistration) {
		if (this.mbeans.remove(mbeanRegistration)) {
			if (logger.isDebugEnabled()) {
				logger.debug("unregistering mbean " + mbeanRegistration);
			}
			for (MBeanServerRegistration mbeanServerRegistration : this.mbeanServers) {
				this.unregisterMBean(mbeanServerRegistration.getMbeanServer(),
						mbeanRegistration.getObjectName());
			}
		} else {
			logger.warn("mbean " + mbeanRegistration + " is not registered");
		}
	}

	/**
	 * Unregisters a mbean server connection instance
	 * 
	 * @param mbeanServerConnectionRegistration
	 *            the mbean server connection registration
	 */
	public synchronized void unregisterMBeanServerConnection(
			MBeanServerConnectionRegistration mbeanServerConnectionRegistration) {
		if (this.mbeanServerConnections
				.remove(mbeanServerConnectionRegistration)) {
			if (logger.isDebugEnabled()) {
				logger.debug("unregistering mbean server connection "
						+ mbeanServerConnectionRegistration);
			}
			for (NotificationListenerRegistration notificationListenerRegistration : this.notificationListeners) {
				this.removeNotificationListener(
						mbeanServerConnectionRegistration
								.getMbeanServerConnection(),
						notificationListenerRegistration.getObjectName(),
						notificationListenerRegistration
								.getNotificationListener(),
						notificationListenerRegistration
								.getNotificationFilter(),
						notificationListenerRegistration.getHandback());
			}
		} else {
			logger.warn("mbean server connection "
					+ mbeanServerConnectionRegistration
					+ " is not registered, skipping.");
		}
	}

	/**
	 * Unregisters a mbean server instance
	 * 
	 * @param mbeanServerRegistration
	 *            the mbean server registration
	 */
	public synchronized void unregisterMBeanServer(
			MBeanServerRegistration mbeanServerRegistration) {
		if (this.mbeanServers.remove(mbeanServerRegistration)) {
			if (logger.isDebugEnabled()) {
				logger.debug("unregistering mbean server "
						+ mbeanServerRegistration);
			}
			for (MBeanRegistration mbeanRegistration : this.mbeans) {
				this.unregisterMBean(mbeanServerRegistration.getMbeanServer(),
						mbeanRegistration.getObjectName());
			}
		} else {
			logger.warn("mbean server " + mbeanServerRegistration
					+ " is not registered, skipping.");
		}
	}
}