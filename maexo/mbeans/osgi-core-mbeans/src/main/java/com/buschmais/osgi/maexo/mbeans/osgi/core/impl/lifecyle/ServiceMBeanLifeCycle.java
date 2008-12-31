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
package com.buschmais.osgi.maexo.mbeans.osgi.core.impl.lifecyle;

import java.util.Map;

import javax.management.DynamicMBean;
import javax.management.ObjectName;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.buschmais.osgi.maexo.framework.commons.mbean.lifecycle.ServiceMBeanLifeCycleSupport;
import com.buschmais.osgi.maexo.mbeans.osgi.core.Service;

/**
 * This class implements a service event listener to manage the life cycle of the
 * associated service mbeans.
 */
public final class ServiceMBeanLifeCycle extends ServiceMBeanLifeCycleSupport {

	/**
	 * Constructor.
	 * 
	 * @param bundleContext
	 *            the bundle context of the exporting bundle
	 */
	public ServiceMBeanLifeCycle(BundleContext bundleContext) {
		super(bundleContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.impl.lifecyle.
	 * ServiceMBeanLifeCycleSupport
	 * #getService(org.osgi.framework.ServiceReference)
	 */
	@Override
	public Object getService(ServiceReference serviceReference) {
		// the service reference itself will be managed by the mbean
		return serviceReference;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.impl.lifecyle.
	 * ServiceMBeanLifeCycleSupport#getMBean(java.lang.Object)
	 */
	@Override
	public Object getMBean(Object service) {
		return new Service(super.getBundleContext(), (ServiceReference) service);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.impl.lifecyle.
	 * ServiceMBeanLifeCycleSupport#getMBeanInterface()
	 */
	@Override
	public Class<?> getMBeanInterface() {
		return DynamicMBean.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.impl.lifecyle.
	 * ServiceMBeanLifeCycleSupport#getObjectName(java.lang.Object,
	 * java.util.Dictionary)
	 */
	@Override
	public ObjectName getObjectName(Object service,
			Map<String, Object> properties) {
		return super.getObjectNameHelper().getObjectName(service,
				ServiceReference.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.impl.lifecyle.
	 * ServiceMBeanLifeCycleSupport#getServiceInterface()
	 */
	@Override
	public Class<?> getServiceInterface() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.impl.lifecyle.
	 * ServiceMBeanLifeCycleSupport
	 * #isManageable(org.osgi.framework.ServiceReference)
	 */
	@Override
	public boolean isManageable(ServiceReference serviceReference) {
		return !super.getBundleContext().getBundle().equals(
				serviceReference.getBundle());
	}

}
