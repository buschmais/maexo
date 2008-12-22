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

public interface ServiceMBean {

	/**
	 * Returns the bundle which registered this service
	 * 
	 * @return the bundle
	 */
	public ObjectName getBundle();

	/**
	 * Returns the id of this service
	 * 
	 * @return the id
	 */
	public Long getId();

	/**
	 * Returns the description of this service
	 * 
	 * @return the description
	 */
	public String getDescription();


	/**
	 * Returns the object class(es) of this service
	 * 
	 * @return the object class(es)
	 */
	public String[] getObjectClass();


	/**
	 * Returns the persistent id of this services
	 * 
	 * @return the persistent id
	 */
	public String getPid();

	/**
	 * Returns the properties of this service
	 * 
	 * @return the properties
	 * @throws MBeanException
	 */
	public TabularData getProperties() throws MBeanException;

	/**
	 * Returns the ranking of this service
	 * 
	 * @return the ranking
	 */
	public Integer getRanking();

	/**
	 * Returns the bundles which are using this service
	 * 
	 * @return the using bundles
	 */
	public ObjectName[] getUsingBundles();

	/**
	 * Returns the vendor of this service
	 * 
	 * @return the vendor
	 */
	public String getVendor();

}
