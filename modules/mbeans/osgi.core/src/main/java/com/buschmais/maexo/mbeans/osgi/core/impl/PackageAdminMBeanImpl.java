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
package com.buschmais.maexo.mbeans.osgi.core.impl;

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
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.packageadmin.RequiredBundle;

import com.buschmais.maexo.framework.commons.mbean.dynamic.DynamicMBeanSupport;
import com.buschmais.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;
import com.buschmais.maexo.mbeans.osgi.core.BundleMBeanConstants;
import com.buschmais.maexo.mbeans.osgi.core.PackageAdminMBean;
import com.buschmais.maexo.mbeans.osgi.core.PackageAdminMBeanConstants;
import com.buschmais.maexo.mbeans.osgi.core.PackageAdminMBeanConstants.BundleType;

/**
 * Represents the OSGi package admin service.
 */
public final class PackageAdminMBeanImpl extends DynamicMBeanSupport implements
		PackageAdminMBean, MBeanRegistration {

	/** Translation map for bundle types. */
	private static Map<Integer, BundleType> bundleTypes;

	static {
		bundleTypes = new HashMap<Integer, BundleType>();
		bundleTypes
				.put(
						Integer
								.valueOf(org.osgi.service.packageadmin.PackageAdmin.BUNDLE_TYPE_FRAGMENT),
						BundleType.FRAGMENT);
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
	private final ObjectNameFactoryHelper objectNameFactoryHelper;

	/**
	 * Constructor.
	 * 
	 * @param bundleContext
	 *            The bundle context.
	 * @param packageAdmin
	 *            The package admin service.
	 */
	public PackageAdminMBeanImpl(BundleContext bundleContext,
			PackageAdmin packageAdmin) {
		this.bundleContext = bundleContext;
		this.packageAdmin = packageAdmin;
		this.objectNameFactoryHelper = new ObjectNameFactoryHelper(
				bundleContext);
	}

	/**
	 * {@inheritDoc}
	 */
	public MBeanInfo getMBeanInfo() {
		String className = PackageAdminMBeanImpl.class.getName();
		// attributes
		OpenMBeanAttributeInfoSupport[] mbeanAttributeInfos = new OpenMBeanAttributeInfoSupport[0];
		// operations
		OpenMBeanOperationInfoSupport[] mbeanOperationInfos = new OpenMBeanOperationInfoSupport[] {
				PackageAdminMBeanConstants.GETBUNDLES,
				PackageAdminMBeanConstants.GETBUNDLETYPE_BY_ID,
				PackageAdminMBeanConstants.GETBUNDLETYPE_BY_OBJECTNAME,
				PackageAdminMBeanConstants.GETBUNDLETYPEASNAME_BY_ID,
				PackageAdminMBeanConstants.GETBUNDLETYPEASNAME_BY_OBJECTNAME,
				PackageAdminMBeanConstants.EXPORTEDPACKAGE_BY_OBJECTNAME,
				PackageAdminMBeanConstants.EXPORTEDPACKAGES_BY_ID,
				PackageAdminMBeanConstants.EXPORTEDPACKAGES_BY_OBJECTNAME,
				PackageAdminMBeanConstants.EXPORTEDPACKAGES_BY_NAME,
				PackageAdminMBeanConstants.FRAGMENTS_BY_ID,
				PackageAdminMBeanConstants.FRAGMENTS_BY_OBJECTNAME,
				PackageAdminMBeanConstants.HOSTS_BY_ID,
				PackageAdminMBeanConstants.HOSTS_BY_OBJECTNAME,
				PackageAdminMBeanConstants.REQUIREDBUNDLES_BY_SYMBOLICNAME,
				PackageAdminMBeanConstants.REFRESHPACKAGES_BY_IDS,
				PackageAdminMBeanConstants.REFRESHPACKAGES_BY_OBJECTNAME,
				PackageAdminMBeanConstants.REFRESHPACKAGES,
				PackageAdminMBeanConstants.RESOLVEBUNDLES_BY_ID,
				PackageAdminMBeanConstants.RESOLVEBUNDLES_BY_OBJECTNAME,
				PackageAdminMBeanConstants.RESOLVEBUNDLES };

		// constructors
		OpenMBeanConstructorInfoSupport[] mbeanConstructorInfos = new OpenMBeanConstructorInfoSupport[] {};
		// notifications
		MBeanNotificationInfo[] mbeanNotificationInfos = new MBeanNotificationInfo[] {};
		// mbean info
		OpenMBeanInfoSupport mbeanInfo = new OpenMBeanInfoSupport(className,
				PackageAdminMBeanConstants.MBEAN_DESCRIPTION,
				mbeanAttributeInfos, mbeanConstructorInfos,
				mbeanOperationInfos, mbeanNotificationInfos);
		return mbeanInfo;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getBundleType(ObjectName objectName) {
		Long id = (Long) getAttribute(objectName, BundleMBeanConstants.ID
				.getName());
		return this.getBundleType(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getBundleType(Long id) {
		Bundle bundle = this.bundleContext.getBundle(id.longValue());
		if (null == bundle) {
			throw new IllegalArgumentException(String.format(
					"cannot get bundle for id %s", id));
		}
		return Integer.valueOf(this.packageAdmin.getBundleType(bundle));
	}

	/**
	 * {@inheritDoc}
	 */
	public String getBundleTypeAsName(ObjectName objectName) {
		BundleType bundleType = bundleTypes.get(this.getBundleType(objectName));
		return bundleType != null ? bundleType.toString() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getBundleTypeAsName(Long id) {
		BundleType bundleType = bundleTypes.get(this.getBundleType(id));
		return bundleType != null ? bundleType.toString() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public ObjectName[] getBundles(String symbolicName, String versionRange) {
		Bundle[] bundles = this.packageAdmin.getBundles(symbolicName,
				versionRange);
		if (bundles == null) {
			return null;
		}
		return this.objectNameFactoryHelper.getObjectNames(bundles,
				Bundle.class);
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
		Long id = (Long) getAttribute(objectName, BundleMBeanConstants.ID
				.getName());
		return this.getExportedPackages(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public TabularData getExportedPackages(Long id) {
		Bundle bundle = this.bundleContext.getBundle(id.longValue());
		if (null == bundle) {
			throw new IllegalArgumentException(String.format(
					"cannot get bundle for id %s", id));
		}
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
		Long id = (Long) getAttribute(objectName, BundleMBeanConstants.ID
				.getName());
		return this.getFragments(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public ObjectName[] getFragments(Long id) {
		Bundle bundle = this.bundleContext.getBundle(id.longValue());
		if (null == bundle) {
			throw new IllegalArgumentException(String.format(
					"cannot get bundle for id %s", id));
		}
		return this.objectNameFactoryHelper.getObjectNames(this.packageAdmin
				.getFragments(bundle), Bundle.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public ObjectName[] getHosts(ObjectName objectName) {
		Long id = (Long) getAttribute(objectName, BundleMBeanConstants.ID
				.getName());
		return this.getHosts(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public ObjectName[] getHosts(Long id) {
		Bundle bundle = this.bundleContext.getBundle(id.longValue());
		if (null == bundle) {
			throw new IllegalArgumentException(String.format(
					"cannot get bundle for id %s", id));
		}
		return this.objectNameFactoryHelper.getObjectNames(this.packageAdmin
				.getHosts(bundle), Bundle.class);
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
				PackageAdminMBeanConstants.REQUIRED_BUNDLES_TYPE);
		for (RequiredBundle requiredBundle : requiredBundles) {
			try {
				tabularData.put(new CompositeDataSupport(
						PackageAdminMBeanConstants.REQUIRED_BUNDLE_TYPE,
						PackageAdminMBeanConstants.REQUIRED_BUNDLE_ITEMS
								.toArray(new String[0]), new Object[] {
								this.objectNameFactoryHelper.getObjectName(
										requiredBundle.getBundle(),
										Bundle.class),
								this.objectNameFactoryHelper.getObjectNames(
										requiredBundle.getRequiringBundles(),
										Bundle.class),
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
			ids[i] = (Long) getAttribute(objectNames[i],
					BundleMBeanConstants.ID.getName());
		}
		this.refreshPackages(ids);
	}

	/**
	 * {@inheritDoc}
	 */
	public void refreshPackages(Long[] ids) {
		if (null == ids) {
			refreshPackages();
			return;
		}
		Bundle[] bundles = new Bundle[ids.length];
		for (int i = 0; i < ids.length; i++) {
			Bundle bundle = this.bundleContext.getBundle(ids[i].longValue());
			if (null == bundle) {
				throw new IllegalArgumentException(String.format(
						"cannot get bundle for id %s", ids[i]));
			}
			bundles[i] = bundle;
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
			ids[i] = (Long) getAttribute(objectNames[i],
					BundleMBeanConstants.ID.getName());
		}
		return this.resolveBundles(ids);
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean resolveBundles(Long[] ids) {
		if (null == ids) {
			return resolveBundles();
		}
		Bundle[] bundles = new Bundle[ids.length];
		for (int i = 0; i < ids.length; i++) {
			Bundle bundle = this.bundleContext.getBundle(ids[i].longValue());
			if (null == bundle) {
				throw new IllegalArgumentException(String.format(
						"cannot get bundle for id %s", ids[i]));
			}
			bundles[i] = bundle;
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
	 *            The exported packages.
	 * @return The tabular data or null if the exported packages are null.
	 */
	private TabularData convertExportedPackages(
			ExportedPackage[] exportedPackages) {
		if (null == exportedPackages) {
			return null;
		}
		TabularData tabularData = new TabularDataSupport(
				PackageAdminMBeanConstants.EXPORTED_PACKAGES_TYPE);
		for (ExportedPackage exportedPackage : exportedPackages) {
			tabularData.put(this.convertExportedPackage(exportedPackage));
		}
		return tabularData;
	}

	/**
	 * Converts an exported package to composite data.
	 * 
	 * @param exportedPackage
	 *            The exported package.
	 * @return The composite data or null if the exported package is null.
	 */
	@SuppressWarnings("deprecation")
	private CompositeData convertExportedPackage(ExportedPackage exportedPackage) {
		if (null == exportedPackage) {
			return null;
		}
		try {
			return new CompositeDataSupport(
					PackageAdminMBeanConstants.EXPORTED_PACKAGE_TYPE,
					PackageAdminMBeanConstants.EXPORTED_PACKAGE_ITEMS
							.toArray(new String[0]),
					new Object[] {
							this.objectNameFactoryHelper.getObjectName(
									exportedPackage.getExportingBundle(),
									Bundle.class),
							this.objectNameFactoryHelper.getObjectNames(
									exportedPackage.getImportingBundles(),
									Bundle.class), exportedPackage.getName(),
							exportedPackage.getSpecificationVersion(),
							exportedPackage.getVersion().toString(),
							Boolean.valueOf(exportedPackage.isRemovalPending()) });
		} catch (OpenDataException e) {
			throw new IllegalStateException(e);
		}
	}

}
