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

import java.util.HashMap;
import java.util.Map;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanRegistration;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.RequiredBundle;

import com.buschmais.osgi.maexo.framework.commons.mbean.dynamic.DynamicMBeanSupport;
import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameHelper;

/**
 * Represents the OSGi package admin service.
 */
public class PackageAdmin extends DynamicMBeanSupport implements
		PackageAdminMBean, MBeanRegistration {

	// translation map for bundle states
	private static Map<Integer, String> bundleTypes;

	static {
		bundleTypes = new HashMap<Integer, String>();
		bundleTypes
				.put(
						Integer
								.valueOf(org.osgi.service.packageadmin.PackageAdmin.BUNDLE_TYPE_FRAGMENT),
						"FRAGMENT");
	}

	/**
	 * The bundle context
	 */
	private BundleContext bundleContext;

	/**
	 * The package admin service
	 */
	private org.osgi.service.packageadmin.PackageAdmin packageAdmin;

	/**
	 * The object name helper
	 */
	private ObjectNameHelper objectNameHelper;

	private TabularType exportedPackagesType;

	private CompositeType exportedPackageType;

	private TabularType requiredBundlesType;

	private CompositeType requiredBundleType;

	/**
	 * Constructor
	 * 
	 * @param bundleContext
	 *            The bundle context.
	 * @param packageAdmin
	 *            The package admin service.
	 */
	public PackageAdmin(BundleContext bundleContext,
			org.osgi.service.packageadmin.PackageAdmin packageAdmin) {
		this.bundleContext = bundleContext;
		this.packageAdmin = packageAdmin;
		this.objectNameHelper = new ObjectNameHelper(bundleContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.DynamicMBean#getMBeanInfo()
	 */
	public MBeanInfo getMBeanInfo() {
			String className = org.osgi.service.packageadmin.PackageAdmin.class.getName();
		// attributes
		OpenMBeanAttributeInfoSupport[] mbeanAttributeInfos = new OpenMBeanAttributeInfoSupport[0];
		// operations
		OpenMBeanOperationInfoSupport[] mbeanOperationInfos = new OpenMBeanOperationInfoSupport[] {
				PackageAdminConstants.GETBUNDLES, PackageAdminConstants.GETBUNDLETYPE_BY_ID,
				PackageAdminConstants.GETBUNDLETYPE_BY_OBJECTNAME, PackageAdminConstants.GETBUNDLETYPEASNAME_BY_ID,
				PackageAdminConstants.GETBUNDLETYPEASNAME_BY_OBJECTNAME,
				PackageAdminConstants.EXPORTEDPACKAGE_BY_OBJECTNAME, PackageAdminConstants.EXPORTEDPACKAGES_BY_ID,
				PackageAdminConstants.EXPORTEDPACKAGES_BY_OBJECTNAME, PackageAdminConstants.EXPORTEDPACKAGES_BY_NAME,
				PackageAdminConstants.FRAGMENTS_BY_ID, PackageAdminConstants.FRAGMENTS_BY_OBJECTNAME,
				PackageAdminConstants.HOSTS_BY_ID, PackageAdminConstants.HOSTS_BY_OBJECTNAME,
				PackageAdminConstants.REQUIREDBUNDLES_BY_SYMBOLICNAME, PackageAdminConstants.REFRESHPACKAGES_BY_IDS,
				PackageAdminConstants.REFRESHPACKAGES_BY_OBJECTNAME, PackageAdminConstants.REFRESHPACKAGES,
				PackageAdminConstants.RESOLVEBUNDLES_BY_ID, PackageAdminConstants.RESOLVEBUNDLES_BY_OBJECTNAME,
				PackageAdminConstants.RESOLVEBUNDLES };

		// constructors
		OpenMBeanConstructorInfoSupport[] mbeanConstructorInfos = new OpenMBeanConstructorInfoSupport[] {};
		// notifications
		MBeanNotificationInfo[] mbeanNotificationInfos = new MBeanNotificationInfo[] {};
		// mbean info
		OpenMBeanInfoSupport mbeanInfo = new OpenMBeanInfoSupport(className, PackageAdminConstants.MBEAN_DESCRIPTION,
				mbeanAttributeInfos, mbeanConstructorInfos, mbeanOperationInfos, mbeanNotificationInfos);
		return mbeanInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#getBundleType
	 * (javax.management.ObjectName)
	 */
	public Integer getBundleType(ObjectName objectName)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException {
		Long id = (Long) super.getMbeanServer().getAttribute(objectName,
				BundleConstants.ID.getName());
		return this.getBundleType(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#getBundleType
	 * (java.lang.Long)
	 */
	public Integer getBundleType(Long id) {
		org.osgi.framework.Bundle bundle = this.bundleContext.getBundle(id
				.longValue());
		return Integer.valueOf(this.packageAdmin.getBundleType(bundle));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#
	 * getBundleTypeAsName(javax.management.ObjectName)
	 */
	public String getBundleTypeAsName(ObjectName objectName)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException {
		return bundleTypes.get(this.getBundleType(objectName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#
	 * getBundleTypeAsName(java.lang.Long)
	 */
	public String getBundleTypeAsName(Long id) {
		return bundleTypes.get(this.getBundleType(id));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#getBundles
	 * (java.lang.String, java.lang.String)
	 */
	public ObjectName[] getBundles(String symbolicName, String versionRange) {
		org.osgi.framework.Bundle[] bundles = this.packageAdmin.getBundles(
				symbolicName, versionRange);
		if (bundles == null) {
			return null;
		}
		return this.getObjectNames(bundles);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#
	 * getExportedPackage(java.lang.String)
	 */
	public CompositeData getExportedPackage(String name) throws MBeanException {
		ExportedPackage exportedPackage = this.packageAdmin
				.getExportedPackage(name);
		return this.convertExportedPackage(exportedPackage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#
	 * getExportedPackages(javax.management.ObjectName)
	 */
	public TabularData getExportedPackages(ObjectName objectName)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException {
		Long id = (Long) super.getMbeanServer().getAttribute(objectName,
				BundleConstants.ID.getName());
		return this.getExportedPackages(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#
	 * getExportedPackages(java.lang.Long)
	 */
	public TabularData getExportedPackages(Long id) throws MBeanException {
		Bundle bundle = this.bundleContext.getBundle(id.longValue());
		ExportedPackage[] exportedPackages = this.packageAdmin
				.getExportedPackages(bundle);
		return this.convertExportedPackages(exportedPackages);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#
	 * getExportedPackages(java.lang.String)
	 */
	public TabularData getExportedPackages(String name) throws MBeanException {
		ExportedPackage[] exportedPackages = this.packageAdmin
				.getExportedPackages(name);
		return this.convertExportedPackages(exportedPackages);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#getFragments
	 * (javax.management.ObjectName)
	 */
	public ObjectName[] getFragments(ObjectName objectName)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException {
		Long id = (Long) super.getMbeanServer().getAttribute(objectName,
				BundleConstants.ID.getName());
		return this.getFragments(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#getFragments
	 * (java.lang.Long)
	 */
	public ObjectName[] getFragments(Long id) {
		Bundle bundle = this.bundleContext.getBundle(id.longValue());
		return this.getObjectNames(this.packageAdmin.getFragments(bundle));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#getHosts(
	 * javax.management.ObjectName)
	 */
	public ObjectName[] getHosts(ObjectName objectName)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException {
		Long id = (Long) super.getMbeanServer().getAttribute(objectName,
				BundleConstants.ID.getName());
		return this.getHosts(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#getHosts(
	 * java.lang.Long)
	 */
	public ObjectName[] getHosts(Long id) {
		Bundle bundle = this.bundleContext.getBundle(id.longValue());
		return this.getObjectNames(this.packageAdmin.getHosts(bundle));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#
	 * getRequiredBundles(java.lang.String)
	 */
	public TabularData getRequiredBundles(String symbolicName)
			throws MBeanException {
		RequiredBundle[] requiredBundles = this.packageAdmin
				.getRequiredBundles(symbolicName);
		if (requiredBundles == null) {
			return null;
		}
		TabularData tabularData = new TabularDataSupport(
				this.requiredBundlesType);
		for (RequiredBundle requiredBundle : requiredBundles) {
			try {
				tabularData
						.put(new CompositeDataSupport(
								this.requiredBundleType,
								PackageAdminConstants.REQUIREDBUNDLE_ITEM_NAMES,
								new Object[] {
										this.objectNameHelper.getObjectName(
												requiredBundle.getBundle(),
												Bundle.class),
										this.getObjectNames(requiredBundle
												.getRequiringBundles()),
										requiredBundle.getSymbolicName(),
										requiredBundle.getVersion().toString(),
										Boolean.valueOf(requiredBundle
												.isRemovalPending()) }));
			} catch (OpenDataException e) {
				throw new MBeanException(e);
			}
		}
		return tabularData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#refreshPackages
	 * (javax.management.ObjectName[])
	 */
	public void refreshPackages(ObjectName[] objectNames)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException {
		Long[] ids = new Long[objectNames.length];
		for (int i = 0; i < objectNames.length; i++) {
			ids[i] = (Long) super.getMbeanServer().getAttribute(objectNames[i],
					BundleConstants.ID.getName());
		}
		this.refreshPackages(ids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#refreshPackages
	 * (java.lang.Long[])
	 */
	public void refreshPackages(Long[] ids) {
		Bundle[] bundles = new Bundle[ids.length];
		for (int i = 0; i < ids.length; i++) {
			bundles[i] = this.bundleContext.getBundle(ids[i].longValue());
		}
		this.packageAdmin.refreshPackages(bundles);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#refreshPackages
	 * ()
	 */
	public void refreshPackages() {
		this.packageAdmin.refreshPackages(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#resolveBundles
	 * (javax.management.ObjectName[])
	 */
	public Boolean resolveBundles(ObjectName[] objectNames)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException {
		Long[] ids = new Long[objectNames.length];
		for (int i = 0; i < objectNames.length; i++) {
			ids[i] = (Long) super.getMbeanServer().getAttribute(objectNames[i],
					BundleConstants.ID.getName());
		}
		return this.resolveBundles(ids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#resolveBundles
	 * (java.lang.Long[])
	 */
	public Boolean resolveBundles(Long[] ids) {
		Bundle[] bundles = new Bundle[ids.length];
		for (int i = 0; i < ids.length; i++) {
			bundles[i] = this.bundleContext.getBundle(ids[i].longValue());
		}
		return Boolean.valueOf(this.packageAdmin.resolveBundles(bundles));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean#resolveBundles
	 * ()
	 */
	public Boolean resolveBundles() {
		return Boolean.valueOf(this.packageAdmin.resolveBundles(null));
	}

	/**
	 * Converts exported packages to tabular data
	 * 
	 * @param exportedPackages
	 *            the exported packages
	 * @return the tabular data
	 * @throws MBeanException
	 */
	private TabularData convertExportedPackages(
			ExportedPackage[] exportedPackages) throws MBeanException {
		TabularData tabularData = new TabularDataSupport(
				this.exportedPackagesType);
		for (ExportedPackage exportedPackage : exportedPackages) {
			tabularData.put(this.convertExportedPackage(exportedPackage));
		}
		return tabularData;
	}

	/**
	 * Converts an exported package to composite data
	 * 
	 * @param exportedPackage
	 *            the exported package
	 * @return the composite data
	 * @throws MBeanException
	 */
	@SuppressWarnings("deprecation")
	private CompositeData convertExportedPackage(ExportedPackage exportedPackage)
			throws MBeanException {
		try {
			return new CompositeDataSupport(
					this.exportedPackageType,
					PackageAdminConstants.EXPORTEDPACKAGE_ITEM_NAMES,
					new Object[] {
							this.objectNameHelper.getObjectName(exportedPackage
									.getExportingBundle(), Bundle.class),
							this.getObjectNames(exportedPackage
									.getImportingBundles()),
							exportedPackage.getName(),
							exportedPackage.getSpecificationVersion(),
							exportedPackage.getVersion().toString(),
							Boolean.valueOf(exportedPackage.isRemovalPending()) });
		} catch (OpenDataException e) {
			throw new MBeanException(e);
		}
	}

	/**
	 * Converts the given bundles to object names
	 * 
	 * @param bundles
	 *            the bundles
	 * @return the object names
	 */
	private ObjectName[] getObjectNames(Bundle[] bundles) {
		if (bundles == null) {
			return null;
		}
		ObjectName objectNames[] = new ObjectName[bundles.length];
		for (int i = 0; i < bundles.length; i++) {
			objectNames[i] = this.objectNameHelper.getObjectName(bundles[i],
					org.osgi.framework.Bundle.class);
		}
		return objectNames;
	}
}
