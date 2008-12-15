package com.buschmais.osgi.maexo.framework.mbeanexporter.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MBeanExporterImpl {

	private static Logger logger = LoggerFactory
			.getLogger(MBeanExporterImpl.class);

	private Set<MBeanServerRegistration> mbeanServers = new HashSet<MBeanServerRegistration>();

	private Set<MBeanRegistration> mbeans = new HashSet<MBeanRegistration>();

	private Set<NotificationListenerRegistration> notificationListeners = new HashSet<NotificationListenerRegistration>();

	/**
	 * Adds a notification listener on a mbean server
	 * 
	 * @param mbeanServer
	 *            the mbean server
	 * @param objectName
	 *            the object name
	 * @param notificationListener
	 *            the notification listener
	 * @param handback
	 *            the handback object
	 */
	private void addNotificationListener(MBeanServer mbeanServer,
			ObjectName objectName, NotificationListener notificationListener,
			NotificationFilter notificationFilter, Object handback) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("adding notification listener "
						+ notificationListener + " on server " + mbeanServer);
			}
			mbeanServer.addNotificationListener(objectName,
					notificationListener, notificationFilter, handback);
		} catch (Exception e) {
			logger.warn(
					"caught exception while adding notification listener "
							+ notificationListener + ", on mbean server "
							+ mbeanServer, e);
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
			for (MBeanServerRegistration mbeanServerRegistration : this.mbeanServers) {
				this.addNotificationListener(mbeanServerRegistration
						.getMbeanServer(), notificationListenerRegistration
						.getObjectName(), notificationListenerRegistration
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
	 * @param mbeanServer
	 *            the mbean server
	 * @param objectName
	 *            the object name
	 * @param notificationListener
	 *            the notification listener
	 * @param notificationFilter
	 *            the notification filter
	 * @param handback
	 *            the handback object
	 */
	private void removeNotificationListener(MBeanServer mbeanServer,
			ObjectName objectName, NotificationListener notificationListener,
			NotificationFilter notificationFilter, Object handback) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("removing notification listener "
						+ notificationListener + " from server " + mbeanServer);
			}
			mbeanServer.removeNotificationListener(objectName,
					notificationListener, notificationFilter, handback);
		} catch (Exception e) {
			logger.warn(
					"caught exception while removing notification listener "
							+ notificationListener + ", from mbean server "
							+ mbeanServer, e);
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
			for (MBeanServerRegistration mbeanServerRegistration : this.mbeanServers) {
				this.removeNotificationListener(mbeanServerRegistration
						.getMbeanServer(), notificationListenerRegistration
						.getObjectName(), notificationListenerRegistration
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