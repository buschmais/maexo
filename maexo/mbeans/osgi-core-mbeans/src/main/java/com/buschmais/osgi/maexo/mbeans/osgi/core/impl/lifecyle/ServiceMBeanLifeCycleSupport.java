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
package com.buschmais.osgi.maexo.mbeans.osgi.core.impl.lifecyle;

import javax.management.ObjectName;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import com.buschmais.osgi.maexo.framework.commons.mbean.lifecycle.MBeanLifecycleSupport;
import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameFactory;
import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameHelper;

/**
 * This class represents an abstract service event listener to manage the
 * lifecycle of the associated service mbeans.
 */
public abstract class ServiceMBeanLifeCycleSupport extends
		MBeanLifecycleSupport implements ServiceListener {

	public ServiceMBeanLifeCycleSupport(BundleContext bundleContext) {
		super(bundleContext);
	}

	/**
	 * Starts the life cycle listener. The existing services are looked up,
	 * published as mbeans and the lifecyle listener is registered with the
	 * bundle context.
	 * 
	 * @throws InvalidSyntaxException
	 */
	public void start() throws InvalidSyntaxException {
		// register all existing services as mbeans
		ServiceReference[] serviceReferences = this.getServices();
		if (serviceReferences != null) {
			for (ServiceReference serviceReference : serviceReferences) {
				this.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED,
						serviceReference));
			}
		}
		// add the service listener
		super.getBundleContext().addServiceListener(this);
	}

	/**
	 * Stops the life cycle listener. The existing services are looked up, their
	 * mbeans get unpublished and the lifecyle listener is unregistered from the
	 * bundle context.
	 * 
	 * @throws InvalidSyntaxException
	 */
	public void stop() throws InvalidSyntaxException {
		// remove service listener
		super.getBundleContext().removeServiceListener(this);
		// unregister all registered service mbeans
		ServiceReference[] serviceReferences = this.getServices();
		if (serviceReferences != null) {
			for (ServiceReference serviceReference : serviceReferences) {
				this.serviceChanged(new ServiceEvent(
						ServiceEvent.UNREGISTERING, serviceReference));
			}
		}
	}

	/**
	 * Returns the currently registered service references for the interface
	 * provided by {@link #getServiceInterface()}
	 * 
	 * @return the service references
	 * @throws InvalidSyntaxException
	 */
	private ServiceReference[] getServices() throws InvalidSyntaxException {
		Class<?> serviceInterface = this.getServiceInterface();
		String serviceInterfaceName = serviceInterface == null ? null
				: serviceInterface.getName();
		return super.getBundleContext().getServiceReferences(
				serviceInterfaceName, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework.
	 * ServiceEvent)
	 */
	public final synchronized void serviceChanged(ServiceEvent serviceEvent) {
		ServiceReference serviceReference = serviceEvent.getServiceReference();
		if (this.isManageable(serviceReference)) {
			Object service = this.getService(serviceReference);
			ObjectName objectName = this.getObjectName(service);
			switch (serviceEvent.getType()) {
			case ServiceEvent.REGISTERED: {
				super.registerMBeanService(this.getMBeanInterface(),
						objectName, this.getMBean(service));
			}
				break;
			case ServiceEvent.UNREGISTERING: {
				try {
					super.unregisterMBeanService(objectName);
				} finally {
					this.ungetService(serviceReference);
				}
			}
				break;
			}
		}
	}

	/**
	 * Returns true if the given service reference can be managed by a MBean.
	 * The default implementation returns true.
	 * 
	 * @param serviceReference
	 *            the service reference
	 * @return true if a MBean can be provided
	 */
	public boolean isManageable(ServiceReference serviceReference) {
		return true;
	}

	/**
	 * Returns the service instance. The default implementation uses the object
	 * provided by the service reference.
	 * 
	 * @param serviceReference
	 *            the service reference
	 * @return the service instance
	 */
	public Object getService(ServiceReference serviceReference) {
		return super.getBundleContext().getService(serviceReference);
	}

	/**
	 * Releases the service reference. The default implementation uses
	 * {@link BundleContext#ungetService(ServiceReference)}.
	 * 
	 * @param serviceReference
	 *            the service reference
	 */
	public void ungetService(ServiceReference serviceReference) {
		super.getBundleContext().ungetService(serviceReference);
	}

	/**
	 * Returns the service interface supported by the lifecycle event listener.
	 * 
	 * @return the service interface
	 */
	public abstract Class<?> getServiceInterface();

	/**
	 * Returns the object name of service which will be used for the mbean. The
	 * default implementation delegates to the @link {@link ObjectNameHelper}
	 * which will lookup a @link {@link ObjectNameFactory}.
	 * 
	 * @param service
	 *            the service
	 * @return the object name
	 */
	public ObjectName getObjectName(Object service) {
		return super.getObjectNameHelper().getObjectName(service,
				this.getServiceInterface());
	}

	/**
	 * Returns the mbean instance for the given service.
	 * 
	 * @param service
	 *            the service
	 * @return the mbean instance
	 */
	public abstract Object getMBean(Object service);

	/**
	 * Returns the mbean interface which will be used for registration
	 * 
	 * @return the mbean interface
	 */
	public abstract Class<?> getMBeanInterface();

}
