package com.buschmais.osgi.maexo.server.platform.impl;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	private static Logger logger = LoggerFactory.getLogger(Activator.class);

	private ServiceRegistration mbeanServerRegistration;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("Starting maexo Platform MBean Server");
		}
		MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
		if (logger.isDebugEnabled())
			logger.debug("registering instance " + mbeanServer + " as service");
		this.mbeanServerRegistration = bundleContext.registerService(
				new String[] { MBeanServer.class.getName(),
						MBeanServerConnection.class.getName() }, mbeanServer,
				null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("Stopping maexo Platform MBean Server");
		}
		this.mbeanServerRegistration.unregister();
	}

}
