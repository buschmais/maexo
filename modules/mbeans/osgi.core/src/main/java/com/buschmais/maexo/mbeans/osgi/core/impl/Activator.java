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
package com.buschmais.maexo.mbeans.osgi.core.impl;

import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.startlevel.StartLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.maexo.framework.commons.mbean.lifecycle.ServiceMBeanLifeCycleSupport;
import com.buschmais.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;
import com.buschmais.maexo.mbeans.osgi.core.impl.lifecyle.BundleMBeanLifeCycle;
import com.buschmais.maexo.mbeans.osgi.core.impl.lifecyle.FrameworkMBeanLifeCycle;
import com.buschmais.maexo.mbeans.osgi.core.impl.lifecyle.PackageAdminMBeanLifeCycle;
import com.buschmais.maexo.mbeans.osgi.core.impl.lifecyle.ServiceMBeanLifeCycle;
import com.buschmais.maexo.mbeans.osgi.core.impl.lifecyle.StartLevelMBeanLifeCycle;
import com.buschmais.maexo.mbeans.osgi.core.impl.objectname.BundleObjectNameFactory;
import com.buschmais.maexo.mbeans.osgi.core.impl.objectname.FrameworkObjectNameFactory;
import com.buschmais.maexo.mbeans.osgi.core.impl.objectname.PackageAdminObjectNameFactory;
import com.buschmais.maexo.mbeans.osgi.core.impl.objectname.ServiceObjectNameFactory;
import com.buschmais.maexo.mbeans.osgi.core.impl.objectname.StartLevelObjectNameFactory;

/**
 * OSGi bundle activator for the the OSGi core mbeans bundle.
 */
public final class Activator implements BundleActivator {

	/** The logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(Activator.class);

	/** List of service registrations. */
	private List<ServiceRegistration> serviceRegistrations;

	/** The bundles life cycle. */
	private BundleMBeanLifeCycle bundleLifeCyle;

	/** The services life cycle. */
	private ServiceMBeanLifeCycleSupport serviceLifeCycle;

	/** The start level life cycle. */
	private ServiceMBeanLifeCycleSupport startLevelServiceLifeCycle;

	/** The package admin life cycle. */
	private ServiceMBeanLifeCycleSupport packageAdminServiceLifeCycle;
	
	/** The framework life cycle. */
	private FrameworkMBeanLifeCycle frameworkLifeCycle;


	/**
	 * {@inheritDoc}
	 */
	public void start(final BundleContext bundleContext) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("Starting maexo OSGi Core MBeans");
		}
		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(bundleContext);
		this.serviceRegistrations = new LinkedList<ServiceRegistration>();
		this.serviceRegistrations.add(objectNameFactoryHelper.registerObjectNameFactory(new BundleObjectNameFactory(), Bundle.class));
		this.serviceRegistrations.add(objectNameFactoryHelper.registerObjectNameFactory(new ServiceObjectNameFactory(), ServiceReference.class));
		this.serviceRegistrations.add(objectNameFactoryHelper.registerObjectNameFactory(new StartLevelObjectNameFactory(), StartLevel.class));
		this.serviceRegistrations.add(objectNameFactoryHelper.registerObjectNameFactory(new PackageAdminObjectNameFactory(), PackageAdmin.class));
		this.serviceRegistrations.add(objectNameFactoryHelper
				.registerObjectNameFactory(new FrameworkObjectNameFactory(),
						BundleContext.class));
		// create FrameworkMBean
		this.frameworkLifeCycle = new FrameworkMBeanLifeCycle(bundleContext);
		this.frameworkLifeCycle.start();
		// create bundle listener
		this.bundleLifeCyle = new BundleMBeanLifeCycle(bundleContext);
		this.bundleLifeCyle.start();

		// create service listener
		this.serviceLifeCycle = new ServiceMBeanLifeCycle(bundleContext);
		this.serviceLifeCycle.start();

		// create start level service listener
		this.startLevelServiceLifeCycle = new StartLevelMBeanLifeCycle(
				bundleContext);
		this.startLevelServiceLifeCycle.start();

		// create package admin service listener
		this.packageAdminServiceLifeCycle = new PackageAdminMBeanLifeCycle(
				bundleContext);
		this.packageAdminServiceLifeCycle.start();
	}


	/**
	 * {@inheritDoc}
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("Stopping maexo OSGi Core MBeans");
		}
		
		this.frameworkLifeCycle.stop();

		// remove bundle listener
		this.bundleLifeCyle.stop();

		// remove service listener
		this.serviceLifeCycle.stop();

		// remove start level service listener
		this.startLevelServiceLifeCycle.stop();

		// remove package admin service listener
		this.packageAdminServiceLifeCycle.stop();

		// unregister services
		for (ServiceRegistration serviceRegistration : this.serviceRegistrations) {
			serviceRegistration.unregister();
		}
	}
}
