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

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OSGi bundle activator for the switchboard bundle.
 */
public final class Activator implements BundleActivator {

	/**
	 * Filter for mbean server connections.
	 */
	private static final String FILTER_MBEANSERVERCONNECTION = String.format(
			"(|(objectClass=%s)(objectClass=%s))", MBeanServerConnection.class
					.getName(), MBeanServer.class.getName());

	/**
	 * Filter for mbean servers.
	 */
	private static final String FILTER_MBEANSERVER = String.format(
			"(objectClass=%s)", MBeanServer.class.getName());

	/**
	 * Filter for mbeans with either a <code>javax.management.ObjectName</code>
	 * or objectName attribute.
	 */
	private static final String FILTER_MBEAN = String.format(
			"(&(objectClass=*MBean)(|(%s=*)(objectName=*)))", ObjectName.class
					.getName());

	/**
	 * Filter for notification listeners.
	 */
	private static final String FILTER_NOTIFICATIONLISTENER = String.format(
			"(&(objectClass=%s)(|(%s=*)(objectName=*)))",
			NotificationListener.class.getName(), ObjectName.class.getName());

	private static Logger logger = LoggerFactory.getLogger(Activator.class);

	private SwitchBoardImpl switchBoard;

	private ServiceListener mbeanServerConnectionServiceListener;

	private ServiceListener mbeanServerServiceListener;

	private ServiceListener mbeanServiceListener;

	private ServiceListener notificationListenerServiceListener;

	/**
	 * {@inheritDoc}
	 */
	public void start(BundleContext bundleContext) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("Starting maexo switch board");
		}
		this.switchBoard = new SwitchBoardImpl();
		this.mbeanServerConnectionServiceListener = this
				.registerMBeanServerConnectionServiceListener(bundleContext);
		this.mbeanServerServiceListener = this
				.registerMBeanServerServiceListener(bundleContext);
		this.mbeanServiceListener = this
				.registerMBeanServiceListener(bundleContext);
		this.notificationListenerServiceListener = this
				.registerNotificationListenerServiceListener(bundleContext);
	}

	/**
	 * {@inheritDoc}
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("Stopping maexo switch board");
		}
		// remove service listener for mbean server connections s and clean up
		if (this.mbeanServerConnectionServiceListener != null) {
			bundleContext
					.removeServiceListener(this.mbeanServerConnectionServiceListener);
		}
		for (MBeanServerConnectionRegistration mbeanServerConnectionRegistration : this.switchBoard
				.getMBeanServerConnections()) {
			this.switchBoard
					.unregisterMBeanServerConnection(mbeanServerConnectionRegistration);
		}
		// remove service listener for mbean servers and clean up
		if (this.mbeanServerServiceListener != null) {
			bundleContext
					.removeServiceListener(this.mbeanServerServiceListener);
		}
		for (MBeanServerRegistration mbeanServerRegistration : this.switchBoard
				.getMBeanServers()) {
			this.switchBoard.unregisterMBeanServer(mbeanServerRegistration);
		}
		// remove service listener for mbeans and clean up
		if (this.mbeanServiceListener != null) {
			bundleContext.removeServiceListener(this.mbeanServiceListener);
		}
		for (MBeanRegistration mbeanRegistration : this.switchBoard.getMBeans()) {
			this.switchBoard.unregisterMBean(mbeanRegistration);
		}
		// remove service listener for mbeans and clean up
		if (this.notificationListenerServiceListener != null) {
			bundleContext
					.removeServiceListener(this.notificationListenerServiceListener);
		}
		for (NotificationListenerRegistration notificationListenerRegistration : this.switchBoard
				.getNotificationListeners()) {
			this.switchBoard
					.removeNotificationListener(notificationListenerRegistration);
		}
	}

	/**
	 * Registers a service listener for MBean server connections.
	 * 
	 * @param bundleContext
	 *            the bundle context
	 * @return the service listener
	 * @throws InvalidSyntaxException
	 *             if the registering fails
	 */
	private ServiceListener registerMBeanServerConnectionServiceListener(
			final BundleContext bundleContext) throws InvalidSyntaxException {
		mbeanServerConnectionServiceListener = new ServiceListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework
			 * .ServiceEvent)
			 */
			public void serviceChanged(ServiceEvent serviceEvent) {
				ServiceReference serviceReference = serviceEvent
						.getServiceReference();
				MBeanServerConnectionRegistration mbeanServerConnectionRegistration = new MBeanServerConnectionRegistration(
						bundleContext, serviceReference);
				switch (serviceEvent.getType()) {
				case ServiceEvent.REGISTERED:
					Activator.this.switchBoard
							.registerMBeanServerConnection(mbeanServerConnectionRegistration);
					break;
				case ServiceEvent.UNREGISTERING:
					Activator.this.switchBoard
							.unregisterMBeanServerConnection(mbeanServerConnectionRegistration);
					break;
				default:
					break;
				}
			}

		};
		bundleContext.addServiceListener(mbeanServerConnectionServiceListener,
				FILTER_MBEANSERVERCONNECTION);
		// do initial registration of MBeanServerConnections
		if (logger.isDebugEnabled()) {
			logger
					.debug("performing initial registration of mbean server connections");
		}
		this.registerExistingServices(FILTER_MBEANSERVERCONNECTION,
				bundleContext, mbeanServerConnectionServiceListener);
		return mbeanServerConnectionServiceListener;
	}

	/**
	 * Registers a service listener for mbean servers.
	 * 
	 * @param bundleContext
	 *            the bundle context
	 * @return the service listener
	 * @throws InvalidSyntaxException
	 *             if the registering fails
	 */
	private ServiceListener registerMBeanServerServiceListener(
			final BundleContext bundleContext) throws InvalidSyntaxException {
		mbeanServerServiceListener = new ServiceListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework
			 * .ServiceEvent)
			 */
			public void serviceChanged(ServiceEvent serviceEvent) {
				ServiceReference serviceReference = serviceEvent
						.getServiceReference();
				MBeanServerRegistration mBeanServerRegistration = new MBeanServerRegistration(
						bundleContext, serviceReference);
				switch (serviceEvent.getType()) {
				case ServiceEvent.REGISTERED:
					Activator.this.switchBoard
							.registerMBeanServer(mBeanServerRegistration);
					break;
				case ServiceEvent.UNREGISTERING:
					Activator.this.switchBoard
							.unregisterMBeanServer(mBeanServerRegistration);
					break;
				default:
					break;
				}
			}

		};
		bundleContext.addServiceListener(mbeanServerServiceListener,
				FILTER_MBEANSERVER);
		// do initial registration of MBeanServers
		if (logger.isDebugEnabled()) {
			logger.debug("performing initial registration of mbean servers");
		}
		this.registerExistingServices(FILTER_MBEANSERVER, bundleContext,
				mbeanServerServiceListener);
		return mbeanServerServiceListener;
	}

	/**
	 * Registers a service listener for mbeans.
	 * 
	 * @param bundleContext
	 *            the bundle context
	 * @return the service listener
	 * @throws InvalidSyntaxException
	 *             if the registering fails
	 */
	private ServiceListener registerMBeanServiceListener(
			final BundleContext bundleContext) throws InvalidSyntaxException {
		mbeanServiceListener = new ServiceListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework
			 * .ServiceEvent)
			 */
			public void serviceChanged(ServiceEvent serviceEvent) {
				ServiceReference serviceReference = serviceEvent
						.getServiceReference();
				MBeanRegistration mbeanRegistration = null;
				try {
					mbeanRegistration = new MBeanRegistration(bundleContext,
							serviceReference);
				} catch (Exception e) {
					Activator.logger
							.warn(
									"cannot create mbean registration, skipping (un-)registration",
									e);
				}
				if (mbeanRegistration != null) {
					switch (serviceEvent.getType()) {
					case ServiceEvent.REGISTERED:
						Activator.this.switchBoard
								.registerMBean(mbeanRegistration);
						break;
					case ServiceEvent.UNREGISTERING:
						Activator.this.switchBoard
								.unregisterMBean(mbeanRegistration);
						break;
					default:
						break;
					}
				}
			}

		};
		bundleContext.addServiceListener(mbeanServiceListener, FILTER_MBEAN);
		// do initial registration of MBeans
		if (logger.isDebugEnabled()) {
			logger.debug("performing initial registration of mbeans");
		}
		this.registerExistingServices(FILTER_MBEAN, bundleContext,
				mbeanServiceListener);
		return mbeanServiceListener;
	}

	/**
	 * Registers a service listener for notification listeners.
	 * 
	 * @param bundleContext
	 *            the bundle context
	 * @return the service listener
	 * @throws InvalidSyntaxException
	 *             if the registering fails
	 */
	private ServiceListener registerNotificationListenerServiceListener(
			final BundleContext bundleContext) throws InvalidSyntaxException {
		notificationListenerServiceListener = new ServiceListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework
			 * .ServiceEvent)
			 */
			public void serviceChanged(ServiceEvent serviceEvent) {
				ServiceReference serviceReference = serviceEvent
						.getServiceReference();
				NotificationListenerRegistration notificationListenerRegistration = null;
				try {
					notificationListenerRegistration = new NotificationListenerRegistration(
							bundleContext, serviceReference);
				} catch (Exception e) {
					Activator.logger
							.warn(
									"cannot create notification listener registration, skipping add/remove",
									e);
				}
				if (notificationListenerRegistration != null) {
					switch (serviceEvent.getType()) {
					case ServiceEvent.REGISTERED:
						Activator.this.switchBoard
								.addNotificationListener(notificationListenerRegistration);
						break;
					case ServiceEvent.UNREGISTERING:
						Activator.this.switchBoard
								.removeNotificationListener(notificationListenerRegistration);
						break;
					default:
						break;
					}
				}
			}

		};
		bundleContext.addServiceListener(notificationListenerServiceListener,
				FILTER_NOTIFICATIONLISTENER);
		// do initial registration of MBeans
		if (logger.isDebugEnabled()) {
			logger
					.debug("performing initial registration of notification listeners");
		}
		this.registerExistingServices(FILTER_NOTIFICATIONLISTENER,
				bundleContext, notificationListenerServiceListener);
		return notificationListenerServiceListener;
	}

	/**
	 * Registers already existing services.
	 * 
	 * @param filter
	 *            the filter for the services to match
	 * @param bundleContext
	 *            the bundle context
	 * @param serviceListener
	 *            the service listener to effect the registration
	 * @throws InvalidSyntaxException
	 *             if the filter has a syntactical error
	 */
	private void registerExistingServices(String filter,
			BundleContext bundleContext, ServiceListener serviceListener)
			throws InvalidSyntaxException {
		ServiceReference[] serviceReferences = bundleContext
				.getServiceReferences(null, filter);
		if (serviceReferences != null) {
			for (ServiceReference serviceReference : serviceReferences) {
				if (serviceReference != null) {
					serviceListener.serviceChanged(new ServiceEvent(
							ServiceEvent.REGISTERED, serviceReference));
				}
			}
		}
	}
}
