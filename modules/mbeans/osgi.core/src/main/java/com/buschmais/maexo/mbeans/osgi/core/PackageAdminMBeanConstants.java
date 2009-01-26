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

import java.util.Arrays;
import java.util.List;

import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularType;

import com.buschmais.maexo.framework.commons.mbean.dynamic.OpenTypeFactory;

/**
 * Class holding all constants for PackageAdminMBeans.
 */
public final class PackageAdminMBeanConstants {

	/**
	 * Private Constructor.
	 */
	private PackageAdminMBeanConstants() {

	}

	/** MBean object name format. */
	public static final String OBJECTNAME_FORMAT = "com.buschmais.maexo:type=PackageAdmin,id=%s";

	/** MBean description. */
	public static final String MBEAN_DESCRIPTION = "PackageAdmin MBean";

	/**
	 * <code>org.osgi.service.packageadmin.ExportedPackage</code> attribute
	 * exportingBundle.
	 */
	public static final String EXPORTEDPACKAGE_ITEM_EXPORTINGBUNDLE = "exportingBundle";

	/**
	 * <code>org.osgi.service.packageadmin.ExportedPackage</code> attribute
	 * importingBundle.
	 */
	public static final String EXPORTEDPACKAGE_ITEM_IMPORTINGBUNDLE = "importingBundle";

	/**
	 * <code>org.osgi.service.packageadmin.ExportedPackage</code> attribute
	 * name.
	 */
	public static final String EXPORTEDPACKAGE_ITEM_NAME = "name";

	/**
	 * <code>org.osgi.service.packageadmin.ExportedPackage</code> attribute
	 * specificationVersion.
	 */
	public static final String EXPORTEDPACKAGE_ITEM_SPECIFICATIONVERSION = "specificationVersion";

	/**
	 * <code>org.osgi.service.packageadmin.ExportedPackage</code> attribute
	 * version.
	 */
	public static final String EXPORTEDPACKAGE_ITEM_VERSION = "version";

	/**
	 * <code>org.osgi.service.packageadmin.ExportedPackage</code> attribute
	 * removalPending.
	 */
	public static final String EXPORTEDPACKAGE_ITEM_REMOVALPENDING = "removalPending";

	/**
	 * <code>org.osgi.service.packageadmin.RequiredBundle</code> attribute
	 * bundle.
	 */
	public static final String REQUIREDBUNDLE_ITEM_BUNDLE = "bundle";

	/**
	 * <code>org.osgi.service.packageadmin.RequiredBundle</code> attribute
	 * requiringBundles.
	 */
	public static final String REQUIREDBUNDLE_ITEM_REQUIRINGBUNDLES = "requiringBundles";

	/**
	 * <code>org.osgi.service.packageadmin.RequiredBundle</code> attribute
	 * symbolicName.
	 */
	public static final String REQUIREDBUNDLE_ITEM_SYMBOLICNAME = "symbolicName";

	/**
	 * <code>org.osgi.service.packageadmin.RequiredBundle</code> attribute
	 * version.
	 */
	public static final String REQUIREDBUNDLE_ITEM_VERSION = "version";

	/**
	 * <code>org.osgi.service.packageadmin.RequiredBundle</code> attribute
	 * removalPending.
	 */
	public static final String REQUIREDBUNDLE_ITEM_REMOVALPENDING = "removalPending";

	/** Exported package properties. */
	public static final List<String> EXPORTED_PACKAGE_ITEMS = Arrays
			.asList(new String[] { EXPORTEDPACKAGE_ITEM_EXPORTINGBUNDLE,
					EXPORTEDPACKAGE_ITEM_IMPORTINGBUNDLE,
					EXPORTEDPACKAGE_ITEM_NAME,
					EXPORTEDPACKAGE_ITEM_SPECIFICATIONVERSION,
					EXPORTEDPACKAGE_ITEM_VERSION,
					EXPORTEDPACKAGE_ITEM_REMOVALPENDING });

	/** Required bundle properties. */
	public static final List<String> REQUIRED_BUNDLE_ITEMS = Arrays
			.asList(new String[] { REQUIREDBUNDLE_ITEM_BUNDLE,
					REQUIREDBUNDLE_ITEM_REQUIRINGBUNDLES,
					REQUIREDBUNDLE_ITEM_SYMBOLICNAME,
					REQUIREDBUNDLE_ITEM_VERSION,
					REQUIREDBUNDLE_ITEM_REMOVALPENDING });

	/** Composite type representing one exported package. */
	public static final CompositeType EXPORTED_PACKAGE_TYPE = OpenTypeFactory
			.createCompositeType("exportedPackage", "exported package",
					EXPORTED_PACKAGE_ITEMS.toArray(new String[0]),
					EXPORTED_PACKAGE_ITEMS.toArray(new String[0]),
					new OpenType[] {
							SimpleType.OBJECTNAME,
							OpenTypeFactory.createArrayType(1,
									SimpleType.OBJECTNAME), SimpleType.STRING,
							SimpleType.STRING, SimpleType.STRING,
							SimpleType.BOOLEAN });

	/** Tabular type containing exported packages as composite type. */
	public static final TabularType EXPORTED_PACKAGES_TYPE = OpenTypeFactory
			.createTabularType("exportedPackages", "exported packages",
					PackageAdminMBeanConstants.EXPORTED_PACKAGE_TYPE,
					new String[] { "name" });

	/** Composite type representing one required bundle. */
	public static final CompositeType REQUIRED_BUNDLE_TYPE = OpenTypeFactory
			.createCompositeType("requiredBundle", "required bundle",
					REQUIRED_BUNDLE_ITEMS.toArray(new String[0]),
					REQUIRED_BUNDLE_ITEMS.toArray(new String[0]),
					new OpenType[] {
							SimpleType.OBJECTNAME,
							OpenTypeFactory.createArrayType(1,
									SimpleType.OBJECTNAME), SimpleType.STRING,
							SimpleType.STRING, SimpleType.BOOLEAN });

	/** Tabular type containing required bundles as composite type. */
	public static final TabularType REQUIRED_BUNDLES_TYPE = OpenTypeFactory
			.createTabularType("requiredBundles", "required bundles",
					PackageAdminMBeanConstants.REQUIRED_BUNDLE_TYPE,
					new String[] { "symbolicName" });

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#getBundles(String, String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETBUNDLES = new OpenMBeanOperationInfoSupport(
			"getBundles",
			"Returns the bundles with the specified symbolic name whose bundle version is within the specified version range.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("symbolicName",
							"The symbolic name of the desired bundles.",
							SimpleType.STRING),
					new OpenMBeanParameterInfoSupport(
							"versionRange",
							"The version range of the desired bundles, or null if all versions are desired.",
							SimpleType.STRING) }, OpenTypeFactory
					.createArrayType(1, SimpleType.OBJECTNAME),
			OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#getBundleType(Long)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETBUNDLETYPE_BY_ID = new OpenMBeanOperationInfoSupport(
			"getBundleType",
			"Returns the special type of the specified bundle.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"id", "The id of the bundle.", SimpleType.LONG) },
			SimpleType.INTEGER, OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#getBundleType(javax.management.ObjectName)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETBUNDLETYPE_BY_OBJECTNAME = new OpenMBeanOperationInfoSupport(
			"getBundleType",
			"Returns the special type of the specified bundle.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"objectName", "The object name of the bundle.",
					SimpleType.OBJECTNAME) }, SimpleType.INTEGER,
			OpenMBeanOperationInfoSupport.INFO);

	/**
	 * Enum type for bundle types for MBean operations
	 * {@link PackageAdminMBean#getBundleTypeAsName(Long)} and
	 * {@link PackageAdminMBean#getBundleTypeAsName(javax.management.ObjectName)}
	 * .
	 */
	public enum BundleType {
		FRAGMENT
	}

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#getBundleTypeAsName(Long)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETBUNDLETYPEASNAME_BY_ID = new OpenMBeanOperationInfoSupport(
			"getBundleTypeAsName",
			"Returns the special type of the specified bundle.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"id", "The id of the bundle.", SimpleType.LONG) },
			SimpleType.STRING, OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#getBundleTypeAsName(javax.management.ObjectName)}
	 * .
	 */
	public static final OpenMBeanOperationInfoSupport GETBUNDLETYPEASNAME_BY_OBJECTNAME = new OpenMBeanOperationInfoSupport(
			"getBundleTypeAsName",
			"Returns the special type of the specified bundle.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"objectName", "The object name of the bundle.",
					SimpleType.OBJECTNAME) }, SimpleType.STRING,
			OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#getExportedPackage(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport EXPORTEDPACKAGE_BY_OBJECTNAME = new OpenMBeanOperationInfoSupport(
			"getExportedPackage",
			"Gets the exported package for the specified package name.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the exported package to be returned.",
					SimpleType.STRING) },
			PackageAdminMBeanConstants.EXPORTED_PACKAGE_TYPE,
			OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#getExportedPackages(Long)}.
	 */
	public static final OpenMBeanOperationInfoSupport EXPORTEDPACKAGES_BY_ID = new OpenMBeanOperationInfoSupport(
			"getExportedPackages",
			"Gets the exported package for the specified package name.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"id", "The id of the bundle.", SimpleType.LONG) },
			PackageAdminMBeanConstants.EXPORTED_PACKAGES_TYPE,
			OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#getExportedPackages(javax.management.ObjectName)}
	 * .
	 */
	public static final OpenMBeanOperationInfoSupport EXPORTEDPACKAGES_BY_OBJECTNAME = new OpenMBeanOperationInfoSupport(
			"getExportedPackages",
			"Gets the exported package for the specified package name.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"objectName", "The object name of the bundle.",
					SimpleType.OBJECTNAME) },
			PackageAdminMBeanConstants.EXPORTED_PACKAGES_TYPE,
			OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#getExportedPackages(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport EXPORTEDPACKAGES_BY_NAME = new OpenMBeanOperationInfoSupport(
			"getExportedPackages",
			"Gets the exported package for the specified package name.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the exported package to be returned.",
					SimpleType.STRING) },
			PackageAdminMBeanConstants.EXPORTED_PACKAGES_TYPE,
			OpenMBeanOperationInfoSupport.ACTION_INFO);

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#getFragments(Long)}.
	 */
	public static final OpenMBeanOperationInfoSupport FRAGMENTS_BY_ID = new OpenMBeanOperationInfoSupport(
			"getFragments",
			"Returns the of attached fragment bundles for the specified bundle.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"id", "The id of the bundle.", SimpleType.LONG) },
			OpenTypeFactory.createArrayType(1, SimpleType.OBJECTNAME),
			OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#getFragments(javax.management.ObjectName)}.
	 */
	public static final OpenMBeanOperationInfoSupport FRAGMENTS_BY_OBJECTNAME = new OpenMBeanOperationInfoSupport(
			"getFragments",
			"Returns the of attached fragment bundles for the specified bundle.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"objectName", "The object name of the bundle.",
					SimpleType.OBJECTNAME) }, OpenTypeFactory.createArrayType(
					1, SimpleType.OBJECTNAME),
			OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#getHosts(Long)}.
	 */
	public static final OpenMBeanOperationInfoSupport HOSTS_BY_ID = new OpenMBeanOperationInfoSupport(
			"getHosts",
			"Returns the host bundle to which the specified fragment bundle is attached or null if the specified bundle is not attached to a host or is not a fragment bundle.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"id", "The id of the bundle.", SimpleType.LONG) },
			OpenTypeFactory.createArrayType(1, SimpleType.OBJECTNAME),
			OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#getHosts(javax.management.ObjectName)}.
	 */
	public static final OpenMBeanOperationInfoSupport HOSTS_BY_OBJECTNAME = new OpenMBeanOperationInfoSupport(
			"getHosts",
			"Returns the host bundle to which the specified fragment bundle is attached or null if the specified bundle is not attached to a host or is not a fragment bundle.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"objectName", "The object name of the bundle.",
					SimpleType.OBJECTNAME) }, OpenTypeFactory.createArrayType(
					1, SimpleType.OBJECTNAME),
			OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#getRequiredBundles(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport REQUIREDBUNDLES_BY_SYMBOLICNAME = new OpenMBeanOperationInfoSupport(
			"getRequiredBundles",
			"Returns an array of required bundles having the specified symbolic name.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"symbolicName",
					"The bundle symbolic name or null for all required bundles.",
					SimpleType.STRING) },
			PackageAdminMBeanConstants.REQUIRED_BUNDLES_TYPE,
			OpenMBeanOperationInfoSupport.INFO);

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#refreshPackages(Long[])}.
	 */
	public static final OpenMBeanOperationInfoSupport REFRESHPACKAGES_BY_IDS = new OpenMBeanOperationInfoSupport(
			"refreshPackages",
			"Forces the update (replacement) or removal of packages exported by the specified bundles.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"ids",
					"The ids of the bundles whose exported packages are to be updated or removed, or null for all bundles updated or uninstalled since the last call to this method.",
					OpenTypeFactory.createArrayType(1, SimpleType.LONG)) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#refreshPackages(javax.management.ObjectName[])}.
	 */
	public static final OpenMBeanOperationInfoSupport REFRESHPACKAGES_BY_OBJECTNAME = new OpenMBeanOperationInfoSupport(
			"refreshPackages",
			"Forces the update (replacement) or removal of packages exported by the specified bundles.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"objectNames",
					"The object names of the bundles whose exported packages are to be updated or removed, or null for all bundles updated or uninstalled since the last call to this method.",
					OpenTypeFactory.createArrayType(1, SimpleType.OBJECTNAME)) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#refreshPackages()}.
	 */
	public static final OpenMBeanOperationInfoSupport REFRESHPACKAGES = new OpenMBeanOperationInfoSupport(
			"refreshPackages",
			"Forces the update (replacement) or removal of packages exported by the specified bundles.",
			new OpenMBeanParameterInfoSupport[0], SimpleType.VOID,
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#resolveBundles(Long[])}.
	 */
	public static final OpenMBeanOperationInfoSupport RESOLVEBUNDLES_BY_ID = new OpenMBeanOperationInfoSupport(
			"resolveBundles",
			"Resolve the specified bundles.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"ids",
					"The ids of the bundles to resolve or null to resolve all unresolved bundles installed in the Framework.",
					OpenTypeFactory.createArrayType(1, SimpleType.LONG)) },
			SimpleType.BOOLEAN, OpenMBeanOperationInfoSupport.ACTION_INFO);

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#resolveBundles(javax.management.ObjectName[])}.
	 */
	public static final OpenMBeanOperationInfoSupport RESOLVEBUNDLES_BY_OBJECTNAME = new OpenMBeanOperationInfoSupport(
			"resolveBundles",
			"Resolve the specified bundles.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"objectNames",
					"The object names of the bundles to resolve or null to resolve all unresolved bundles installed in the Framework.",
					OpenTypeFactory.createArrayType(1, SimpleType.OBJECTNAME)) },
			SimpleType.BOOLEAN, OpenMBeanOperationInfoSupport.ACTION_INFO);

	/**
	 * MBean operation info for operation
	 * {@link PackageAdminMBean#resolveBundles()}.
	 */
	public static final OpenMBeanOperationInfoSupport RESOLVEBUNDLES = new OpenMBeanOperationInfoSupport(
			"resolveBundles", "Resolve the specified bundles.",
			new OpenMBeanParameterInfoSupport[0], SimpleType.BOOLEAN,
			OpenMBeanOperationInfoSupport.ACTION_INFO);

}
