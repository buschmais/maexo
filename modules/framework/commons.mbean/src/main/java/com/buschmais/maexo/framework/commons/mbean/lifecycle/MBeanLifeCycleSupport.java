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
package com.buschmais.maexo.framework.commons.mbean.lifecycle;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.ObjectName;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;

/**
 * Provides support to control the life cycle of MBeans.
 * <p>
 * This class provides support to register MBeans as OSGi services. Other
 * bundles like the MAEXO SwitchBoard may track the life cycle of these services
 * for registration on MBeanServer instances.
 * <p>
 * A derived class usually implements a life cycle listener interface (e.g.
 * {@link org.osgi.framwork.ServiceListener}) and uses the methods
 * {@link #registerMBeanService(Class, ObjectName, Object)} and
 * {@link #unregisterMBeanService(ObjectName)} to control registration of MBeans
 * in the OSGi service registry.
 */
public abstract class MBeanLifeCycleSupport {

	private static final Logger logger = LoggerFactory
			.getLogger(MBeanLifeCycleSupport.class);

	/**
	 * The bundle context of the exporting bundle.
	 */
	private final BundleContext bundleContext;

	/**
	 * The object name helper instance.
	 */
	private final ObjectNameFactoryHelper objectNameFactoryHelper;

	private final Map<Object, ServiceRegistration> mbeanRegistrations = new ConcurrentHashMap<Object, ServiceRegistration>();

	/**
	 * Constructor.
	 * 
	 * @param bundleContext
	 *            the bundle context of the exporting bundle
	 */
	public MBeanLifeCycleSupport(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		this.objectNameFactoryHelper = new ObjectNameFactoryHelper(
				bundleContext);
	}

	/**
	 * Returns the bundle context.
	 * 
	 * @return the bundle context
	 */
	public final BundleContext getBundleContext() {
		return bundleContext;
	}

	/**
	 * Returns the ObjectNameFactoryHelper.
	 * 
	 * @return the objectNameFactoryHelper
	 */
	protected final ObjectNameFactoryHelper getObjectNameHelper() {
		return objectNameFactoryHelper;
	}

	/**
	 * Registers an MBean as a service in the OSGi service registration.
	 * <p>
	 * The MBean is registered using the interface provided by the parameter
	 * <code>mbeanInterface</code> and will have a property named
	 * "javax.management.ObjectName" with the value of the parameter
	 * <code>objectName</code>.
	 * 
	 * @param mbeanInterface
	 *            The interface for service registration.
	 * @param objectName
	 *            The unique object name which will be be used as service
	 *            property.
	 * @param mbean
	 *            The mbean instance.
	 */
	protected final void registerMBeanService(Class<?> mbeanInterface,
			ObjectName objectName, Object mbean) {
		Dictionary<String, Object> serviceProperties = new Hashtable<String, Object>();
		serviceProperties.put(ObjectName.class.getName(), objectName);
		logger
				.debug(
						"registering mbean with object name '{}' as service with interface {}",
						objectName, mbeanInterface.getName());
		ServiceRegistration serviceRegistration = this.bundleContext
				.registerService(mbeanInterface.getName(), mbean,
						serviceProperties);
		this.mbeanRegistrations.put(objectName, serviceRegistration);
	}

	/**
	 * Unregisters a previously registered MBean.
	 * <p>
	 * The MBean is identified by its object name.
	 * 
	 * @param objectName
	 *            The object name which identifies the MBean to be unregistered.
	 */
	protected final void unregisterMBeanService(ObjectName objectName) {
		// lookup serviceRegistration
		ServiceRegistration serviceRegistration = this.mbeanRegistrations
				.get(objectName);
		if (serviceRegistration != null) {
			// unregister service
			if (logger.isDebugEnabled()) {
				logger.debug(
						"unregistering mbean service with object name '{}'",
						objectName);
			}
			serviceRegistration.unregister();
		} else {
			logger
					.debug(
							"mbean service with object name '{}' not found, skipping unregistration",
							objectName);
		}
	}
}
