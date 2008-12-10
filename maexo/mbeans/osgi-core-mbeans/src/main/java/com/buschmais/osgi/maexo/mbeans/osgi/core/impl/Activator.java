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

import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameHelper;
import com.buschmais.osgi.maexo.mbeans.osgi.core.impl.listener.BundleEventListener;
import com.buschmais.osgi.maexo.mbeans.osgi.core.impl.listener.ServiceEventListener;
import com.buschmais.osgi.maexo.mbeans.osgi.core.impl.objectname.BundleObjectNameFactory;
import com.buschmais.osgi.maexo.mbeans.osgi.core.impl.objectname.ServiceObjectNameFactory;

public class Activator implements BundleActivator {

	private List<ServiceRegistration> serviceRegistrations;

	private BundleListener bundleListener;

	private ServiceListener serviceListener;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(final BundleContext bundleContext) throws Exception {
		ObjectNameHelper objectNameHelper = new ObjectNameHelper(bundleContext);
		this.serviceRegistrations = new LinkedList<ServiceRegistration>();
		this.serviceRegistrations.add(objectNameHelper
				.registerObjectNameFactory(new BundleObjectNameFactory(),
						Bundle.class));
		this.serviceRegistrations.add(objectNameHelper
				.registerObjectNameFactory(new ServiceObjectNameFactory(),
						ServiceReference.class));
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
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
		// unregister services
		for (ServiceRegistration serviceRegistration : this.serviceRegistrations) {
			serviceRegistration.unregister();
		}
	}

}
