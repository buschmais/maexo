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

import java.util.Date;

import javax.management.MBeanException;
import javax.management.ObjectName;
import javax.management.openmbean.TabularData;

/**
 * Management interface for an OSGI bundle.
 * 
 * @see org.osgi.framework.Bundle
 */
public interface BundleMBean {

	/**
	 * Returns this bundle's unique identifier.
	 * 
	 * @return The unique identifier of this bundle.
	 */
	Long getBundleId();

	/**
	 * Returns this bundle's current state.
	 * 
	 * @return An element of UNINSTALLED,INSTALLED, RESOLVED,STARTING,
	 *         STOPPING,ACTIVE.
	 */
	Integer getState();

	/**
	 * Returns this bundle's current state.
	 * 
	 * @return An element of UNINSTALLED,INSTALLED, RESOLVED,STARTING,
	 *         STOPPING,ACTIVE.
	 */
	String getStateAsName();

	/**
	 * Returns this bundle's Manifest headers and values.
	 * 
	 * @return A TabularData object containing this bundle's Manifest headers
	 *         and values.
	 * @throws MBeanException
	 */
	TabularData getHeaders() throws MBeanException;

	/**
	 * Returns the time when this bundle was last modified.
	 * 
	 * @return The time when this bundle was last modified.
	 */
	Long getLastModified();

	/**
	 * Returns the time when this bundle was last modified.
	 * 
	 * @return The time when this bundle was last modified.
	 */
	Date getLastModifiedAsDate();

	/**
	 * Returns this bundle's location identifier.
	 * 
	 * @return The string representation of this bundle's location identifier.
	 */
	String getLocation();

	/**
	 * Returns the services which have been registered by this bundle.
	 * 
	 * @return the services
	 */
	ObjectName[] getRegisteredServices();

	/**
	 * Returns this bundle's ServiceReference list for all services it is using
	 * or returns null if this bundle is not using any services.
	 * 
	 * @return An array of object names or null.
	 */
	ObjectName[] getServicesInUse();

	/**
	 * Start the bundle.
	 * 
	 * @throws MBeanException
	 */
	void start() throws MBeanException;

	/**
	 * Stop the bundle.
	 * 
	 * @throws MBeanException
	 */
	void stop() throws MBeanException;

	/**
	 * Update the bundle.
	 * 
	 * @throws MBeanException
	 */
	void update() throws MBeanException;

	/**
	 * Update the bundle from the provided url.
	 * 
	 * @param url
	 *            the url
	 * @throws MBeanException
	 */
	void update(String url) throws MBeanException;

	/**
	 * Uninstall the bundle.
	 */
	void uninstall() throws MBeanException;
}
