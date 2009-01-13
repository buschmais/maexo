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

import javax.management.DynamicMBean;
import javax.management.ObjectName;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.buschmais.osgi.maexo.framework.commons.mbean.lifecycle.ServiceMBeanLifeCycleSupport;
import com.buschmais.osgi.maexo.mbeans.osgi.core.Service;

/**
 * 
 * TODO@DM: complete javadoc (exclusion of services)
 * 
 * 
 * This class implements a service event listener to manage the life cycle of
 * the associated service mbeans.
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object getMBean(ServiceReference serviceReference, Object service) {
		return new Service(super.getBundleContext(), serviceReference);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<?> getMBeanInterface() {
		return DynamicMBean.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ObjectName getObjectName(ServiceReference serviceReference,
			Object service) {
		return super.getObjectNameHelper().getObjectName(serviceReference,
				ServiceReference.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<?> getServiceInterface() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getServiceFilter() {
		// do not create MBeans for services which themselves represent
		// services, e.g. the MBean services which were registered by this
		// bundle
		return String.format(
				"(!(&(objectClass=*MBean)(|(%s=*)(objectName=*))))",
				ObjectName.class.getName());
	}
}
