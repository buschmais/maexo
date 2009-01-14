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

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.ObjectName;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;

/**
 * Provides support to control the life cycle of mbeans.
 */
public abstract class MBeanLifecycleSupport {

	private static final Logger logger = LoggerFactory
			.getLogger(MBeanLifecycleSupport.class);

	/**
	 * The bundle context of the exporting bundle.
	 */
	private BundleContext bundleContext;

	/**
	 * The object name helper instance.
	 */
	private ObjectNameFactoryHelper objectNameFactoryHelper;

	private Map<Object, ServiceRegistration> mbeanRegistrations = new ConcurrentHashMap<Object, ServiceRegistration>();

	/**
	 * Constructor.
	 * 
	 * @param bundleContext
	 *            the bundle context of the exporting bundle
	 */
	public MBeanLifecycleSupport(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		this.objectNameFactoryHelper = new ObjectNameFactoryHelper(
				bundleContext);
	}

	/**
	 * @return the bundleContext
	 */
	public final BundleContext getBundleContext() {
		return bundleContext;
	}

	/**
	 * @return the objectNameFactoryHelper
	 */
	public final ObjectNameFactoryHelper getObjectNameHelper() {
		return objectNameFactoryHelper;
	}

	/**
	 * Registers an mbean as a service in the OSGi service registration.
	 * <p>
	 * The mbean is stored under the object name for later unregistration
	 * 
	 * @param mbeanInterface
	 *            the interface to use for registration
	 * @param objectName
	 *            the object name
	 * @param mbean
	 *            the mbean
	 */
	public final void registerMBeanService(Class<?> mbeanInterface,
			ObjectName objectName, Object mbean) {
		Dictionary<String, Object> serviceProperties = new Hashtable<String, Object>();
		serviceProperties.put(ObjectName.class.getName(), objectName);
		if (logger.isDebugEnabled()) {
			logger
					.debug(
							"registering mbean with object name '{}' as service with interface {}",
							objectName, mbeanInterface.getClass().getName());
		}
		ServiceRegistration serviceRegistration = this.bundleContext
				.registerService(mbeanInterface.getName(), mbean,
						serviceProperties);
		this.mbeanRegistrations.put(objectName, serviceRegistration);
	}

	/**
	 * Unregisters a previously registered mbean.
	 * 
	 * @param objectName
	 *            the objectName which identifies the mbean to be unregistered
	 */
	public final void unregisterMBeanService(ObjectName objectName) {
		// lookup serviceRegistration
		ServiceRegistration serviceRegistration = this.mbeanRegistrations
				.get(objectName);
		if (serviceRegistration != null) {
			// unregister service
			serviceRegistration.unregister();
		} else {
			logger
					.debug(
							"mbean service with object name '{}' not found, skipping unregistration",
							objectName);
		}
	}
}
