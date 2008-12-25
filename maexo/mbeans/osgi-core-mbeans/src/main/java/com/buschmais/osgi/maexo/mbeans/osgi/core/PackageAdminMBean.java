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

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

/**
 * MBean which represents the PackageAdmin service.
 */
public interface PackageAdminMBean {

	/**
	 * Returns the special type of the specified bundle.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#getBundleType(org.osgi.framework.Bundle)
	 * 
	 * @param objectName
	 *            The object name of the bundle.
	 * @return the special type of the bundle
	 * @throws ReflectionException
	 * @throws MBeanException
	 * @throws InstanceNotFoundException
	 * @throws AttributeNotFoundException
	 */
	public Integer getBundleType(ObjectName objectName)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException;

	/**
	 * Returns the special type of the specified bundle.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#getBundleType(org.osgi.framework.Bundle)
	 * 
	 * @param id
	 *            The id of the bundle.
	 * @return the special type of the bundle
	 */
	public Integer getBundleType(Long id);

	/**
	 * Returns the special type of the specified bundle as name.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#getBundleType(org.osgi.framework.Bundle)
	 * 
	 * @param objectName
	 *            The object name of the bundle.
	 * @return the special type of the bundle
	 * @throws ReflectionException
	 * @throws MBeanException
	 * @throws InstanceNotFoundException
	 * @throws AttributeNotFoundException
	 */
	public String getBundleTypeAsName(ObjectName objectName)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException;

	/**
	 * Returns the special type of the specified bundle as name.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#getBundleType(org.osgi.framework.Bundle)
	 * 
	 * @param id
	 *            The id of the bundle.
	 * @return the special type of the bundle
	 */
	public String getBundleTypeAsName(Long id);

	/**
	 * Returns the bundles with the specified symbolic name whose bundle version
	 * is within the specified version range.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#getBundles(String,
	 *      String)
	 * 
	 * @param symbolicName
	 *            The symbolic name of the desired bundles.
	 * @param versionRange
	 *            The version range of the desired bundles, or null if all
	 *            versions are desired.
	 * @return An array of bundles object names
	 */
	public ObjectName[] getBundles(String symbolicName, String versionRange);

	/**
	 * Gets the exported package for the specified package name.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#getExportedPackage(String)
	 * 
	 * @param name
	 *            The name of the exported package to be returned.
	 * @return The exported package, or null if no exported package with the
	 *         specified name exists.
	 * @throws MBeanException
	 */
	public CompositeData getExportedPackage(String name) throws MBeanException;

	/**
	 * Gets the exported packages for the specified bundle.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#getExportedPackages(org.osgi.framework.Bundle)
	 * 
	 * @param objectName
	 *            The object name of the bundle.
	 * @return The exported packages, or null if the specified bundle has no
	 *         exported packages.
	 * @throws ReflectionException
	 * @throws MBeanException
	 * @throws InstanceNotFoundException
	 * @throws AttributeNotFoundException
	 */
	public TabularData getExportedPackages(ObjectName objectName)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException;

	/**
	 * Gets the exported packages for the specified bundle.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#getExportedPackages(org.osgi.framework.Bundle)
	 * 
	 * @param id
	 *            The id of the bundle.
	 * @return The exported packages, or null if the specified bundle has no
	 *         exported packages.
	 * @throws MBeanException
	 */
	public TabularData getExportedPackages(Long id) throws MBeanException;

	/**
	 * Gets the exported packages for the specified package name.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#getExportedPackages(String)
	 * 
	 * @param name
	 *            The name of the exported packages to be returned.
	 * @return The exported packages, or null if no exported packages with the
	 *         specified name exists.
	 * @throws MBeanException
	 */
	public TabularData getExportedPackages(String name) throws MBeanException;

	/**
	 * Returns the of attached fragment bundles for the specified bundle.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#getFragments(org.osgi.framework.Bundle)
	 * 
	 * @param objectName
	 *            The object name of the bundle.
	 * @return The array of fragment bundles or null if the bundle does not have
	 *         any attached fragment bundles or the bundle is not resolved.
	 * @throws ReflectionException
	 * @throws MBeanException
	 * @throws InstanceNotFoundException
	 * @throws AttributeNotFoundException
	 */
	public ObjectName[] getFragments(ObjectName objectName)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException;

	/**
	 * Returns the of attached fragment bundles for the specified bundle.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#getFragments(org.osgi.framework.Bundle)
	 * 
	 * @param id
	 *            The id of the bundle.
	 * @return The array of fragment bundles or null if the bundle does not have
	 *         any attached fragment bundles or the bundle is not resolved.
	 */
	public ObjectName[] getFragments(Long id);

	/**
	 * Returns the containing the host bundle to which the specified fragment
	 * bundle is attached or null if the specified bundle is not attached to a
	 * host or is not a fragment bundle.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#getHosts(org.osgi.framework.Bundle)
	 * 
	 * @param objectName
	 *            The object name of the bundle.
	 * @return An array containing the host bundle or null if the bundle does
	 *         not have a host bundle.
	 * @throws ReflectionException
	 * @throws MBeanException
	 * @throws InstanceNotFoundException
	 * @throws AttributeNotFoundException
	 */
	public ObjectName[] getHosts(ObjectName objectName)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException;

	/**
	 * Returns the containing the host bundle to which the specified fragment
	 * bundle is attached or null if the specified bundle is not attached to a
	 * host or is not a fragment bundle.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#getHosts(org.osgi.framework.Bundle)
	 * 
	 * @param id
	 *            The id of the bundle.
	 * @return An array containing the host bundle or null if the bundle does
	 *         not have a host bundle.
	 */
	public ObjectName[] getHosts(Long id);

	/**
	 * Returns an array of required bundles having the specified symbolic name.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#getRequiredBundles(String)
	 * 
	 * @param symbolicName
	 *            The bundle symbolic name or null for all required bundles.
	 * @return An array of required bundles or null if no required bundles exist
	 *         for the specified symbolic name.
	 * @throws MBeanException
	 */
	public TabularData getRequiredBundles(String symbolicName)
			throws MBeanException;

	/**
	 * Forces the update (replacement) or removal of packages exported by the
	 * specified bundles.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#refreshPackages(org.osgi.framework.Bundle[])
	 * 
	 * @param objectNames
	 *            The object names of the bundles whose exported packages are to
	 *            be updated or removed, or null for all bundles updated or
	 *            uninstalled since the last call to this method.
	 * @throws ReflectionException
	 * @throws MBeanException
	 * @throws InstanceNotFoundException
	 * @throws AttributeNotFoundException
	 */
	public void refreshPackages(ObjectName[] objectNames)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException;

	/**
	 * Forces the update (replacement) or removal of packages exported by the
	 * specified bundles.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#refreshPackages(org.osgi.framework.Bundle[])
	 * 
	 * @param ids
	 *            The ids of the bundles whose exported packages are to be
	 *            updated or removed, or null for all bundles updated or
	 *            uninstalled since the last call to this method.
	 */
	public void refreshPackages(Long[] ids);

	/**
	 * Forces the update (replacement) or removal of packages. This is
	 * equivalent to a call to the methods {@link #refreshPackages(Long[])} or
	 * {@link #refreshPackages(ObjectName[])} with empty arrays.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#resolveBundles(org.osgi.framework.Bundle[])
	 */
	public void refreshPackages();

	/**
	 * Resolve the specified bundles.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#resolveBundles(org.osgi.framework.Bundle[])
	 * 
	 * @param objectNames
	 *            The object names of the bundles to resolve or null to resolve
	 *            all unresolved bundles installed in the Framework.
	 * @return true if all specified bundles are resolved;
	 * @throws ReflectionException
	 * @throws MBeanException
	 * @throws InstanceNotFoundException
	 * @throws AttributeNotFoundException
	 */
	public Boolean resolveBundles(ObjectName[] objectNames)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException;

	/**
	 * Resolve the specified bundles.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#resolveBundles(org.osgi.framework.Bundle[])
	 * 
	 * @param ids
	 *            The ids of the bundles to resolve or null to resolve all
	 *            unresolved bundles installed in the Framework.
	 * @return true if all specified bundles are resolved;
	 */
	public Boolean resolveBundles(Long[] ids);

	/**
	 * Resolve all unresolved bundles. This is equivalent to a call to the
	 * methods {@link #resolveBundles(Long[])} or
	 * {@link #resolveBundles(ObjectName[])} with empty arrays.
	 * 
	 * @see org.osgi.service.packageadmin.PackageAdmin#resolveBundles(org.osgi.framework.Bundle[])
	 */
	public Boolean resolveBundles();
}