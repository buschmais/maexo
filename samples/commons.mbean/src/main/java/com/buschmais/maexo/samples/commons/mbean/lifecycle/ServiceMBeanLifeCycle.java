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
package com.buschmais.maexo.samples.commons.mbean.lifecycle;

import javax.management.DynamicMBean;
import javax.management.ObjectName;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.maexo.framework.commons.mbean.lifecycle.ServiceMBeanLifeCycleSupport;

/**
 * This sample demonstrates how to use the {@link ServiceMBeanLifeCycleSupport}.
 * <p>
 * The class {@link ServiceMBeanLifeCycleSupport} offers an easy way to track
 * the life cycle of services in the OSGi registry and export MBeans which
 * represent such a service.
 * <p>
 * There is another class called {@link DefaultServiceMBeanLifeCycleSupport}
 * which provides default implementations for the methods
 * {@link ServiceMBeanLifeCycleSupport#getServiceFilter()} and
 * {@link ServiceMBeanLifeCycleSupport#getObjectName()}. For further information
 * refer to the API documentation.
 */
public class ServiceMBeanLifeCycle extends ServiceMBeanLifeCycleSupport {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ServiceMBeanLifeCycle.class);

	/**
	 * The constructor.
	 *
	 * @param bundleContext
	 *            The bundle context.
	 */
	public ServiceMBeanLifeCycle(BundleContext bundleContext) {
		super(bundleContext);
	}

	/**
	 * Returns the class which defines the services to track in the OSGi service
	 * registry.
	 *
	 * {@inheritDoc}
	 */
	@Override
	protected final Class<?> getServiceInterface() {
		return Service.class;
	}

	/**
	 * An additional filter may be used for tracking services.
	 *
	 * {@inheritDoc}
	 */
	@Override
	protected final String getServiceFilter() {
		return null;
	}

	/**
	 * Constructs the MBean which will represent/manage the service.
	 *
	 * {@inheritDoc}
	 */
	@Override
	protected final Object getMBean(ServiceReference serviceReference,
			Object service) {
		logger.info(String.format("constructing service MBean for service %s",
				service));
		return new ServiceMBean((Service) service);
	}

	/**
	 * Returns the interface which will be used to publish the MBean.
	 *
	 * {@inheritDoc}
	 */
	@Override
	protected final Class<?> getMBeanInterface() {
		return DynamicMBean.class;
	}

	/**
	 * Returns the {@link ObjectName} for the MBean.
	 *
	 * {@inheritDoc}
	 */
	@Override
	protected final ObjectName getObjectName(ServiceReference serviceReference,
			Object service) {
		try {
			return new ObjectName(
					"com.buschmais.maexo.sample:type=ServiceMBean");
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
}
