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
package com.buschmais.osgi.maexo.framework.commons.mbean.objectname;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import javax.management.ObjectName;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides common functionality to work with object names.
 * <p>
 * The <code>ObjectNameHelper</code> relates to the {@link ObjectNameFactory}s
 * with regard to
 * <ul>
 * <li>their registration:
 * {@link #registerObjectNameFactory(ObjectNameFactory, Class)}</li>
 * <li>object name generation:
 * {@link #getObjectName(Object, Class[], Dictionary)}</li>
 * </ul>
 * 
 * @see ObjectNameFactory
 */
public final class ObjectNameHelper {

	private static Logger logger = LoggerFactory
			.getLogger(ObjectNameHelper.class);

	private BundleContext bundleContext;

	/**
	 * Constructor.
	 * 
	 * @param bundleContext
	 *            for service lookups
	 */
	public ObjectNameHelper(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	/**
	 * Creates an object name for the given resource.
	 * 
	 * @param resource
	 *            the resource
	 * @return the object name
	 * @see ObjectNameHelper#getObjectName(Object, Class[], Dictionary)
	 */
	public ObjectName getObjectName(Object resource) {
		return this.getObjectName(resource,
				resource.getClass().getInterfaces(), null);
	}

	/**
	 * Creates an object name for the given resource.
	 * 
	 * @param resource
	 *            the resource
	 * @param properties
	 *            additional properties which will be passed to the object name
	 *            factory
	 * @return the object name
	 * @see ObjectNameHelper#getObjectName(Object, Class[], Dictionary)
	 */
	public ObjectName getObjectName(Object resource,
			Map<String, Object> properties) {
		return this.getObjectName(resource,
				resource.getClass().getInterfaces(), properties);
	}

	/**
	 * Creates an object name for the given resource.
	 * 
	 * @param resource
	 *            the resource
	 * @param resourceInterface
	 *            the interface to use for looking up the object name factory
	 * @return the object name
	 * @see ObjectNameHelper#getObjectName(Object, Class[], Dictionary)
	 */
	public ObjectName getObjectName(Object resource, Class<?> resourceInterface) {
		return this.getObjectName(resource,
				new Class<?>[] { resourceInterface }, null);
	}

	/**
	 * Creates an object name for the given resource.
	 * 
	 * @param resource
	 *            the resource
	 * @param resourceInterface
	 *            the interface to use for looking up the object name factory
	 * @param properties
	 *            additional properties which will be passed to the object name
	 *            factory
	 * @return the object name
	 * @see ObjectNameHelper#getObjectName(Object, Class[], Dictionary)
	 */
	public ObjectName getObjectName(Object resource,
			Class<?> resourceInterface, Map<String, Object> properties) {
		return this.getObjectName(resource,
				new Class<?>[] { resourceInterface }, properties);
	}

	/**
	 * Creates an object name for the given resource.
	 * 
	 * @param resource
	 *            the resource
	 * @param resourceInterfaces
	 *            the interfaces to use for looking up the object name factory
	 * @return the object name
	 * @see ObjectNameHelper#getObjectName(Object, Class[], Dictionary)
	 */
	public ObjectName getObjectName(Object resource,
			Class<?>[] resourceInterfaces) {
		return this.getObjectName(resource, resourceInterfaces, null);
	}

	/**
	 * Creates an object name for the given resource.
	 * <p>
	 * The object name is actually created by looking up the corresponding
	 * object name factory in the OSGi service registry and invoking its
	 * {@link ObjectNameFactory#getObjectName(Object, Dictionary)} method.
	 * 
	 * @param resource
	 *            the resource
	 * @param resourceInterfaces
	 *            the interfaces to use for looking up the object name factory
	 * @param properties
	 *            additional properties which will be passed to the object name
	 *            factory
	 * @return the object name
	 * @exception ObjectNameFactoryException
	 *                if no appropriate <code>ObjectNameFactory</code> is found
	 *                and therefore no object name could be constructed
	 */
	public ObjectName getObjectName(Object resource,
			Class<?>[] resourceInterfaces, Map<String, Object> properties)
			throws ObjectNameFactoryException {
		if (resource == null || resourceInterfaces == null
				|| resourceInterfaces.length == 0) {
			throw new IllegalArgumentException(
					"Parameters resource and resourceInterface must not be null");
		}
		// find the object name factory that handles instances of the resource's
		// interfaces
		if (logger.isDebugEnabled()) {
			logger
					.debug("looking up object name factory service for interfaces "
							+ Arrays.toString(resourceInterfaces));
		}
		ServiceReference[] serviceReferences;
		StringBuilder filter = new StringBuilder();
		filter.append("(|");
		for (Class<?> resourceInterface : resourceInterfaces) {
			filter.append("(");
			filter.append(Constants.SERVICE_PROPERTY_RESOURCEINTERFACE);
			filter.append("=");
			filter.append(resourceInterface.getName());
			filter.append(")");
		}
		filter.append(")");
		if (logger.isDebugEnabled()) {
			logger.debug("using filter : " + filter.toString());
		}
		try {
			serviceReferences = this.bundleContext.getServiceReferences(
					ObjectNameFactory.class.getName(), filter.toString());
		} catch (InvalidSyntaxException e) {
			throw new IllegalArgumentException(e);
		}
		// iterate over all available service reference and use the first
		// available one to create the object name
		if (serviceReferences != null) {
			for (ServiceReference serviceReference : serviceReferences) {
				try {
					ObjectNameFactory objectNameFactory = (ObjectNameFactory) this.bundleContext
							.getService(serviceReference);
					if (objectNameFactory != null) {
						if (logger.isDebugEnabled()) {
							logger.debug("using object name factory "
									+ objectNameFactory);
						}
						return objectNameFactory.getObjectName(resource,
								properties);
					}
				} finally {
					this.bundleContext.ungetService(serviceReference);
				}
			}
		}
		// throw an exception if the object name could not be constructed
		throw new ObjectNameFactoryException(
				"No object name factory found for resource interface "
						+ Arrays.toString(resourceInterfaces));
	}

	/**
	 * Registers an object name factory for a given type as a service.
	 * <p>
	 * The object name factory is registered under the class of
	 * <code>ObjectNameHelper</code>.
	 * 
	 * @param objectNameFactory
	 *            the object name factory
	 * @param resourceInterface
	 *            the class of the resources which are supported
	 * @return the service reference
	 */
	public ServiceRegistration registerObjectNameFactory(
			ObjectNameFactory objectNameFactory, Class<?> resourceInterface) {
		if (objectNameFactory == null || resourceInterface == null) {
			throw new IllegalArgumentException(
					"Parameters objectNameFactory and resourceInterface must be provided");
		}
		Dictionary<String, String> properties = new Hashtable<String, String>();
		properties.put(Constants.SERVICE_PROPERTY_RESOURCEINTERFACE,
				resourceInterface.getName());
		return this.bundleContext.registerService(ObjectNameFactory.class
				.getName(), objectNameFactory, properties);
	}

	/**
	 * Assembles an object name from the provided properties using the default
	 * domain.
	 * 
	 * @param properties
	 *            the properties
	 * @return the object name
	 * @see Constants#DEFAULT_DOMAIN
	 */
	public static ObjectName assembleObjectName(Map<String, Object> properties) {
		return assembleObjectName(Constants.DEFAULT_DOMAIN, properties);
	}

	/**
	 * Assembles an object name from <code>domain</code> and
	 * <code>properties</code>.
	 * <p>
	 * The object name is constructed according JMX requirements as (without
	 * spaces): <blockquote> <i>domain</i> : <i>properties.key1</i> =
	 * <i>properties.value1</i>, <i>properties .key2</i> =
	 * <i>properties.value2</i> </blockquote>
	 * 
	 * @param domain
	 *            the domain
	 * @param properties
	 *            the properties
	 * @return the object name
	 * @see ObjectName
	 */
	public static ObjectName assembleObjectName(String domain,
			Map<String, Object> properties) {
		StringBuilder sb = new StringBuilder(domain);
		sb.append(':');
		boolean firstEntry = true;
		for (Entry<String, Object> entry : properties.entrySet()) {
			if (firstEntry) {
				firstEntry = false;
			} else {
				sb.append(',');
			}
			sb.append(entry.getKey());
			sb.append('=');
			sb.append(entry.getValue());
		}
		try {
			return new ObjectName(sb.toString());
		} catch (Exception e) {
			throw new IllegalArgumentException(String.format(
					"Cannot create object name instance for '%s'", sb), e);
		}
	}

}
