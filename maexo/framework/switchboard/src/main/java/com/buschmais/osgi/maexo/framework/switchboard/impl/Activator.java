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

import javax.management.MBeanServer;
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

public class Activator implements BundleActivator {

	/**
	 * Definition of the filter for mbean servers
	 */
	private static final String FILTER_MBEANSERVER = "(objectClass="
			+ MBeanServer.class.getName() + ")";

	/**
	 * Definition of the filter for mbeans. It contains condition to only accept
	 * services which have a javax.management.ObjectName or objectName
	 * attribute.
	 */
	private static final String FILTER_MBEAN = "(&(objectClass=*MBean)(|("
			+ ObjectName.class.getName() + "=*)(objectName=*)))";

	/**
	 * Definition of the filter for notification listeners
	 */
	private static final String FILTER_NOTIFICATIONLISTENER = "(&(objectClass="
			+ NotificationListener.class.getName() + ")(|("
			+ ObjectName.class.getName() + "=*)(objectName=*)))";

	private static Logger logger = LoggerFactory.getLogger(Activator.class);

	private SwitchBoardImpl mbeanExporter;

	private ServiceListener mbeanServerServiceListener;

	private ServiceListener mbeanServiceListener;

	private ServiceListener notificationListenerServiceListener;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("Starting maexo switch board");
		}
		this.mbeanExporter = new SwitchBoardImpl();
		this.mbeanServerServiceListener = this
				.registerMBeanServerServiceListener(bundleContext);
		this.mbeanServiceListener = this
				.registerMBeanServiceListener(bundleContext);
		this.notificationListenerServiceListener = this
				.registerNotificationListenerServiceListener(bundleContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("Stopping maexo switch board");
		}
		// remove service listener for mbean servers and clean up
		if (this.mbeanServerServiceListener != null) {
			bundleContext
					.removeServiceListener(this.mbeanServerServiceListener);
		}
		for (MBeanServerRegistration mbeanServerRegistration : this.mbeanExporter
				.getMBeanServers()) {
			this.mbeanExporter.unregisterMBeanServer(mbeanServerRegistration);
		}
		// remove service listener for mbeans and clean up
		if (this.mbeanServiceListener != null) {
			bundleContext.removeServiceListener(this.mbeanServiceListener);
		}
		for (MBeanRegistration mbeanRegistration : this.mbeanExporter
				.getMBeans()) {
			this.mbeanExporter.unregisterMBean(mbeanRegistration);
		}
		// remove service listener for mbeans and clean up
		if (this.notificationListenerServiceListener != null) {
			bundleContext
					.removeServiceListener(this.notificationListenerServiceListener);
		}
		for (NotificationListenerRegistration notificationListenerRegistration : this.mbeanExporter
				.getNotificationListeners()) {
			this.mbeanExporter
					.removeNotificationListener(notificationListenerRegistration);
		}
	}

	/**
	 * Registers a service listener for MBean servers
	 * 
	 * @param bundleContext
	 *            the bundle context
	 * @return the service listener
	 * @throws InvalidSyntaxException
	 */
	private ServiceListener registerMBeanServerServiceListener(
			final BundleContext bundleContext) throws InvalidSyntaxException {
		ServiceListener mbeanServerServiceListener = new ServiceListener() {

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
				case ServiceEvent.REGISTERED: {
					Activator.this.mbeanExporter
							.registerMBeanServer(mBeanServerRegistration);
				}
					break;
				case ServiceEvent.UNREGISTERING: {
					Activator.this.mbeanExporter
							.unregisterMBeanServer(mBeanServerRegistration);
				}
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
	 * Registers a service listener for MBeans
	 * 
	 * @param bundleContext
	 *            the bundle context
	 * @return the service listener
	 * @throws InvalidSyntaxException
	 */
	private ServiceListener registerMBeanServiceListener(
			final BundleContext bundleContext) throws InvalidSyntaxException {
		ServiceListener mbeanServiceListener = new ServiceListener() {

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
					case ServiceEvent.REGISTERED: {
						Activator.this.mbeanExporter
								.registerMBean(mbeanRegistration);
					}
						break;
					case ServiceEvent.UNREGISTERING: {
						Activator.this.mbeanExporter
								.unregisterMBean(mbeanRegistration);
					}
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
	 * Registers a service listener for notification listeners
	 * 
	 * @param bundleContext
	 *            the bundle context
	 * @return the service listener
	 * @throws InvalidSyntaxException
	 */
	private ServiceListener registerNotificationListenerServiceListener(
			final BundleContext bundleContext) throws InvalidSyntaxException {
		ServiceListener notificationListenerServiceListener = new ServiceListener() {

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
					case ServiceEvent.REGISTERED: {
						Activator.this.mbeanExporter
								.addNotificationListener(notificationListenerRegistration);
					}
						break;
					case ServiceEvent.UNREGISTERING: {
						Activator.this.mbeanExporter
								.removeNotificationListener(notificationListenerRegistration);
					}
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
	 * Registers existing already services that match on a service class and
	 * filter using a service listener
	 * 
	 * @param serviceClass
	 *            the service class
	 * @param filter
	 *            the filter
	 * @param bundleContext
	 *            the bundle context
	 * @param serviceListener
	 *            the service listener
	 * @throws InvalidSyntaxException
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
