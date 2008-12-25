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
package com.buschmais.osgi.maexo.mbeans.osgi.core.impl;

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

import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameHelper;
import com.buschmais.osgi.maexo.mbeans.osgi.core.impl.lifecyle.BundleMBeanLifeCycle;
import com.buschmais.osgi.maexo.mbeans.osgi.core.impl.lifecyle.PackageAdminMBeanLifeCycle;
import com.buschmais.osgi.maexo.mbeans.osgi.core.impl.lifecyle.ServiceMBeanLifeCycle;
import com.buschmais.osgi.maexo.mbeans.osgi.core.impl.lifecyle.ServiceMBeanLifeCycleSupport;
import com.buschmais.osgi.maexo.mbeans.osgi.core.impl.lifecyle.StartLevelMBeanLifeCycle;
import com.buschmais.osgi.maexo.mbeans.osgi.core.impl.objectname.BundleObjectNameFactory;
import com.buschmais.osgi.maexo.mbeans.osgi.core.impl.objectname.PackageAdminObjectNameFactory;
import com.buschmais.osgi.maexo.mbeans.osgi.core.impl.objectname.ServiceObjectNameFactory;
import com.buschmais.osgi.maexo.mbeans.osgi.core.impl.objectname.StartLevelObjectNameFactory;

/**
 * Activator class for the OSGi Core MBeans
 */
public class Activator implements BundleActivator {

	private static final Logger logger = LoggerFactory
			.getLogger(Activator.class);

	private List<ServiceRegistration> serviceRegistrations;

	private BundleMBeanLifeCycle bundleLifecyle;

	private ServiceMBeanLifeCycleSupport serviceLifecycle;

	private ServiceMBeanLifeCycleSupport startLevelServiceLifecycle;

	private ServiceMBeanLifeCycleSupport packageAdminServiceLifecycle;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(final BundleContext bundleContext) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("Starting maexo OSGi Core MBeans");
		}
		ObjectNameHelper objectNameHelper = new ObjectNameHelper(bundleContext);
		this.serviceRegistrations = new LinkedList<ServiceRegistration>();
		this.serviceRegistrations.add(objectNameHelper
				.registerObjectNameFactory(new BundleObjectNameFactory(),
						Bundle.class));
		this.serviceRegistrations.add(objectNameHelper
				.registerObjectNameFactory(new ServiceObjectNameFactory(),
						ServiceReference.class));
		this.serviceRegistrations.add(objectNameHelper
				.registerObjectNameFactory(new StartLevelObjectNameFactory(),
						StartLevel.class));
		this.serviceRegistrations.add(objectNameHelper
				.registerObjectNameFactory(new PackageAdminObjectNameFactory(),
						PackageAdmin.class));

		// create bundle listener
		this.bundleLifecyle = new BundleMBeanLifeCycle(bundleContext);
		this.bundleLifecyle.start();

		// create service listener
		this.serviceLifecycle = new ServiceMBeanLifeCycle(bundleContext);
		this.serviceLifecycle.start();

		// create start level service listener
		this.startLevelServiceLifecycle = new StartLevelMBeanLifeCycle(
				bundleContext);
		this.startLevelServiceLifecycle.start();

		// create package admin service listener
		this.packageAdminServiceLifecycle = new PackageAdminMBeanLifeCycle(
				bundleContext);
		this.packageAdminServiceLifecycle.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("Stopping maexo OSGi Core MBeans");
		}

		// remove bundle listener
		this.bundleLifecyle.stop();

		// remove service listener
		this.serviceLifecycle.stop();

		// remove start level service listener
		this.startLevelServiceLifecycle.stop();

		// remove package admin service listener
		this.packageAdminServiceLifecycle.stop();

		// unregister services
		for (ServiceRegistration serviceRegistration : this.serviceRegistrations) {
			serviceRegistration.unregister();
		}
	}
}
