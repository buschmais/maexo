package com.buschmais.osgi.maexo.framework.mbeanexporter.impl;

import javax.management.MBeanServer;
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
	 * The object name property
	 */
	private static final String SERVICE_PROPERTY_OBJECTNAME = "objectName";

	private static Logger logger = LoggerFactory.getLogger(Activator.class);

	private MBeanExporterImpl registry;

	private ServiceListener mbeanServerServiceListener;

	private ServiceListener mbeanServiceListener;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("Starting maexo MBean Exporter");
		}
		this.registry = new MBeanExporterImpl();
		this.mbeanServerServiceListener = this
				.registerMBeanServerServiceListener(bundleContext);
		this.mbeanServiceListener = this
				.registerMBeanServiceListener(bundleContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("Stopping maexo MBean Exporter");
		}
		// remove service listener for mbean servers and clean up
		if (this.mbeanServerServiceListener != null) {
			bundleContext
					.removeServiceListener(this.mbeanServerServiceListener);
		}
		for (MBeanServer mbeanServer : this.registry.getMBeanServers()) {
			this.registry.unregisterMBeanServer(mbeanServer);
		}
		// remove service listener for mbeans and clean up
		if (this.mbeanServiceListener != null) {
			bundleContext.removeServiceListener(this.mbeanServiceListener);
		}
		for (ObjectName objectName : this.registry.getMBeans().keySet()) {
			this.registry.unregisterMBean(objectName);
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
				MBeanServer mbeanServer = (MBeanServer) bundleContext
						.getService(serviceReference);
				switch (serviceEvent.getType()) {
				case ServiceEvent.REGISTERED: {
					Activator.this.registry.registerMBeanServer(mbeanServer);
				}
					break;
				case ServiceEvent.UNREGISTERING: {
					Activator.this.registry.unregisterMBeanServer(mbeanServer);
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
				// get object name from service properties
				ObjectName objectName = (ObjectName) serviceReference
						.getProperty(ObjectName.class.getName());
				String name = (String) serviceReference
						.getProperty(SERVICE_PROPERTY_OBJECTNAME);
				if (objectName == null && name != null) {
					// if no object name is available try to construct it from
					// given name
					try {
						objectName = new ObjectName(name);
					} catch (Exception e) {
						Activator.logger
								.error("cannot create ObjectName instance from \""
										+ name
										+ "\", skipping mbean (un-)registration");
					}
				}
				if (objectName != null) {
					Object mbean = bundleContext.getService(serviceReference);
					// populate event to registry
					switch (serviceEvent.getType()) {
					case ServiceEvent.REGISTERED: {
						Activator.this.registry
								.registerMBean(objectName, mbean);
					}
						break;
					case ServiceEvent.UNREGISTERING: {
						Activator.this.registry.unregisterMBean(objectName);
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
