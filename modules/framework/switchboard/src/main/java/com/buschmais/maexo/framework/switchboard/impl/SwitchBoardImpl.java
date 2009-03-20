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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerDelegateMBean;
import javax.management.MBeanServerInvocationHandler;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides transparent couplings between MBeans, {@link NotificationListener}s,
 * {@link MBeanServer}s and {@link MBeanServerConnection}s by tracking the life
 * cycle of these items in the OSGi service registry.
 * <p>
 * The main idea is to register MBeans not to the MBean server directly but to
 * the OSGi service registry as an OSGi service. The <strong>MAEXO switch
 * board</strong> will take care of that all MBean services will be registered
 * (and transparently unregistered) to all known MBean servers. The same
 * mechanism is implemented for the relation between NotificationListeners and
 * MBean server connections.
 */
public final class SwitchBoardImpl {

	/**
	 * The service property which defines the {@link ObjectName} of an MBean.
	 */
	private static final String SERVICEPROPERTY_OBJECTNAME = "objectName";

	/**
	 * The service property which defines the hand back for a
	 * {@link NotificationListener}.
	 */
	private static final String SERVICEPROPERTY_HANDBACK = "handback";

	private static Logger logger = LoggerFactory
			.getLogger(SwitchBoardImpl.class);

	private Map<ServiceReference, MBeanServerConnectionRegistration> mbeanServerConnections = null;

	private Map<ServiceReference, MBeanServerRegistration> mbeanServers = null;

	private Map<ServiceReference, MBeanRegistration> mbeans = null;

	private Map<ServiceReference, NotificationListenerRegistration> notificationListeners = null;

	private BundleContext bundleContext;

	/**
	 * The Constructor.
	 *
	 * @param bundleContext
	 *            The bundle context.
	 */
	public SwitchBoardImpl(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	/**
	 * Returns the agentId of a MBean server.
	 *
	 * @param mbeanServerConnection
	 *            The MBean server connection
	 * @return The agentId or <code>null</code>.
	 */
	private String getAgentId(MBeanServerConnection mbeanServerConnection) {
		ObjectName delegateObjectName = null;
		try {
			delegateObjectName = new ObjectName(
					"JMImplementation:type=MBeanServerDelegate");
			MBeanServerDelegateMBean mbeanServerDelegateMBean = (MBeanServerDelegateMBean) MBeanServerInvocationHandler
					.newProxyInstance(mbeanServerConnection,
							delegateObjectName, MBeanServerDelegateMBean.class,
							false);
			return mbeanServerDelegateMBean.getMBeanServerId();
		} catch (Exception e) {
			logger
					.debug(
							"cannot construct object name for mbean server delegate",
							e);
		}
		return null;
	}

	/**
	 * Constructs an {@link ObjectName} from the properties of a service
	 * reference.
	 *
	 * @param serviceReference
	 *            The service reference.
	 * @return The {@link ObjectName} instance or <code>null</code> if the
	 *         properties are not set or an error occurred while construction
	 *         the instance.
	 */
	private ObjectName getObjectName(ServiceReference serviceReference) {
		// get object name from service properties
		ObjectName objectName = (ObjectName) serviceReference
				.getProperty(ObjectName.class.getName());
		if (objectName != null) {
			return objectName;
		}
		Object objectNameProperty = serviceReference
				.getProperty(SERVICEPROPERTY_OBJECTNAME);
		if (objectNameProperty != null) {
			try {
				objectName = new ObjectName((objectNameProperty.toString()));
			} catch (Exception e) {
				logger
						.debug(
								String
										.format(
												"cannot construct object name from service property %s",
												objectNameProperty), e);
			}
		}
		return objectName;
	}

	/**
	 * Starts the switch board.
	 */
	public synchronized void start() {
		this.mbeanServerConnections = new HashMap<ServiceReference, MBeanServerConnectionRegistration>();
		this.mbeanServers = new HashMap<ServiceReference, MBeanServerRegistration>();
		this.mbeans = new HashMap<ServiceReference, MBeanRegistration>();
		this.notificationListeners = new HashMap<ServiceReference, NotificationListenerRegistration>();
	}

	/**
	 * Stops the switch board.
	 * <p>
	 * All registrations of MBeans and notification listeners are removed.
	 */
	public synchronized void stop() {
		for (ServiceReference serviceReference : new HashSet<ServiceReference>(
				this.mbeanServerConnections.keySet())) {
			this.unregisterMBeanServerConnection(serviceReference);
		}
		for (ServiceReference serviceReference : new HashSet<ServiceReference>(
				this.mbeanServers.keySet())) {
			this.unregisterMBeanServer(serviceReference);
		}
		for (ServiceReference serviceReference : new HashSet<ServiceReference>(
				this.mbeans.keySet())) {
			this.unregisterMBean(serviceReference);
		}
		for (ServiceReference serviceReference : new HashSet<ServiceReference>(
				this.notificationListeners.keySet())) {
			this.removeNotificationListener(serviceReference);
		}
	}

	/**
	 * Adds a notification listener.
	 * <p>
	 * The notification listener is registered on all known MBean server
	 * connections.
	 *
	 * @param serviceReference
	 *            The service reference which provides the notification
	 *            listener.
	 */
	public synchronized void addNotificationListener(
			ServiceReference serviceReference) {
		assert (this.notificationListeners != null);
		if (this.notificationListeners.containsKey(serviceReference)) {
			logger
					.warn(
							"Notification listener from service reference '{}' is already registered.",
							serviceReference);
		} else {
			NotificationListener notificationListener = (NotificationListener) bundleContext
					.getService(serviceReference);
			ObjectName objectName = this.getObjectName(serviceReference);
			NotificationFilter notificationFilter = (NotificationFilter) serviceReference
					.getProperty(NotificationFilter.class.getName());
			Object handback = serviceReference
					.getProperty(SERVICEPROPERTY_HANDBACK);
			if (objectName != null) {
				NotificationListenerRegistration notificationListenerRegistration = new NotificationListenerRegistration(
						notificationListener, objectName, notificationFilter,
						handback);
				logger.debug("adding notification listener {}",
						notificationListenerRegistration);
				this.notificationListeners.put(serviceReference,
						notificationListenerRegistration);
				logger
						.debug(
								"adding notification listener {} on all known MBean server connections",
								notificationListenerRegistration);
				for (MBeanServerConnectionRegistration mbeanServerConnectionRegistration : this.mbeanServerConnections
						.values()) {
					this.addNotificationListener(
							mbeanServerConnectionRegistration
									.getMBeanServerConnection(),
							notificationListenerRegistration.getObjectName(),
							notificationListenerRegistration
									.getNotificationListener(),
							notificationListenerRegistration
									.getNotificationFilter(),
							notificationListenerRegistration.getHandback());
				}
			}
		}
	}

	/**
	 * Registers an MBean server connection instance.
	 * <p>
	 * The MBean server connection is associated with all known notification
	 * listeners.
	 *
	 * @param serviceReference
	 *            The service reference which provides the MBean server
	 *            connection.
	 */
	public synchronized void registerMBeanServerConnection(
			ServiceReference serviceReference) {
		assert (this.mbeanServerConnections != null);
		if (this.mbeanServerConnections.containsKey(serviceReference)) {
			logger
					.warn(
							"MBean server connection from service reference '{}' is already registered.",
							serviceReference);
		} else {
			MBeanServerConnection mbeanServerConnection = (MBeanServerConnection) this.bundleContext
					.getService(serviceReference);
			String agentId = this.getAgentId(mbeanServerConnection);
			if (agentId != null) {
				MBeanServerConnectionRegistration mbeanServerConnectionRegistration = new MBeanServerConnectionRegistration(
						agentId, mbeanServerConnection);
				logger.debug("registering MBean server connection {}",
						mbeanServerConnectionRegistration);
				this.mbeanServerConnections.put(serviceReference,
						mbeanServerConnectionRegistration);
				logger
						.debug(
								"associating MBean server connection {} with all known notification listeners",
								mbeanServerConnectionRegistration);
				for (NotificationListenerRegistration notificationListenerRegistration : this.notificationListeners
						.values()) {
					this.addNotificationListener(
							mbeanServerConnectionRegistration
									.getMBeanServerConnection(),
							notificationListenerRegistration.getObjectName(),
							notificationListenerRegistration
									.getNotificationListener(),
							notificationListenerRegistration
									.getNotificationFilter(),
							notificationListenerRegistration.getHandback());
				}
			}
		}
	}

	/**
	 * Adds a notification listener on an MBean server connection.
	 *
	 * @param mbeanServerConnection
	 *            The MBean server connection.
	 * @param objectName
	 *            The object name.
	 * @param notificationListener
	 *            The notification listener.
	 * @param notificationFilter
	 *            The notification filter.
	 * @param handback
	 *            The handback context object.
	 */
	private void addNotificationListener(
			MBeanServerConnection mbeanServerConnection, ObjectName objectName,
			NotificationListener notificationListener,
			NotificationFilter notificationFilter, Object handback) {
		try {
			logger.trace(
					"adding notification listener {} on server connection {}",
					notificationListener, mbeanServerConnection);
			mbeanServerConnection.addNotificationListener(objectName,
					notificationListener, notificationFilter, handback);
		} catch (Exception e) {
			logger
					.warn(
							String
									.format(
											"exception while adding notification listener %s on MBean server connection %s",
											notificationListener,
											mbeanServerConnection), e);
		}
	}

	/**
	 * Registers an MBean.
	 * <p>
	 * The MBean is registered on all known MBean servers.
	 *
	 * @param serviceReference
	 *            The service reference which provides the MBean.
	 */
	public synchronized void registerMBean(ServiceReference serviceReference) {
		assert (this.mbeans != null);
		if (this.mbeans.containsKey(serviceReference)) {
			logger.warn(
					"MBean from service reference '{}' is already registered.",
					serviceReference);
		} else {
			Object mbean = this.bundleContext.getService(serviceReference);
			ObjectName objectName = this.getObjectName(serviceReference);
			if (objectName != null && mbean != null) {
				MBeanRegistration mbeanRegistration = new MBeanRegistration(
						objectName, mbean);
				logger.debug("registering MBean '{}'", mbeanRegistration);
				this.mbeans.put(serviceReference, mbeanRegistration);
				logger.debug(
						"registering MBean '{}' on all known MBean servers",
						mbeanRegistration);
				for (MBeanServerRegistration mbeanServerRegistration : this.mbeanServers
						.values()) {
					this.registerMBean(
							mbeanServerRegistration.getMBeanServer(),
							mbeanRegistration.getObjectName(),
							mbeanRegistration.getMbean());
				}
			}
		}
	}

	/**
	 * Registers a MBean server instance.
	 * <p>
	 * The MBean server instance is associated with all known MBeans.
	 *
	 * @param serviceReference
	 *            The service reference which provides the MBean server.
	 */
	public synchronized void registerMBeanServer(
			ServiceReference serviceReference) {
		assert (this.mbeanServers != null);
		if (this.mbeanServers.containsKey(serviceReference)) {
			logger
					.warn(
							"MBean server from service reference '{}' is already registered.",
							serviceReference);
		} else {
			MBeanServer mbeanServer = (MBeanServer) bundleContext
					.getService(serviceReference);
			String agentId = this.getAgentId(mbeanServer);
			if (agentId != null) {
				MBeanServerRegistration mbeanServerRegistration = new MBeanServerRegistration(
						agentId, mbeanServer);
				logger.debug("registering MBean server '{}'",
						mbeanServerRegistration);
				this.mbeanServers
						.put(serviceReference, mbeanServerRegistration);
				logger.debug(
						"associating MBean server {} with all known MBeans",
						mbeanServerRegistration);
				for (MBeanRegistration mbeanRegistration : this.mbeans.values()) {
					this.registerMBean(
							mbeanServerRegistration.getMBeanServer(),
							mbeanRegistration.getObjectName(),
							mbeanRegistration.getMbean());
				}
			}
		}
	}

	/**
	 * Registers an MBean on a single MBean server.
	 *
	 * @param mbeanServer
	 *            The MBean server.
	 * @param objectName
	 *            The object name.
	 * @param mbean
	 *            The MBean.
	 */
	private void registerMBean(MBeanServer mbeanServer, ObjectName objectName,
			Object mbean) {
		logger.trace("registering MBean {} on MBean server {}", objectName,
				mbeanServer);
		try {
			mbeanServer.registerMBean(mbean, objectName);
		} catch (Exception e) {
			logger
					.warn(
							String
									.format(
											"caught exception while registering MBean %s on MBean server %s",
											objectName, mbeanServer), e);
		}
	}

	/**
	 * Removes a notification listener.
	 * <p>
	 * The notification listener is removed from all known MBean server
	 * connections.
	 *
	 * @param serviceReference
	 *            The service reference which provides the notification
	 *            listener.
	 *
	 */
	public synchronized void removeNotificationListener(
			ServiceReference serviceReference) {
		NotificationListenerRegistration notificationListenerRegistration = this.notificationListeners
				.remove(serviceReference);
		if (notificationListenerRegistration == null) {
			logger
					.warn(
							"notification listener from service reference '{}' is not registered.",
							serviceReference);
		} else {
			logger
					.debug(
							"removing notification listener from service reference '{}'",
							notificationListenerRegistration);
			for (MBeanServerConnectionRegistration mbeanServerConnectionRegistration : this.mbeanServerConnections
					.values()) {
				this.removeNotificationListener(
						mbeanServerConnectionRegistration
								.getMBeanServerConnection(),
						notificationListenerRegistration.getObjectName(),
						notificationListenerRegistration
								.getNotificationListener(),
						notificationListenerRegistration
								.getNotificationFilter(),
						notificationListenerRegistration.getHandback());
			}
		}
	}

	/**
	 * Unregisters an MBean server connection instance.
	 * <p>
	 * The MBean server connection is disassociated with all known notification
	 * listeners.
	 *
	 * @param serviceReference
	 *            The service reference which provides the MBean server
	 *            connection.
	 */
	public synchronized void unregisterMBeanServerConnection(
			ServiceReference serviceReference) {
		assert (this.mbeanServerConnections != null);
		MBeanServerConnectionRegistration mbeanServerConnectionRegistration = this.mbeanServerConnections
				.remove(serviceReference);
		if (mbeanServerConnectionRegistration == null) {
			logger
					.warn(
							"MBean server connection from service reference '{}' is not registered.",
							serviceReference);
		} else {
			logger
					.debug(
							"unregistering MBean server connection from service reference '{}'",
							mbeanServerConnectionRegistration);
			for (NotificationListenerRegistration notificationListenerRegistration : this.notificationListeners
					.values()) {
				this.removeNotificationListener(
						mbeanServerConnectionRegistration
								.getMBeanServerConnection(),
						notificationListenerRegistration.getObjectName(),
						notificationListenerRegistration
								.getNotificationListener(),
						notificationListenerRegistration
								.getNotificationFilter(),
						notificationListenerRegistration.getHandback());
			}
		}
	}

	/**
	 * Removes a notification listener from a single MBean server connection.
	 *
	 * @param mbeanServerConnection
	 *            The MBean server connection.
	 * @param objectName
	 *            The object name.
	 * @param notificationListener
	 *            The notification listener.
	 * @param notificationFilter
	 *            The notification filter.
	 * @param handback
	 *            The handback context object.
	 */
	private void removeNotificationListener(
			MBeanServerConnection mbeanServerConnection, ObjectName objectName,
			NotificationListener notificationListener,
			NotificationFilter notificationFilter, Object handback) {
		try {
			logger
					.trace(
							"removing notification listener {} from MBean server connection {}",
							notificationListener, mbeanServerConnection);
			mbeanServerConnection.removeNotificationListener(objectName,
					notificationListener, notificationFilter, handback);
		} catch (Exception e) {
			logger
					.warn(
							String
									.format(
											"caught exception while removing notification listener %s from MBean server conncetion %s",
											notificationListener,
											mbeanServerConnection), e);
		}
	}

	/**
	 * Unregisters an MBean.
	 * <p>
	 * The MBean is unregistered from all known MBean servers.
	 *
	 * @param serviceReference
	 *            The service reference which provides the MBean.
	 */
	public synchronized void unregisterMBean(ServiceReference serviceReference) {
		assert (this.mbeans != null);
		MBeanRegistration mbeanRegistration = this.mbeans
				.remove(serviceReference);
		if (mbeanRegistration == null) {
			logger.warn("MBean from service reference '{}' is not registered.",
					serviceReference);
		} else {
			logger.debug("unregistering MBean from service reference '{}'",
					mbeanRegistration);
			for (MBeanServerRegistration mbeanServerRegistration : this.mbeanServers
					.values()) {
				this.unregisterMBean(mbeanServerRegistration.getMBeanServer(),
						mbeanRegistration.getObjectName());
			}
		}
	}

	/**
	 * Unregisters an MBean server instance.
	 * <p>
	 * The MBean server instance is disassociated with all known MBeans.
	 *
	 * @param serviceReference
	 *            The service reference which provides the MBean server.
	 */
	public synchronized void unregisterMBeanServer(
			ServiceReference serviceReference) {
		assert (this.mbeanServers != null);
		MBeanServerRegistration mbeanServerRegistration = this.mbeanServers
				.remove(serviceReference);
		if (mbeanServerRegistration == null) {
			logger
					.warn(
							"MBean server from service reference '{}' is not registered.",
							serviceReference);
		} else {
			logger.warn(
					"unregistering MBean server from service reference '{}'",
					serviceReference);
			for (MBeanRegistration mbeanRegistration : this.mbeans.values()) {
				this.unregisterMBean(mbeanServerRegistration.getMBeanServer(),
						mbeanRegistration.getObjectName());
			}
		}
	}

	/**
	 * Unregisters an MBean from a single MBean server.
	 *
	 * @param mbeanServer
	 *            The MBean server.
	 * @param objectName
	 *            The object name.
	 */
	private void unregisterMBean(MBeanServer mbeanServer, ObjectName objectName) {
		try {
			logger.trace("unregistering MBean {} from server {}", objectName,
					mbeanServer);
			mbeanServer.unregisterMBean(objectName);
		} catch (Exception e) {
			logger
					.warn(
							String
									.format(
											"caught exception while unregistering MBean %s from MBean server %s",
											objectName, mbeanServer), e);
		}
	}
}
