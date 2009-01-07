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
package com.buschmais.osgi.maexo.mbeans.osgi.core;

import javax.management.MBeanException;
import javax.management.ObjectName;
import javax.management.openmbean.TabularData;

/**
 * Management interface for an OSGI service.
 * 
 * @see org.osgi.framework.Bundle
 */
public interface ServiceMBean {

	/**
	 * Returns the object name of the bundle which registered this service.
	 * 
	 * @return The object name of the bundle that registered the service; null
	 *         if that service has already been unregistered.
	 */
	ObjectName getBundle();

	/**
	 * Returns the service property named "service.id" identifying a service's
	 * registration number.
	 * 
	 * @return The service property named "service.id".
	 */
	Long getId();

	/**
	 * Returns the service property named "service.description" identifying a
	 * service's description.
	 * 
	 * @return The service property named "service.description".
	 */
	String getDescription();

	/**
	 * Returns the service property named "objectClass" identifying all of the
	 * class names under which a service was registered in the Framework.
	 * 
	 * @return The service property named "objectClass".
	 */
	String[] getObjectClass();

	/**
	 * Returns the service property named "service.pid" identifying a service's
	 * persistent identifier.
	 * 
	 * @return The service property named "service.pid".
	 */
	String getPid();

	/**
	 * Returns the properties of the Dictionary object of the service.
	 * 
	 * @return The properties of the Dictionary object of the service.
	 * @throws MBeanException
	 */
	TabularData getProperties() throws MBeanException;

	/**
	 * Returns the service property named "service.ranking" identifying a
	 * service's ranking number (of type java.lang.Integer).
	 * 
	 * @return The service property named "service.ranking".
	 */
	Integer getRanking();

	/**
	 * Returns the bundles that are using the service.
	 * 
	 * @return An array of bundle's object names whose usage count for the
	 *         service referenced object is greater than zero; null if no
	 *         bundles are currently using that service.
	 */
	ObjectName[] getUsingBundles();

	/**
	 * Returns the service property named "service.vendor" identifying a
	 * service's vendor.
	 * 
	 * @return The service property named "service.vendor".
	 */
	String getVendor();

}
