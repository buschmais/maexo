/*
 * Copyright 2009 buschmais GbR
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
package com.buschmais.maexo.mbeans.osgi.compendium.impl;

import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.maexo.framework.commons.mbean.lifecycle.ServiceMBeanLifeCycleSupport;
import com.buschmais.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;
import com.buschmais.maexo.mbeans.osgi.compendium.impl.lifecycle.ConfigurationAdminMBeanLifeCycle;
import com.buschmais.maexo.mbeans.osgi.compendium.impl.objectname.ConfigurationAdminObjectNameFactory;
import com.buschmais.maexo.mbeans.osgi.compendium.impl.objectname.ConfigurationObjectNameFactory;

/**
 * OSGi bundle activator for the the OSGi compendium MBeans bundle.
 */
public final class Activator implements BundleActivator {

	/** The logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(Activator.class);

	/** List of service registrations. */
	private List<ServiceRegistration> serviceRegistrations;

	/** The configuration admin life cycle. */
	private ServiceMBeanLifeCycleSupport configurationAdminServiceLifeCycle;

	/**
	 * {@inheritDoc}
	 */
	public void start(final BundleContext bundleContext) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("Starting MAEXO OSGi Compendium MBeans");
		}
		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				bundleContext);
		this.serviceRegistrations = new LinkedList<ServiceRegistration>();
		this.serviceRegistrations.add(objectNameFactoryHelper
				.registerObjectNameFactory(
						new ConfigurationAdminObjectNameFactory(),
						ConfigurationAdmin.class));
		this.serviceRegistrations.add(objectNameFactoryHelper
				.registerObjectNameFactory(
						new ConfigurationObjectNameFactory(),
						Configuration.class));

		// create configuration admin service listener
		this.configurationAdminServiceLifeCycle = new ConfigurationAdminMBeanLifeCycle(
				bundleContext);
		this.configurationAdminServiceLifeCycle.start();
	}

	/**
	 * {@inheritDoc}
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("Stopping MAEXO OSGi Compendium MBeans");
		}

		this.configurationAdminServiceLifeCycle.stop();

		// unregister services
		for (ServiceRegistration serviceRegistration : this.serviceRegistrations) {
			serviceRegistration.unregister();
		}
	}
}
