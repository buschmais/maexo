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
package com.buschmais.osgi.maexo.framework.commons.mbean.lifecycle;

import java.util.HashMap;
import java.util.Map;

import javax.management.ObjectName;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

/**
 * Represents an abstract service event listener to manage the life cycle of the
 * associated service mbeans.
 * <p>
 * The <code>ServiceMBeanLifeCycleSupport</code> instance will be registered as
 * a service listener to receive service-changed-events.
 */
public abstract class ServiceMBeanLifeCycleSupport extends
		MBeanLifecycleSupport implements ServiceListener {

	/**
	 * Constructor.
	 * 
	 * @param bundleContext
	 *            the bundle context of the exporting bundle
	 */
	public ServiceMBeanLifeCycleSupport(BundleContext bundleContext) {
		super(bundleContext);
	}

	/**
	 * Starts the life cycle listener.
	 * <p>
	 * The existing services are looked up to be published as mbeans. Later the
	 * life cycle listener is registered with the bundle context.
	 * 
	 * //TODO@DM: change thrown ExceptionType
	 * 
	 * @throws InvalidSyntaxException
	 */
	public final void start() throws InvalidSyntaxException {
		// register all existing services as mbeans
		ServiceReference[] serviceReferences = this.getServices();
		if (serviceReferences != null) {
			for (ServiceReference serviceReference : serviceReferences) {
				this.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED,
						serviceReference));
			}
		}
		// add the service listener
		super.getBundleContext().addServiceListener(this,
				this.getServiceFilter());
	}

	/**
	 * Stops the life cycle listener.
	 * <p>
	 * The existing services are looked up, their mbeans get unpublished and the
	 * life cycle listener is unregistered from the bundle context.
	 * 
	 * //TODO@DM: change thrown ExceptionType
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
	 * provided by {@link #getServiceInterface()}.
	 * 
	 * @return the service references
	 * 
	 *         // TODO@DM: change thrown ExceptionType
	 * @throws InvalidSyntaxException
	 */
	private ServiceReference[] getServices() throws InvalidSyntaxException {
		return super.getBundleContext().getServiceReferences(null,
				this.getServiceFilter());
	}

	/**
	 * Returns a filter representing using the service interface as object
	 * class.
	 * 
	 * @return the filter or <code>null</code> if there is no service interface
	 *         declared
	 */
	private String getServiceFilter() {
		Class<?> serviceInterface = this.getServiceInterface();
		if (serviceInterface == null) {
			return null;
		}
		return ("(objectClass=" + serviceInterface.getName() + ")");
	}


	/**
	 * {@inheritDoc}
	 */
	public final synchronized void serviceChanged(ServiceEvent serviceEvent) {
		ServiceReference serviceReference = serviceEvent.getServiceReference();
		if (this.isManageable(serviceReference)) {
			Object service = this.getService(serviceReference);
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(Constants.SERVICE_ID, serviceReference
					.getProperty(Constants.SERVICE_ID));
			ObjectName objectName = this.getObjectName(service, properties);
			switch (serviceEvent.getType()) {
			case ServiceEvent.REGISTERED:
				super.registerMBeanService(this.getMBeanInterface(),
						objectName, this.getMBean(service));
				break;
			case ServiceEvent.UNREGISTERING:
				try {
					super.unregisterMBeanService(objectName);
				} finally {
					this.ungetService(serviceReference);
				}
				break;
			default:
				assert false : "Unexpected ServiceEvent";
			}
		}
	}

	/**
	 * Returns true if the given service reference can be managed by a MBean.
	 * <p>
	 * The default implementation returns true.
	 * 
	 * @param serviceReference
	 *            the service reference
	 * @return true if a mbean can be provided
	 */
	public boolean isManageable(ServiceReference serviceReference) {
		return true;
	}

	/**
	 * Returns the service instance.
	 * <p>
	 * The default implementation uses the object provided by the service
	 * reference.
	 * 
	 * @param serviceReference
	 *            the service reference
	 * @return the service instance
	 */
	public Object getService(ServiceReference serviceReference) {
		return super.getBundleContext().getService(serviceReference);
	}

	/**
	 * Releases the service reference.
	 * <p>
	 * The default implementation uses
	 * {@link BundleContext#ungetService(ServiceReference)}.
	 * 
	 * @param serviceReference
	 *            the service reference
	 */
	public final void ungetService(ServiceReference serviceReference) {
		super.getBundleContext().ungetService(serviceReference);
	}

	/**
	 * Returns the service interface supported by the life cycle event listener.
	 * 
	 * @return the service interface
	 */
	public abstract Class<?> getServiceInterface();

	/**
	 * Returns the object name of service which will be used for the managed
	 * bean.
	 * <p>
	 * The default implementation delegates to the {@link ObjectNameHelper}
	 * which will lookup an appropriate {@link ObjectNameFactory} to eventually
	 * construct the object name.
	 * 
	 * @param service
	 *            the service
	 * @param properties
	 *            additional properties which may be required to construct
	 *            unique object names
	 * @return the object name
	 */
	public ObjectName getObjectName(Object service,
			Map<String, Object> properties) {
		return super.getObjectNameHelper().getObjectName(service,
				this.getServiceInterface(), properties);
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
	 * Returns the mbean interface which will be used for registration.
	 * 
	 * @return the mbean interface
	 */
	public abstract Class<?> getMBeanInterface();

}
