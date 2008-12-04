package com.buschmais.osgi.maexo.mbeans.framework.impl.listener;

import javax.management.DynamicMBean;
import javax.management.ObjectName;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import com.buschmais.osgi.maexo.mbeans.framework.ServiceMBean;

public class ServiceEventListener extends EventListener implements
		ServiceListener {

	private Bundle bundle;

	public ServiceEventListener(BundleContext bundleContext) {
		super(bundleContext);
		this.bundle = bundleContext.getBundle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework.
	 * ServiceEvent)
	 */
	public synchronized void serviceChanged(ServiceEvent serviceEvent) {
		ServiceReference serviceReference = serviceEvent.getServiceReference();
		// do not process (mbean) services which have been registered by this bundle
		if (!serviceReference.getBundle().equals(this.bundle)) {
			Long id = (Long) serviceReference.getProperty(Constants.SERVICE_ID);
			switch (serviceEvent.getType()) {
			case ServiceEvent.REGISTERED: {
				ObjectName objectName = super
						.getObjectNameHelper()
						.getObjectName(serviceReference, ServiceReference.class);
				super.registerMBeanService(DynamicMBean.class, objectName, id,
						new ServiceMBean(serviceReference, super
								.getObjectNameHelper()));
			}
				break;
			case ServiceEvent.UNREGISTERING: {
				super.unregisterMBeanService(id);
			}
				break;
			}
		}
	}

}
