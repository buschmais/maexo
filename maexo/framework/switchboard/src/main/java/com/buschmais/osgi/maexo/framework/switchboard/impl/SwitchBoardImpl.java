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

/**
 * Provides transparent couplings between mbeans, mbean servers, mbean server
 * connections and the OSGi service registry.
 * <p>
 * The main idea is to register mbeans not to the mbean server directly but to
 * the OSGi service registry as an OSGi service. The <strong>MAEXO switch
 * board</strong> will take care of that all mbean services will be registered
 * (and transparently unregistered) to all known mbean servers.
 * <p>
 * Established use cases:
 * <ul>
 * <li>Export mbeans, that are found or registered in the OSGi service registry,
 * to all known mbean servers</li>
 * <li>Unregister any mbeans, if the providing bundle is stopped.</li>
 * <li></li>
 * <li>TODO@DM: Please complete this list</li>
 * </ul>
 * 
 */
public final class SwitchBoardImpl {

	private static Logger logger = LoggerFactory
			.getLogger(SwitchBoardImpl.class);

	private Set<MBeanServerConnectionRegistration> mbeanServerConnections = new HashSet<MBeanServerConnectionRegistration>();

	private Set<MBeanServerRegistration> mbeanServers = new HashSet<MBeanServerRegistration>();

	private Set<MBeanRegistration> mbeans = new HashSet<MBeanRegistration>();

	private Set<NotificationListenerRegistration> notificationListeners = new HashSet<NotificationListenerRegistration>();

	/**
	 * Returns the currently registered mbeans.
	 * 
	 * @return the map of mbeans registrations
	 */
	public Set<MBeanRegistration> getMBeans() {
		return Collections.unmodifiableSet(this.mbeans);
	}

	/**
	 * Returns the registered mbean server connections.
	 * 
	 * @return the mbean server connection registrations
	 */
	public Set<MBeanServerConnectionRegistration> getMBeanServerConnections() {
		return Collections.unmodifiableSet(this.mbeanServerConnections);
	}

	/**
	 * Returns the registered mbean servers.
	 * 
	 * @return the mbean server registrations
	 */
	public Set<MBeanServerRegistration> getMBeanServers() {
		return Collections.unmodifiableSet(this.mbeanServers);
	}

	/**
	 * Returns the currently registered notification listeners.
	 * 
	 * @return the set of notification listener registrations
	 */
	public Set<NotificationListenerRegistration> getNotificationListeners() {
		return Collections.unmodifiableSet(this.notificationListeners);
	}

	/**
	 * Adds a notification listener.
	 * <p>
	 * The notification listener is registered on all known mbean server
	 * connections.
	 * 
	 * @param notificationListenerRegistration
	 *            the notification listener registration
	 */
	public synchronized void addNotificationListener(
			NotificationListenerRegistration notificationListenerRegistration) {
		logger.debug("adding notification listener {}",
				notificationListenerRegistration);
		if (!this.notificationListeners
				.contains(notificationListenerRegistration)) {
			logger
					.debug(
							"adding notification listener {} on all known mbean server connections",
							notificationListenerRegistration);
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
			logger.warn("notification listener {} is already registered",
					notificationListenerRegistration);
		}
	}

	/**
	 * Registers an mbean server connection instance.
	 * <p>
	 * The mbean server connection is associated with all known notification
	 * listeners.
	 * 
	 * @param mbeanServerConnectionRegistration
	 *            the mbean server connection registration
	 */
	public synchronized void registerMBeanServerConnection(
			MBeanServerConnectionRegistration mbeanServerConnectionRegistration) {
		logger.debug("registering mbean server connection {}",
				mbeanServerConnectionRegistration);
		if (!this.mbeanServerConnections
				.contains(mbeanServerConnectionRegistration)) {
			logger
					.debug(
							"associating mbean server connection {} with all known notification listeners",
							mbeanServerConnectionRegistration);
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
			logger.warn("mbean server connection {} is already registered",
					mbeanServerConnectionRegistration);
		}
	}

	/**
	 * Adds a notification listener on an mbean server connection.
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
	 *            the handback context object
	 */
	private void addNotificationListener(
			MBeanServerConnection mbeanServerConnection, ObjectName objectName,
			NotificationListener notificationListener,
			NotificationFilter notificationFilter, Object handback) {
		try {
			logger.trace(
					"add notification listener {} on server connection {}",
					notificationListener, mbeanServerConnection);
			mbeanServerConnection.addNotificationListener(objectName,
					notificationListener, notificationFilter, handback);
		} catch (Exception e) {
			logger
					.warn(
							String
									.format(
											"exception while adding notification listener %s on mbean server connection %s",
											notificationListener,
											mbeanServerConnection), e);
		}
	}

	/**
	 * Registers an mbean.
	 * <p>
	 * The mbean is registered on all known mbean servers.
	 * 
	 * @param mbeanRegistration
	 *            the mbean registration
	 */
	public synchronized void registerMBean(MBeanRegistration mbeanRegistration) {
		logger.debug("registering mbean {}", mbeanRegistration);
		if (!this.mbeans.contains(mbeanRegistration)) {
			logger.debug("registering mbean {} on all known mbean servers",
					mbeanRegistration);
			this.mbeans.add(mbeanRegistration);
			for (MBeanServerRegistration mbeanServerRegistration : this.mbeanServers) {
				this.registerMBean(mbeanServerRegistration.getMbeanServer(),
						mbeanRegistration.getObjectName(), mbeanRegistration
								.getMbean());
			}
		} else {
			logger.warn("mbean {} is already registered", mbeanRegistration);
		}
	}

	/**
	 * Registers a mbean server instance.
	 * <p>
	 * The mbean server instance is associated with all known mbeans.
	 * 
	 * @param mbeanServerRegistration
	 *            the mbean server registration
	 */
	public synchronized void registerMBeanServer(
			MBeanServerRegistration mbeanServerRegistration) {
		logger.debug("registering mbean server {}", mbeanServerRegistration);
		if (!this.mbeanServers.contains(mbeanServerRegistration)) {
			logger.debug("associating mbean server {} with all known mbeans",
					mbeanServerRegistration);
			this.mbeanServers.add(mbeanServerRegistration);
			for (MBeanRegistration mbeanRegistration : this.mbeans) {
				this.registerMBean(mbeanServerRegistration.getMbeanServer(),
						mbeanRegistration.getObjectName(), mbeanRegistration
								.getMbean());
			}
		} else {
			logger.warn("mbean server {} is already registered",
					mbeanServerRegistration);
		}
	}

	/**
	 * Registers an mbean on a single mbean server.
	 * 
	 * @param mbeanServer
	 *            the mbean server
	 * @param objectName
	 *            the object name
	 * @param mbean
	 *            the mbean
	 */
	private void registerMBean(MBeanServer mbeanServer, ObjectName objectName,
			Object mbean) {
		logger.trace("registering mbean {} on mbean server {}", objectName,
				mbeanServer);
		try {
			mbeanServer.registerMBean(mbean, objectName);
		} catch (Exception e) {
			logger
					.warn(
							String
									.format(
											"caught exception while registering mbean %s on mbean server %s",
											objectName, mbeanServer), e);
		}
	}

	/**
	 * Removes a notification listener.
	 * <p>
	 * The notification listener is removed from all known mbean server
	 * connections.
	 * 
	 * @param notificationListenerRegistration
	 *            the notification listener registration
	 * 
	 */
	public synchronized void removeNotificationListener(
			NotificationListenerRegistration notificationListenerRegistration) {
		logger.debug("removing notification listener {}",
				notificationListenerRegistration);
		if (this.notificationListeners.remove(notificationListenerRegistration)) {
			logger
					.debug(
							"removing notification listener {} from all known mbean server connections",
							notificationListenerRegistration);
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
			logger.warn("notification listener {} is not registered",
					notificationListenerRegistration);
		}
	}

	/**
	 * Unregisters an mbean server connection instance.
	 * <p>
	 * The mbean server connection is disassociated with all known notification
	 * listeners.
	 * 
	 * @param mbeanServerConnectionRegistration
	 *            the mbean server connection registration
	 */
	public synchronized void unregisterMBeanServerConnection(
			MBeanServerConnectionRegistration mbeanServerConnectionRegistration) {
		logger.debug("unregistering mbean server connection {}",
				mbeanServerConnectionRegistration);
		if (this.mbeanServerConnections
				.remove(mbeanServerConnectionRegistration)) {
			logger
					.debug(
							"disassociating mbean server connection {} with all known notification listeners",
							mbeanServerConnectionRegistration);
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
			logger.warn(
					"mbean server connection {} is not registered, skipping",
					mbeanServerConnectionRegistration);
		}
	}

	/**
	 * Removes a notification listener from a single mbean server connection.
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
	 *            the handback context object
	 */
	private void removeNotificationListener(
			MBeanServerConnection mbeanServerConnection, ObjectName objectName,
			NotificationListener notificationListener,
			NotificationFilter notificationFilter, Object handback) {
		try {
			logger
					.trace(
							"removing notification listener {} from mbean server connection {}",
							notificationListener, mbeanServerConnection);
			mbeanServerConnection.removeNotificationListener(objectName,
					notificationListener, notificationFilter, handback);
		} catch (Exception e) {
			logger
					.warn(
							String
									.format(
											"caught exception while removing notification listener %s from mbean server conncetion %s",
											notificationListener,
											mbeanServerConnection), e);
		}
	}

	/**
	 * Unregisters an mbean.
	 * <p>
	 * The mbean is unregistered from all known mbean servers.
	 * 
	 * @param mbeanRegistration
	 *            the mbean registration
	 */
	public synchronized void unregisterMBean(MBeanRegistration mbeanRegistration) {
		logger.debug("unregistering mbean {}", mbeanRegistration);
		if (this.mbeans.remove(mbeanRegistration)) {
			logger.debug("unregistering mbean {} from all known mbean servers",
					mbeanRegistration);
			for (MBeanServerRegistration mbeanServerRegistration : this.mbeanServers) {
				this.unregisterMBean(mbeanServerRegistration.getMbeanServer(),
						mbeanRegistration.getObjectName());
			}
		} else {
			logger.warn("mbean {} is not registered", mbeanRegistration);
		}
	}

	/**
	 * Unregisters an mbean server instance.
	 * <p>
	 * The mbean server instance is disassociated with all known mbeans.
	 * 
	 * @param mbeanServerRegistration
	 *            the mbean server registration
	 */
	public synchronized void unregisterMBeanServer(
			MBeanServerRegistration mbeanServerRegistration) {
		logger.debug("unregister mbean server {}", mbeanServerRegistration);
		if (this.mbeanServers.remove(mbeanServerRegistration)) {
			logger.debug(
					"disassociating mbean server {} with all known mbeans",
					mbeanServerRegistration);
			for (MBeanRegistration mbeanRegistration : this.mbeans) {
				this.unregisterMBean(mbeanServerRegistration.getMbeanServer(),
						mbeanRegistration.getObjectName());
			}
		} else {
			logger.warn("mbean server {} is not registered, skipping",
					mbeanServerRegistration);
		}
	}

	/**
	 * Unregisters an mbean from a single mbean server.
	 * 
	 * @param mbeanServer
	 *            the mbean server
	 * @param objectName
	 *            the name
	 */
	private void unregisterMBean(MBeanServer mbeanServer, ObjectName objectName) {
		try {
			logger.trace("unregister mbean {} from server {}", objectName,
					mbeanServer);
			mbeanServer.unregisterMBean(objectName);
		} catch (Exception e) {
			logger
					.warn(
							String
									.format(
											"caught exception while unregistering mbean %s from mbean server %s",
											objectName, mbeanServer), e);
		}
	}

}
