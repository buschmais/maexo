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

import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanRegistration;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.RequiredBundle;

import com.buschmais.osgi.maexo.framework.commons.mbean.dynamic.DynamicMBeanSupport;
import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameHelper;

/**
 * Represents the OSGi package admin service.
 */
public final class PackageAdmin extends DynamicMBeanSupport implements
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
	 * The bundle context.
	 */
	private final BundleContext bundleContext;

	/**
	 * The package admin service.
	 */
	private final org.osgi.service.packageadmin.PackageAdmin packageAdmin;

	/**
	 * The object name helper.
	 */
	private final ObjectNameHelper objectNameHelper;

	/**
	 * Constructor.
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

	/**
	 * {@inheritDoc}
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

	/**
	 * {@inheritDoc}
	 */
	public Integer getBundleType(ObjectName objectName) {
		Long id;
		try {
			id = (Long) super.getMbeanServer().getAttribute(objectName, BundleConstants.ID.getName());
		} catch (Exception e) {
			throw new RuntimeException(String.format("cannot get attribute %s from mbean %s", BundleConstants.ID.getName(), objectName), e);
		}
		return this.getBundleType(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getBundleType(Long id) {
		org.osgi.framework.Bundle bundle = this.bundleContext.getBundle(id
				.longValue());
		return Integer.valueOf(this.packageAdmin.getBundleType(bundle));
	}

	/**
	 * {@inheritDoc}
	 */
	public String getBundleTypeAsName(ObjectName objectName) {
		return bundleTypes.get(this.getBundleType(objectName));
	}

	/**
	 * {@inheritDoc}
	 */
	public String getBundleTypeAsName(Long id) {
		return bundleTypes.get(this.getBundleType(id));
	}

	/**
	 * {@inheritDoc}
	 */
	public ObjectName[] getBundles(String symbolicName, String versionRange) {
		org.osgi.framework.Bundle[] bundles = this.packageAdmin.getBundles(
				symbolicName, versionRange);
		if (bundles == null) {
			return null;
		}
		return this.getObjectNames(bundles);
	}

	/**
	 * {@inheritDoc}
	 */
	public CompositeData getExportedPackage(String name) {
		ExportedPackage exportedPackage = this.packageAdmin
				.getExportedPackage(name);
		return this.convertExportedPackage(exportedPackage);
	}

	/**
	 * {@inheritDoc}
	 */
	public TabularData getExportedPackages(ObjectName objectName) {
		Long id;
		try {
			id = (Long) super.getMbeanServer().getAttribute(objectName, BundleConstants.ID.getName());
		} catch (Exception e) {
			throw new RuntimeException(String.format("cannot get attribute %s from mbean %s", BundleConstants.ID.getName(), objectName), e);
		}
		return this.getExportedPackages(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public TabularData getExportedPackages(Long id) {
		Bundle bundle = this.bundleContext.getBundle(id.longValue());
		ExportedPackage[] exportedPackages = this.packageAdmin
				.getExportedPackages(bundle);
		return this.convertExportedPackages(exportedPackages);
	}

	/**
	 * {@inheritDoc}
	 */
	public TabularData getExportedPackages(String name) {
		ExportedPackage[] exportedPackages = this.packageAdmin
				.getExportedPackages(name);
		return this.convertExportedPackages(exportedPackages);
	}

	/**
	 * {@inheritDoc}
	 */
	public ObjectName[] getFragments(ObjectName objectName) {
		Long id;
		try {
			id = (Long) super.getMbeanServer().getAttribute(objectName, BundleConstants.ID.getName());
		} catch (Exception e) {
			throw new RuntimeException(String.format("cannot get attribute %s from mbean %s", BundleConstants.ID.getName(), objectName), e);
		}
		return this.getFragments(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public ObjectName[] getFragments(Long id) {
		Bundle bundle = this.bundleContext.getBundle(id.longValue());
		return this.getObjectNames(this.packageAdmin.getFragments(bundle));
	}

	/**
	 * {@inheritDoc}
	 */
	public ObjectName[] getHosts(ObjectName objectName) {
		Long id;
		try {
			id = (Long) super.getMbeanServer().getAttribute(objectName, BundleConstants.ID.getName());
		} catch (Exception e) {
			throw new RuntimeException(String.format("cannot get attribute %s from mbean %s", BundleConstants.ID.getName(), objectName), e);
		}
		return this.getHosts(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public ObjectName[] getHosts(Long id) {
		Bundle bundle = this.bundleContext.getBundle(id.longValue());
		return this.getObjectNames(this.packageAdmin.getHosts(bundle));
	}

	/**
	 * {@inheritDoc}
	 */
	public TabularData getRequiredBundles(String symbolicName) {
		RequiredBundle[] requiredBundles = this.packageAdmin
				.getRequiredBundles(symbolicName);
		if (requiredBundles == null) {
			return null;
		}
		TabularData tabularData = new TabularDataSupport(
				PackageAdminConstants.REQUIRED_BUNDLES_TYPE);
		for (RequiredBundle requiredBundle : requiredBundles) {
			try {
				tabularData
						.put(new CompositeDataSupport(
								PackageAdminConstants.REQUIRED_BUNDLE_TYPE,
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
				throw new IllegalStateException(e);
			}
		}
		return tabularData;
	}

	/**
	 * {@inheritDoc}
	 */
	public void refreshPackages(ObjectName[] objectNames) {
		Long[] ids = new Long[objectNames.length];
		for (int i = 0; i < objectNames.length; i++) {
			try {
				ids[i] = (Long) super.getMbeanServer().getAttribute(objectNames[i], BundleConstants.ID.getName());
			} catch (Exception e) {
				throw new RuntimeException(String.format("cannot get attribute %s from mbean %s", BundleConstants.ID.getName(), objectNames[i]), e);
			}
		}
		this.refreshPackages(ids);
	}

	/**
	 * {@inheritDoc}
	 */
	public void refreshPackages(Long[] ids) {
		Bundle[] bundles = new Bundle[ids.length];
		for (int i = 0; i < ids.length; i++) {
			bundles[i] = this.bundleContext.getBundle(ids[i].longValue());
		}
		this.packageAdmin.refreshPackages(bundles);
	}

	/**
	 * {@inheritDoc}
	 */
	public void refreshPackages() {
		this.packageAdmin.refreshPackages(null);
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean resolveBundles(ObjectName[] objectNames) {
		Long[] ids = new Long[objectNames.length];
		for (int i = 0; i < objectNames.length; i++) {
			try {
				ids[i] = (Long) super.getMbeanServer().getAttribute(objectNames[i], BundleConstants.ID.getName());
			} catch (Exception e) {
				throw new RuntimeException(String.format("cannot get attribute %s from mbean %s", BundleConstants.ID.getName(), objectNames[i]), e);
			}
		}
		return this.resolveBundles(ids);
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean resolveBundles(Long[] ids) {
		Bundle[] bundles = new Bundle[ids.length];
		for (int i = 0; i < ids.length; i++) {
			bundles[i] = this.bundleContext.getBundle(ids[i].longValue());
		}
		return Boolean.valueOf(this.packageAdmin.resolveBundles(bundles));
	}

	/**
	 * {@inheritDoc}
	 */ 
	public Boolean resolveBundles() {
		return Boolean.valueOf(this.packageAdmin.resolveBundles(null));
	}

	/**
	 * Converts exported packages to tabular data.
	 * 
	 * @param exportedPackages
	 *            the exported packages
	 * @return the tabular data
	 */
	private TabularData convertExportedPackages(ExportedPackage[] exportedPackages) {
		TabularData tabularData = new TabularDataSupport(
				PackageAdminConstants.EXPORTED_PACKAGES_TYPE);
		for (ExportedPackage exportedPackage : exportedPackages) {
			tabularData.put(this.convertExportedPackage(exportedPackage));
		}
		return tabularData;
	}

	/**
	 * Converts an exported package to composite data.
	 * 
	 * @param exportedPackage
	 *            the exported package
	 * @return the composite data
	 */
	@SuppressWarnings("deprecation")
	private CompositeData convertExportedPackage(ExportedPackage exportedPackage) {
		try {
			return new CompositeDataSupport(
					PackageAdminConstants.EXPORTED_PACKAGE_TYPE,
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
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Converts the given bundles to object names.
	 * 
	 * @param bundles
	 *            the bundles
	 * @return the object names
	 */
	private ObjectName[] getObjectNames(Bundle[] bundles) {
		if (bundles == null) {
			return null;
		}
		ObjectName[] objectNames = new ObjectName[bundles.length];
		for (int i = 0; i < bundles.length; i++) {
			objectNames[i] = this.objectNameHelper.getObjectName(bundles[i],
					org.osgi.framework.Bundle.class);
		}
		return objectNames;
	}
}
