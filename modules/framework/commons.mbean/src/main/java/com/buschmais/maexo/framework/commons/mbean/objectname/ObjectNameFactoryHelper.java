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
package com.buschmais.maexo.framework.commons.mbean.objectname;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

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
 * The <code>ObjectNameFactoryHelper</code> relates to the
 * {@link ObjectNameFactory}s with regard to
 * <ul>
 * <li>their registration:
 * {@link #registerObjectNameFactory(ObjectNameFactory, Class)}</li>
 * <li>object name generation: {@link #getObjectName(Object, Class, Map)}</li>
 * </ul>
 * 
 * @see ObjectNameFactory
 */
public final class ObjectNameFactoryHelper {

	private static Logger logger = LoggerFactory
			.getLogger(ObjectNameFactoryHelper.class);

	private final BundleContext bundleContext;

	/**
	 * Constructor.
	 * 
	 * @param bundleContext
	 *            The bundle context. It is required for service registrations
	 *            and lookups.
	 */
	public ObjectNameFactoryHelper(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	/**
	 * Creates an object name for the given resource.
	 * <p>
	 * This is a convenience method which delegates to
	 * {@link #getObjectName(Object, Class, Map)} with the properties parameter
	 * set to null.
	 * 
	 * @param resource
	 *            The resource.
	 * @param resourceInterface
	 *            The interface to use for looking up the object name factory.
	 * @return The object name.
	 * @exception IllegalStateException
	 *                If no appropriate <code>ObjectNameFactory</code> is found
	 *                and therefore no object name could be constructed.
	 */
	public ObjectName getObjectName(Object resource, Class<?> resourceInterface)
			throws IllegalStateException {
		return this.getObjectName(resource, resourceInterface, null);
	}

	/**
	 * Creates an object name for the given resource.
	 * <p>
	 * The object name is actually created by looking up the corresponding
	 * object name factory in the OSGi service registry and invoking its
	 * {@link ObjectNameFactory#getObjectName(Object, Map)} method.
	 * 
	 * @param resource
	 *            The resource.
	 * @param resourceInterface
	 *            The interface to use for looking up the object name factory.
	 * @param properties
	 *            Additional properties which will be passed to the object name
	 *            factory.
	 * @return The object name.
	 * @exception IllegalStateException
	 *                If no appropriate <code>ObjectNameFactory</code> is found
	 *                and therefore no object name could be constructed.
	 */
	public ObjectName getObjectName(Object resource,
			Class<?> resourceInterface, Map<String, Object> properties)
			throws IllegalStateException {
		if (resource == null) {
			throw new IllegalArgumentException(
					"Parameters resource and resourceInterface must not be null");
		}
		// find the object name factory that handles instances of the resource's
		// interfaces
		ServiceReference[] serviceReferences;
		String filter = String.format("(%s=%s)",
				ObjectNameFactoryConstants.SERVICEPROPERTY_RESOURCEINTERFACE, resourceInterface
						.getName());
		logger.debug("looking up object name factory service for interface '{}' using filter '{}'",
				resourceInterface.getName(), filter);
		try {
			serviceReferences = this.bundleContext.getServiceReferences(
					ObjectNameFactory.class.getName(), filter);
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
						logger.debug("looking up object name for resource '{}' using object name factory '{}'",
								resource, objectNameFactory);
						return objectNameFactory.getObjectName(resource,
								properties);
					}
				} finally {
					this.bundleContext.ungetService(serviceReference);
				}
			}
		}
		// throw an exception if the object name could not be constructed
		throw new IllegalStateException(String.format(
				"No object name factory found for resource interface %s",
				resourceInterface.getName()));
	}

	/**
	 * Creates an object names for the given resources.
	 * <p>
	 * This method delegates to {@link #getObjectName(Object, Class)} for each
	 * resource contained in the provided array.
	 * 
	 * @param resources
	 *            The array of resources.
	 * @param resourceInterface
	 *           The interface to use for looking up the object name factory.
	 * @return The object names or <code>null</code> if no resources were provided.
	 */
	public ObjectName[] getObjectNames(Object[] resources,
			Class<?> resourceInterface) {
		if (resources == null) {
			return null;
		}
		ObjectName[] objectNames = new ObjectName[resources.length];
		for (int i = 0; i < resources.length; i++) {
			objectNames[i] = getObjectName(resources[i], resourceInterface);
		}
		return objectNames;
	}

	/**
	 * Registers an object name factory for a given type as a service.
	 * <p>
	 * The object name factory is registered under the class of
	 * <code>ObjectNameFactoryHelper</code>.
	 * 
	 * @param objectNameFactory
	 *            The object name factory.
	 * @param resourceInterface
	 *            The class of the resources which are supported.
	 * @return The service reference.
	 */
	public ServiceRegistration registerObjectNameFactory(
			ObjectNameFactory objectNameFactory, Class<?> resourceInterface) {
		if (objectNameFactory == null || resourceInterface == null) {
			throw new IllegalArgumentException(
					"Parameters objectNameFactory and resourceInterface must be provided");
		}
		Dictionary<String, String> properties = new Hashtable<String, String>();
		properties.put(ObjectNameFactoryConstants.SERVICEPROPERTY_RESOURCEINTERFACE,
				resourceInterface.getName());
		return this.bundleContext.registerService(ObjectNameFactory.class
				.getName(), objectNameFactory, properties);
	}
}
