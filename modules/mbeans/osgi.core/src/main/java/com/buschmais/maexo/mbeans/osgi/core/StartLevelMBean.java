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
package com.buschmais.maexo.mbeans.osgi.core;

import javax.management.ObjectName;

/**
 * Management interface for the OSGi start level service.
 *
 * @see org.osgi.service.startlevel.StartLevel
 */
public interface StartLevelMBean {

	/**
	 * Returns the assigned start level value for the specified bundle.
	 *
	 * @see org.osgi.service.startlevel.StartLevel#getBundleStartLevel(Bundle)
	 *
	 * @param objectName
	 *            The object name of the bundle.
	 * @return The start level value of the specified bundle.
	 */
	Integer getBundleStartLevel(ObjectName objectName);

	/**
	 * Return the assigned start level value for the specified bundle.
	 *
	 * @see org.osgi.service.startlevel.StartLevel#getBundleStartLevel(Bundle)
	 *
	 * @param id
	 *            The id of the bundle.
	 * @return The start level value of the specified bundle.
	 */
	Integer getBundleStartLevel(Long id);

	/**
	 * Return the initial start level value that is assigned to a bundle when it
	 * is first installed.
	 *
	 * @see org.osgi.service.startlevel.StartLevel#getInitialBundleStartLevel()
	 *
	 * @return The initial start level value for bundles.
	 */
	Integer getInitialBundleStartLevel();

	/**
	 * Return the active start level value of the framework. If the framework is
	 * in the process of changing the start level this method must return the
	 * active start level if this differs from the requested start level.
	 *
	 * @see org.osgi.service.startlevel.StartLevel#getStartLevel()
	 *
	 * @return The active start level value of the framework.
	 */
	Integer getStartLevel();

	/**
	 * Return the persistent state of the specified bundle.
	 *
	 * @see org.osgi.service.startlevel.StartLevel#isBundlePersistentlyStarted(Bundle)
	 *
	 * @param objectName
	 *            The object name of the bundle.
	 * @return TRUE if the bundle is persistently marked to be started.
	 */
	Boolean isBundlePersistentlyStarted(ObjectName objectName);

	/**
	 * Return the persistent state of the specified bundle.
	 *
	 * @see org.osgi.service.startlevel.StartLevel#isBundlePersistentlyStarted(Bundle)
	 *
	 * @param id
	 *            The id of the bundle.
	 * @return TRUE if the bundle is persistently marked to be started.
	 */
	Boolean isBundlePersistentlyStarted(Long id);

	/**
	 * Assign a start level value to the specified bundle.
	 *
	 * @see org.osgi.service.startlevel.StartLevel#setBundleStartLevel(Bundle,
	 *      int)
	 *
	 * @param objectName
	 *            The object name of the bundle.
	 * @param startLevel
	 *            The new start level for the specified bundle.
	 */
	void setBundleStartLevel(ObjectName objectName, Integer startLevel);

	/**
	 * Assign a start level value to the specified bundle.
	 *
	 * @see org.osgi.service.startlevel.StartLevel#setBundleStartLevel(Bundle,
	 *      int)
	 *
	 * @param id
	 *            The id name of the bundle.
	 * @param startLevel
	 *            The new start level for the specified bundle.
	 */
	void setBundleStartLevel(Long id, Integer startLevel);

	/**
	 * Set the initial start level value that is assigned to a bundle when it is
	 * first installed.
	 *
	 * @see org.osgi.service.startlevel.StartLevel#setInitialBundleStartLevel(int)
	 *
	 * @param startLevel
	 *            The initial start level for newly installed bundles.
	 */
	void setInitialBundleStartLevel(Integer startLevel);

	/**
	 * Modify the active start level of the framework.
	 *
	 * @see org.osgi.service.startlevel.StartLevel#setStartLevel(int)
	 *
	 * @param startLevel
	 *            The requested start level for the framework.
	 */
	void setStartLevel(Integer startLevel);

}
