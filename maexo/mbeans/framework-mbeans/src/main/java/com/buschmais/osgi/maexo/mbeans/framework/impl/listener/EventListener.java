package com.buschmais.osgi.maexo.mbeans.framework.impl.listener;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.ObjectName;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.osgi.maexo.core.objectname.ObjectNameHelper;

public abstract class EventListener {

	public static final String SERVICE_PROPERTY_OBJECTNAME = "objectName";

	private static final Logger logger = LoggerFactory
			.getLogger(EventListener.class);

	private BundleContext bundleContext;

	ObjectNameHelper objectNameHelper;

	private Map<Object, ServiceRegistration> mbeanRegistrations = new ConcurrentHashMap<Object, ServiceRegistration>();

	protected EventListener(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		this.objectNameHelper = new ObjectNameHelper(bundleContext);
	}

	/**
	 * @return the objectNameHelper
	 */
	protected ObjectNameHelper getObjectNameHelper() {
		return objectNameHelper;
	}

	/**
	 * Registers a managed bean as service and stores it under a provided key for
	 * later unregistration
	 * 
	 * @param mbeanInterface
	 *            the interface to use for registration
	 * @param objectName
	 *            the object name
	 * @param key
	 *            the key to identify the bean
	 * @param mbean
	 *            the managed bean
	 */
	protected void registerMBeanService(Class<?> mbeanInterface,
			ObjectName objectName, Object key, Object mbean) {
		try {
			Dictionary<String, Object> serviceProperties = new Hashtable<String, Object>();
			serviceProperties.put(SERVICE_PROPERTY_OBJECTNAME, objectName);
			ServiceRegistration serviceRegistration = this.bundleContext
					.registerService(mbeanInterface.getName(), mbean,
							serviceProperties);
			this.mbeanRegistrations.put(key, serviceRegistration);
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
	protected void unregisterMBeanService(Object key) {
		// lookup serviceRegistration
		ServiceRegistration serviceRegistration = this.mbeanRegistrations
				.get(key);
		if (serviceRegistration != null) {
			// unregister service
			serviceRegistration.unregister();
		} else {
			logger.warn("mbean service with key=" + key
					+ " not found, skipping unregistration");
		}
	}

}
