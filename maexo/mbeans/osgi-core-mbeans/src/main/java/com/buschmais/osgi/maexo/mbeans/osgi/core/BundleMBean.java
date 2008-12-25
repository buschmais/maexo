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
package com.buschmais.osgi.maexo.mbeans.osgi.core;

import javax.management.MBeanException;
import javax.management.ObjectName;
import javax.management.openmbean.TabularData;

public interface BundleMBean {


	/**
	 * Returns the id of the bundle
	 * 
	 * @return the id
	 */
	public Long getId();

	/**
	 * Returns the numeric representation of the bundle state
	 * 
	 * @return the state
	 */
	public Integer getState();

	/**
	 * Returns the human readable representation of the bundle state
	 * 
	 * @return the state
	 */
	public String getStateAsName();


	/**
	 * Returns the bundle's headers
	 * 
	 * @return the headers
	 * @throws MBeanException
	 */
	public TabularData getHeaders() throws MBeanException;

	/**
	 * Returns the services which have been registered by this bundle
	 * 
	 * @return the services
	 */
	public ObjectName[] getRegisteredServices();

	/**
	 * Start the bundle
	 * 
	 * @throws MBeanException
	 */
	public void start() throws MBeanException;

	/**
	 * Stop the bundle
	 * 
	 * @throws MBeanException
	 */
	public void stop() throws MBeanException;

	/**
	 * Update the bundle
	 * 
	 * @throws MBeanException
	 */
	public void update() throws MBeanException;

	/**
	 * Update the bundle from the provided url
	 * 
	 * @param url
	 *            the url
	 * @throws MBeanException
	 */
	public void update(String url) throws MBeanException;

	/**
	 * Uninstall the bundle
	 */
	public void uninstall() throws MBeanException;
}
