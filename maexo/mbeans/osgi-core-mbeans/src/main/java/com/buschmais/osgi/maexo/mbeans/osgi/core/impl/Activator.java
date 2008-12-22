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
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.startlevel.StartLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameHelper;
import com.buschmais.osgi.maexo.mbeans.osgi.core.impl.listener.BundleEventListener;
import com.buschmais.osgi.maexo.mbeans.osgi.core.impl.listener.ServiceEventListener;
import com.buschmais.osgi.maexo.mbeans.osgi.core.impl.listener.StartLevelEventListener;
import com.buschmais.osgi.maexo.mbeans.osgi.core.impl.objectname.BundleObjectNameFactory;
import com.buschmais.osgi.maexo.mbeans.osgi.core.impl.objectname.ServiceObjectNameFactory;
import com.buschmais.osgi.maexo.mbeans.osgi.core.impl.objectname.StartLevelObjectNameFactory;

public class Activator implements BundleActivator {

	private static final Logger logger = LoggerFactory
			.getLogger(Activator.class);

	private List<ServiceRegistration> serviceRegistrations;

	private BundleListener bundleListener;

	private ServiceListener serviceListener;

	private ServiceListener startLevelServiceListener;

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
		// create bundle listener
		this.bundleListener = new BundleEventListener(bundleContext);
		// register all existing bundles as mbeans
		for (Bundle bundle : bundleContext.getBundles()) {
			this.bundleListener.bundleChanged(new BundleEvent(
					BundleEvent.INSTALLED, bundle));
		}
		bundleContext.addBundleListener(this.bundleListener);

		// create service listener
		this.serviceListener = new ServiceEventListener(bundleContext);
		// register all existing services as mbeans
		for (ServiceReference serviceReference : bundleContext
				.getServiceReferences(null, null)) {
			this.serviceListener.serviceChanged(new ServiceEvent(
					ServiceEvent.REGISTERED, serviceReference));
		}
		bundleContext.addServiceListener(this.serviceListener);

		// create start level service listener
		this.startLevelServiceListener = new StartLevelEventListener(
				bundleContext);
		// register all existing services as mbeans
		for (ServiceReference serviceReference : bundleContext
				.getServiceReferences(
						org.osgi.service.startlevel.StartLevel.class.getName(),
						null)) {
			this.startLevelServiceListener.serviceChanged(new ServiceEvent(
					ServiceEvent.REGISTERED, serviceReference));
		}
		bundleContext.addServiceListener(this.startLevelServiceListener);
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
		bundleContext.removeBundleListener(this.bundleListener);
		// unregister all registered bundle mbeans
		for (Bundle bundle : bundleContext.getBundles()) {
			this.bundleListener.bundleChanged(new BundleEvent(
					BundleEvent.UNINSTALLED, bundle));
		}

		// remove service listener
		bundleContext.removeServiceListener(this.serviceListener);
		// unregister all registered service mbeans
		for (ServiceReference serviceReference : bundleContext
				.getServiceReferences(null, null)) {
			this.serviceListener.serviceChanged(new ServiceEvent(
					ServiceEvent.UNREGISTERING, serviceReference));
		}

		// remove service listener
		bundleContext.removeServiceListener(this.startLevelServiceListener);
		// unregister all registered service mbeans
		for (ServiceReference serviceReference : bundleContext
				.getServiceReferences(
						org.osgi.service.startlevel.StartLevel.class.getName(),
						null)) {
			this.startLevelServiceListener.serviceChanged(new ServiceEvent(
					ServiceEvent.UNREGISTERING, serviceReference));
		}

		// unregister services
		for (ServiceRegistration serviceRegistration : this.serviceRegistrations) {
			serviceRegistration.unregister();
		}
	}

}
