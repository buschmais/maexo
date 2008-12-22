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
package com.buschmais.osgi.maexo.mbeans.osgi.core.impl.listener;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.ObjectName;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameHelper;

public abstract class LifecycleListener {

	private static final Logger logger = LoggerFactory
			.getLogger(LifecycleListener.class);

	private BundleContext bundleContext;

	private ObjectNameHelper objectNameHelper;

	private Map<Object, ServiceRegistration> mbeanRegistrations = new ConcurrentHashMap<Object, ServiceRegistration>();

	protected LifecycleListener(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		this.objectNameHelper = new ObjectNameHelper(bundleContext);
	}

	/**
	 * @return the bundleContext
	 */
	protected BundleContext getBundleContext() {
		return bundleContext;
	}

	/**
	 * @return the objectNameHelper
	 */
	protected ObjectNameHelper getObjectNameHelper() {
		return objectNameHelper;
	}

	/**
	 * Registers a managed bean as service and stores it under a provided key
	 * for later unregistration
	 * 
	 * @param mbeanInterface
	 *            the interface to use for registration
	 * @param objectName
	 *            the object name
	 * @param mbean
	 *            the managed bean
	 */
	protected void registerMBeanService(Class<?> mbeanInterface,
			ObjectName objectName, Object mbean) {
		try {
			Dictionary<String, Object> serviceProperties = new Hashtable<String, Object>();
			serviceProperties.put(ObjectName.class.getName(), objectName);
			ServiceRegistration serviceRegistration = this.bundleContext
					.registerService(mbeanInterface.getName(), mbean,
							serviceProperties);
			this.mbeanRegistrations.put(objectName, serviceRegistration);
		} catch (Exception e) {
			logger.error("cannot register mbean", e);
		}
	}

	/**
	 * Unregisters a previously registered mbean
	 * 
	 * @param key
	 *            the key, which identifies the mbean
	 */
	protected void unregisterMBeanService(ObjectName objectName) {
		// lookup serviceRegistration
		ServiceRegistration serviceRegistration = this.mbeanRegistrations
				.get(objectName);
		if (serviceRegistration != null) {
			// unregister service
			serviceRegistration.unregister();
		} else {
			logger.warn("mbean service with key=" + objectName
					+ " not found, skipping unregistration");
		}
	}
}
