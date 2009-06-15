/*
 * Copyright 2009 buschmais GbR
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
package com.buschmais.maexo.mbeans.osgi.compendium;

import javax.management.ObjectName;

/**
 * Management interface for the OSGi Configuration Admin service.
 *
 * @see {@link ConfigurationAdmin}
 *
 */
public interface ConfigurationAdminMBean {

	/**
	 * Create a new factory Configuration object with a new PID.
	 *
	 * @param factoryPid
	 *            PID of factory (not null).
	 * @return The {@link ObjectName} representing the new configuration.
	 */
	ObjectName createFactoryConfiguration(String factoryPid);

	/**
	 *Create a new factory Configuration object with a new PID.
	 *
	 * @param factoryPid
	 *            PID of factory (not null).
	 * @param location
	 *            A bundle location string, or null.
	 * @return The {@link ObjectName} representing the new configuration.
	 */
	ObjectName createFactoryConfiguration(String factoryPid, String location);

	/**
	 * Get an existing or new Configuration object from the persistent store.
	 *
	 * @param pid
	 *            Persistent identifier.
	 * @return The {@link ObjectName} representing a new Configuration object.
	 */
	ObjectName getConfiguration(String pid);

	/**
	 * Get an existing Configuration object from the persistent store, or create
	 * a new Configuration object.
	 *
	 * @param pid
	 *            Persistent identifier.
	 * @param location
	 *            The bundle location string, or null.
	 * @return The {@link ObjectName} representing an existing or new
	 *         Configuration object.
	 */
	ObjectName getConfiguration(String pid, String location);

	/**
	 * List the current Configuration objects which match the filter.
	 *
	 * @param filter
	 *            A filter string, or null to retrieve all Configuration
	 *            objects.
	 * @return All {@link ObjectName}s of matching Configuration objects, or
	 *         null if there aren't any.
	 */
	ObjectName[] listConfigurations(String filter);
}
