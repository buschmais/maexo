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

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the {@link ServiceMBean} interface.
 */
public class ServicePublisher implements ServicePublisherMBean {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ServicePublisher.class);

	/**
	 * The bundle context.
	 */
	private BundleContext bundleContext;

	/**
	 * The service registration.
	 */
	private ServiceRegistration serviceRegistration = null;

	/**
	 * The Constructor. It requires a {@link BundleContext} which will be used
	 * to publish a service.
	 *
	 * @param bundleContext
	 *            The {@link BundleContext}.
	 */
	public ServicePublisher(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	/**
	 * {@inheritDoc}
	 */
	public void registerService() {
		if (this.serviceRegistration == null) {
			logger.info("registering service.");
			// Create an instance of the service and register it.
			Service service = new ServiceImpl();
			this.serviceRegistration = this.bundleContext.registerService(
					Service.class.getName(), service, null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void unregisterService() {
		if (this.serviceRegistration != null) {
			logger.info("unregistering service.");
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}
	}

}
