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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanRegistration;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.ArrayType;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
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
 * MBean which represents the PackageAdmin service.
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
		try {
			this.exportedPackageType = new CompositeType(
					PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_ENTRY,
					PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_ENTRY_DESCRIPTION,
					new String[] {
							PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_EXPORTINGBUNDLE,
							PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_IMPORTINGBUNDLES,
							PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_NAME,
							PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_SPECIFICATIONVERSION,
							PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_VERSION,
							PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_REMOVALPENDING },
					new String[] {
							PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_EXPORTINGBUNDLE,
							PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_IMPORTINGBUNDLES,
							PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_NAME,
							PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_SPECIFICATIONVERSION,
							PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_VERSION,
							PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_REMOVALPENDING },
					new OpenType[] { SimpleType.OBJECTNAME,
							new ArrayType(1, SimpleType.OBJECTNAME),
							SimpleType.STRING, SimpleType.STRING,
							SimpleType.STRING, SimpleType.BOOLEAN });
			this.exportedPackagesType = new TabularType(
					PackageAdminConstants.TABULARTYPE_EXPORTEDPACKAGES_NAME,
					PackageAdminConstants.TABULARTYPE_EXPORTEDPACKAGES_DESCRIPTION,
					this.exportedPackageType,
					new String[] { PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_NAME });
			this.requiredBundleType = new CompositeType(
					PackageAdminConstants.COMPOSITETYPE_REQUIREDBUNDLE_ENTRY,
					PackageAdminConstants.COMPOSITETYPE_REQUIREDBUNDLE_ENTRY_DESCRIPTION,
					new String[] {
							PackageAdminConstants.COMPOSITETYPE_REQUIREDBUNDLE_BUNDLE,
							PackageAdminConstants.COMPOSITETYPE_REQUIREDBUNDLE_REQUIRINGBUNDLES,
							PackageAdminConstants.COMPOSITETYPE_REQUIREDBUNDLE_SYMBOLICNAME,
							PackageAdminConstants.COMPOSITETYPE_REQUIREDBUNDLE_VERSION,
							PackageAdminConstants.COMPOSITETYPE_REQUIREDBUNDLE_REMOVALPENDING },
					new String[] {
							PackageAdminConstants.COMPOSITETYPE_REQUIREDBUNDLE_BUNDLE,
							PackageAdminConstants.COMPOSITETYPE_REQUIREDBUNDLE_REQUIRINGBUNDLES,
							PackageAdminConstants.COMPOSITETYPE_REQUIREDBUNDLE_SYMBOLICNAME,
							PackageAdminConstants.COMPOSITETYPE_REQUIREDBUNDLE_VERSION,
							PackageAdminConstants.COMPOSITETYPE_REQUIREDBUNDLE_REMOVALPENDING },
					new OpenType[] { SimpleType.OBJECTNAME,
							new ArrayType(1, SimpleType.OBJECTNAME),
							SimpleType.STRING, SimpleType.STRING,
							SimpleType.BOOLEAN });
			this.requiredBundlesType = new TabularType(
					PackageAdminConstants.TABULARTYPE_REQUIREDBUNDLES_NAME,
					PackageAdminConstants.TABULARTYPE_REQUIREDBUNDLES_DESCRIPTION,
					this.requiredBundleType,
					new String[] { PackageAdminConstants.COMPOSITETYPE_REQUIREDBUNDLE_SYMBOLICNAME });
			String className = org.osgi.service.packageadmin.PackageAdmin.class
					.getName();
			// attributes
			OpenMBeanAttributeInfoSupport[] mbeanAttributeInfos = new OpenMBeanAttributeInfoSupport[0];
			// operations
			List<OpenMBeanOperationInfoSupport> operationList = new ArrayList<OpenMBeanOperationInfoSupport>();
			// ObjectName[] getBundles(String symbolicName, String versionRange)
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							PackageAdminConstants.OPERATION_GETBUNDLES_NAME,
							PackageAdminConstants.OPERATION_GETBUNDLES_DESCRIPTION,
							new OpenMBeanParameterInfoSupport[] {
									new OpenMBeanParameterInfoSupport(
											PackageAdminConstants.OPERATION_GETBUNDLES_SYMBOLICNAME_PARAMETER,
											PackageAdminConstants.OPERATION_GETBUNDLES_SYMBOLICNAME_DESCRIPTION,
											SimpleType.STRING),
									new OpenMBeanParameterInfoSupport(
											PackageAdminConstants.OPERATION_GETBUNDLES_VERSIONRANGE_PARAMETER,
											PackageAdminConstants.OPERATION_GETBUNDLES_VERSIONRANGE_DESCRIPTION,
											SimpleType.STRING) },
							new ArrayType(1, SimpleType.OBJECTNAME),
							OpenMBeanOperationInfoSupport.INFO));
			// Integer getBundleType(Long id)
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							PackageAdminConstants.OPERATION_GETBUNDLETYPE_NAME,
							PackageAdminConstants.OPERATION_GETBUNDLETYPE_DESCRIPTION,
							new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
									PackageAdminConstants.OPERATION_GETBUNDLETYPE_ID_PARAMETER,
									PackageAdminConstants.OPERATION_GETBUNDLETYPE_ID_DESCRIPTION,
									SimpleType.LONG) }, SimpleType.INTEGER,
							OpenMBeanOperationInfoSupport.INFO));
			// Integer getBundleType(ObjectName objectName)
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							PackageAdminConstants.OPERATION_GETBUNDLETYPE_NAME,
							PackageAdminConstants.OPERATION_GETBUNDLETYPE_DESCRIPTION,
							new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
									PackageAdminConstants.OPERATION_GETBUNDLETYPE_OBJECTNAME_PARAMETER,
									PackageAdminConstants.OPERATION_GETBUNDLETYPE_OBJECTNAME_DESCRIPTION,
									SimpleType.OBJECTNAME) },
							SimpleType.INTEGER,
							OpenMBeanOperationInfoSupport.INFO));
			// String getBundleTypeAsName(Long id)
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							PackageAdminConstants.OPERATION_GETBUNDLETYPEASNAME_NAME,
							PackageAdminConstants.OPERATION_GETBUNDLETYPEASNAME_DESCRIPTION,
							new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
									PackageAdminConstants.OPERATION_GETBUNDLETYPE_ID_PARAMETER,
									PackageAdminConstants.OPERATION_GETBUNDLETYPE_ID_DESCRIPTION,
									SimpleType.LONG) }, SimpleType.STRING,
							OpenMBeanOperationInfoSupport.INFO));
			// String getBundleTypeAsName(ObjectName objectName)
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							PackageAdminConstants.OPERATION_GETBUNDLETYPEASNAME_NAME,
							PackageAdminConstants.OPERATION_GETBUNDLETYPEASNAME_DESCRIPTION,
							new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
									PackageAdminConstants.OPERATION_GETBUNDLETYPE_OBJECTNAME_PARAMETER,
									PackageAdminConstants.OPERATION_GETBUNDLETYPE_OBJECTNAME_DESCRIPTION,
									SimpleType.OBJECTNAME) },
							SimpleType.STRING,
							OpenMBeanOperationInfoSupport.INFO));
			// CompositeType getExportedPackage(String name)
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							PackageAdminConstants.OPERATION_GETEXPORTEDPACKAGE_NAME,
							PackageAdminConstants.OPERATION_GETEXPORTEDPACKAGE_DESCRIPTION,
							new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
									PackageAdminConstants.OPERATION_GETEXPORTEDPACKAGE_NAME_PARAMETER,
									PackageAdminConstants.OPERATION_GETEXPORTEDPACKAGE_NAME_DESCRIPTION,
									SimpleType.STRING) },
							this.exportedPackageType,
							OpenMBeanOperationInfoSupport.INFO));
			// TabularData getExportedPackages(Long id)
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							PackageAdminConstants.OPERATION_GETEXPORTEDPACKAGES_NAME,
							PackageAdminConstants.OPERATION_GETEXPORTEDPACKAGES_DESCRIPTION,
							new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
									PackageAdminConstants.OPERATION_GETEXPORTEDPACKAGES_ID_PARAMETER,
									PackageAdminConstants.OPERATION_GETEXPORTEDPACKAGES_ID_DESCRIPTION,
									SimpleType.LONG) },
							this.exportedPackagesType,
							OpenMBeanOperationInfoSupport.INFO));
			// TabularData getExportedPackages(ObjectName objectName)
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							PackageAdminConstants.OPERATION_GETEXPORTEDPACKAGES_NAME,
							PackageAdminConstants.OPERATION_GETEXPORTEDPACKAGES_DESCRIPTION,
							new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
									PackageAdminConstants.OPERATION_GETEXPORTEDPACKAGES_OBJECTNAME_PARAMETER,
									PackageAdminConstants.OPERATION_GETEXPORTEDPACKAGES_OBJECTNAME_DESCRIPTION,
									SimpleType.OBJECTNAME) },
							this.exportedPackagesType,
							OpenMBeanOperationInfoSupport.INFO));
			// TabularData getExportedPackages(String name)
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							PackageAdminConstants.OPERATION_GETEXPORTEDPACKAGES_NAME,
							PackageAdminConstants.OPERATION_GETEXPORTEDPACKAGES_DESCRIPTION,
							new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
									PackageAdminConstants.OPERATION_GETEXPORTEDPACKAGES_NAME_PARAMETER,
									PackageAdminConstants.OPERATION_GETEXPORTEDPACKAGES_NAME_DESCRIPTION,
									SimpleType.STRING) },
							this.exportedPackagesType,
							OpenMBeanOperationInfoSupport.ACTION_INFO));
			// ObjectName[] getFragments(Long id)
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							PackageAdminConstants.OPERATION_GETFRAGMENTS_NAME,
							PackageAdminConstants.OPERATION_GETFRAGMENTS_DESCRIPTION,
							new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
									PackageAdminConstants.OPERATION_GETFRAGMENTS_ID_PARAMETER,
									PackageAdminConstants.OPERATION_GETFRAGMENTS_ID_DESCRIPTION,
									SimpleType.LONG) }, new ArrayType(1,
									SimpleType.OBJECTNAME),
							OpenMBeanOperationInfoSupport.INFO));
			// ObjectName[] getFragments(ObjectName objectName)
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							PackageAdminConstants.OPERATION_GETFRAGMENTS_NAME,
							PackageAdminConstants.OPERATION_GETFRAGMENTS_DESCRIPTION,
							new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
									PackageAdminConstants.OPERATION_GETFRAGMENTS_OBJECTNAME_PARAMETER,
									PackageAdminConstants.OPERATION_GETFRAGMENTS_OBJECTNAME_DESCRIPTION,
									SimpleType.OBJECTNAME) }, new ArrayType(1,
									SimpleType.OBJECTNAME),
							OpenMBeanOperationInfoSupport.INFO));
			// ObjectName[] getHosts(Long id)
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							PackageAdminConstants.OPERATION_GETHOSTS_NAME,
							PackageAdminConstants.OPERATION_GETHOSTS_DESCRIPTION,
							new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
									PackageAdminConstants.OPERATION_GETHOSTS_ID_PARAMETER,
									PackageAdminConstants.OPERATION_GETHOSTS_ID_DESCRIPTION,
									SimpleType.LONG) }, new ArrayType(1,
									SimpleType.OBJECTNAME),
							OpenMBeanOperationInfoSupport.INFO));
			// ObjectName[] getHosts(ObjectName objectName)
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							PackageAdminConstants.OPERATION_GETHOSTS_NAME,
							PackageAdminConstants.OPERATION_GETHOSTS_DESCRIPTION,
							new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
									PackageAdminConstants.OPERATION_GETHOSTS_OBJECTNAME_PARAMETER,
									PackageAdminConstants.OPERATION_GETHOSTS_OBJECTNAME_DESCRIPTION,
									SimpleType.OBJECTNAME) }, new ArrayType(1,
									SimpleType.OBJECTNAME),
							OpenMBeanOperationInfoSupport.INFO));
			// TabularData getRequiredBundles(String symbolicName)
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							PackageAdminConstants.OPERATION_GETREQUIREDBUNDLES_NAME,
							PackageAdminConstants.OPERATION_GETREQUIREDBUNDLES_DESCRIPTION,
							new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
									PackageAdminConstants.OPERATION_GETREQUIREDBUNDLES_SYMBOLICNAME_PARAMETER,
									PackageAdminConstants.OPERATION_GETREQUIREDBUNDLES_SYMBOLICNAME_DESCRIPTION,
									SimpleType.STRING) },
							this.requiredBundlesType,
							OpenMBeanOperationInfoSupport.INFO));
			// void refreshPackages(Long[] ids)
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							PackageAdminConstants.OPERATION_REFRESHPACKAGES_NAME,
							PackageAdminConstants.OPERATION_REFRESHPACKAGES_DESCRIPTION,
							new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
									PackageAdminConstants.OPERATION_REFRESHPACKAGES_IDS_PARAMETER,
									PackageAdminConstants.OPERATION_REFRESHPACKAGES_IDS_DESCRIPTION,
									new ArrayType(1, SimpleType.LONG)) },
							SimpleType.VOID,
							OpenMBeanOperationInfoSupport.ACTION));
			// void refreshPackages(ObjectName[] objectName)
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							PackageAdminConstants.OPERATION_REFRESHPACKAGES_NAME,
							PackageAdminConstants.OPERATION_REFRESHPACKAGES_DESCRIPTION,
							new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
									PackageAdminConstants.OPERATION_REFRESHPACKAGES_OBJECTNAMES_PARAMETER,
									PackageAdminConstants.OPERATION_REFRESHPACKAGES_OBJECTNAMES_DESCRIPTION,
									new ArrayType(1, SimpleType.OBJECTNAME)) },
							SimpleType.VOID,
							OpenMBeanOperationInfoSupport.ACTION));
			// void refreshPackages()
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							PackageAdminConstants.OPERATION_REFRESHPACKAGES_NAME,
							PackageAdminConstants.OPERATION_REFRESHPACKAGES_DESCRIPTION,
							new OpenMBeanParameterInfoSupport[0],
							SimpleType.VOID,
							OpenMBeanOperationInfoSupport.ACTION));
			// void resolveBundles(Long[] ids)
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							PackageAdminConstants.OPERATION_RESOLVEBUNDLES_NAME,
							PackageAdminConstants.OPERATION_RESOLVEBUNDLES_DESCRIPTION,
							new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
									PackageAdminConstants.OPERATION_RESOLVEBUNDLES_IDS_PARAMETER,
									PackageAdminConstants.OPERATION_RESOLVEBUNDLES_IDS_DESCRIPTION,
									new ArrayType(1, SimpleType.LONG)) },
							SimpleType.BOOLEAN,
							OpenMBeanOperationInfoSupport.ACTION_INFO));
			// void resolveBundles(ObjectName[] objectName)
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							PackageAdminConstants.OPERATION_RESOLVEBUNDLES_NAME,
							PackageAdminConstants.OPERATION_RESOLVEBUNDLES_DESCRIPTION,
							new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
									PackageAdminConstants.OPERATION_RESOLVEBUNDLES_OBJECTNAMES_PARAMETER,
									PackageAdminConstants.OPERATION_RESOLVEBUNDLES_OBJECTNAMES_DESCRIPTION,
									new ArrayType(1, SimpleType.OBJECTNAME)) },
							SimpleType.BOOLEAN,
							OpenMBeanOperationInfoSupport.ACTION_INFO));
			// void resolveBundles()
			operationList.add(new OpenMBeanOperationInfoSupport(
					PackageAdminConstants.OPERATION_RESOLVEBUNDLES_NAME,
					PackageAdminConstants.OPERATION_RESOLVEBUNDLES_DESCRIPTION,
					new OpenMBeanParameterInfoSupport[0], SimpleType.BOOLEAN,
					OpenMBeanOperationInfoSupport.ACTION_INFO));
			OpenMBeanOperationInfoSupport[] mbeanOperationInfos = operationList
					.toArray(new OpenMBeanOperationInfoSupport[0]);
			// constructors
			OpenMBeanConstructorInfoSupport[] mbeanConstructorInfos = new OpenMBeanConstructorInfoSupport[] {};
			// notifications
			MBeanNotificationInfo[] mbeanNotificationInfos = new MBeanNotificationInfo[] {};
			// mbean info
			OpenMBeanInfoSupport mbeanInfo = new OpenMBeanInfoSupport(
					className, PackageAdminConstants.MBEAN_DESCRIPTION,
					mbeanAttributeInfos, mbeanConstructorInfos,
					mbeanOperationInfos, mbeanNotificationInfos);
			return mbeanInfo;
		} catch (OpenDataException e) {
			throw new RuntimeException("cannot construct mbean info", e);
		}
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
				BundleConstants.ATTRIBUTE_ID_NAME);
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
				BundleConstants.ATTRIBUTE_ID_NAME);
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
				BundleConstants.ATTRIBUTE_ID_NAME);
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
				BundleConstants.ATTRIBUTE_ID_NAME);
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
								new String[] {
										PackageAdminConstants.COMPOSITETYPE_REQUIREDBUNDLE_BUNDLE,
										PackageAdminConstants.COMPOSITETYPE_REQUIREDBUNDLE_REQUIRINGBUNDLES,
										PackageAdminConstants.COMPOSITETYPE_REQUIREDBUNDLE_SYMBOLICNAME,
										PackageAdminConstants.COMPOSITETYPE_REQUIREDBUNDLE_VERSION,
										PackageAdminConstants.COMPOSITETYPE_REQUIREDBUNDLE_REMOVALPENDING },
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
					BundleConstants.ATTRIBUTE_ID_NAME);
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
					BundleConstants.ATTRIBUTE_ID_NAME);
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
					new String[] {
							PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_EXPORTINGBUNDLE,
							PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_IMPORTINGBUNDLES,
							PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_NAME,
							PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_SPECIFICATIONVERSION,
							PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_VERSION,
							PackageAdminConstants.COMPOSITETYPE_EXPORTEDPACKAGE_REMOVALPENDING },
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
