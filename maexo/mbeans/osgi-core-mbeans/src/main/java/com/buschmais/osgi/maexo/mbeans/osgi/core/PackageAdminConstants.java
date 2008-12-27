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


public class PackageAdminConstants {

	/**
	 * object name properties: type and id
	 */
	public static final String OBJECTNAME_TYPE_PROPERTY = "type";
	public static final String OBJECTNAME_ID_PROPERTY = "id";

	/**
	 * value of the type property
	 */
	public static final String OBJECTNAME_TYPE_VALUE = "PackageAdmin";

	/**
	 * MBean description
	 */
	public static final String MBEAN_DESCRIPTION = "PackageAdmin MBean";

	/**
	 * type: exportedPackages
	 */
	public static final String TABULARTYPE_EXPORTEDPACKAGES_NAME = "exportedPackages";
	public static final String TABULARTYPE_EXPORTEDPACKAGES_DESCRIPTION = "exported packages";

	/**
	 * type: exportedPackage
	 */
	public static final String COMPOSITETYPE_EXPORTEDPACKAGE_ENTRY = "exportedPackage";
	public static final String COMPOSITETYPE_EXPORTEDPACKAGE_ENTRY_DESCRIPTION = "exported package";
	public static final String COMPOSITETYPE_EXPORTEDPACKAGE_NAME = "name";
	public static final String COMPOSITETYPE_EXPORTEDPACKAGE_EXPORTINGBUNDLE = "exportingBundle";
	public static final String COMPOSITETYPE_EXPORTEDPACKAGE_IMPORTINGBUNDLES = "importingBundle";
	public static final String COMPOSITETYPE_EXPORTEDPACKAGE_SPECIFICATIONVERSION = "specificationVersion";
	public static final String COMPOSITETYPE_EXPORTEDPACKAGE_VERSION = "version";
	public static final String COMPOSITETYPE_EXPORTEDPACKAGE_REMOVALPENDING = "removalPending";

	/**
	 * type: requiredBundles
	 */
	public static final String TABULARTYPE_REQUIREDBUNDLES_NAME = "requiredBundles";
	public static final String TABULARTYPE_REQUIREDBUNDLES_DESCRIPTION = "required bundles";

	/**
	 * type: requiredBundle
	 */
	public static final String COMPOSITETYPE_REQUIREDBUNDLE_ENTRY = "requiredBundle";
	public static final String COMPOSITETYPE_REQUIREDBUNDLE_ENTRY_DESCRIPTION = "required bundle";
	public static final String COMPOSITETYPE_REQUIREDBUNDLE_BUNDLE = "bundle";
	public static final String COMPOSITETYPE_REQUIREDBUNDLE_REQUIRINGBUNDLES = "requiringBundles";
	public static final String COMPOSITETYPE_REQUIREDBUNDLE_SYMBOLICNAME = "symbolicName";
	public static final String COMPOSITETYPE_REQUIREDBUNDLE_VERSION = "version";
	public static final String COMPOSITETYPE_REQUIREDBUNDLE_REMOVALPENDING = "removalPending";

	/**
	 * operation: ObjectName[] getBundles(String symbolicName, String
	 * versionRange)
	 */
	public static final String OPERATION_GETBUNDLES_NAME = "getBundles";
	public static final String OPERATION_GETBUNDLES_DESCRIPTION = "Returns the bundles with the specified symbolic name whose bundle version is within the specified version range.";
	public static final String OPERATION_GETBUNDLES_SYMBOLICNAME_PARAMETER = "symbolicName";
	public static final String OPERATION_GETBUNDLES_SYMBOLICNAME_DESCRIPTION = "The symbolic name of the desired bundles.";
	public static final String OPERATION_GETBUNDLES_VERSIONRANGE_PARAMETER = "versionRange";
	public static final String OPERATION_GETBUNDLES_VERSIONRANGE_DESCRIPTION = "The version range of the desired bundles, or null if all versions are desired.";

	/**
	 * operation: Integer getBundleType(Long id) and Integer
	 * getBundleType(ObjectName objectName)
	 */
	public static final String OPERATION_GETBUNDLETYPE_NAME = "getBundleType";
	public static final String OPERATION_GETBUNDLETYPE_DESCRIPTION = "Returns the special type of the specified bundle.";
	public static final String OPERATION_GETBUNDLETYPE_ID_PARAMETER = "id";
	public static final String OPERATION_GETBUNDLETYPE_ID_DESCRIPTION = "The id of the bundle.";
	public static final String OPERATION_GETBUNDLETYPE_OBJECTNAME_PARAMETER = "objectName";
	public static final String OPERATION_GETBUNDLETYPE_OBJECTNAME_DESCRIPTION = "The object name of the bundle.";

	/**
	 * operation: String getBundleTypeAsName(Long id) and String
	 * getBundleTypeAsName(ObjectName objectName)
	 */
	public static final String OPERATION_GETBUNDLETYPEASNAME_NAME = "getBundleTypeAsName";
	public static final String OPERATION_GETBUNDLETYPEASNAME_DESCRIPTION = "Returns the special type of the specified bundle.";
	public static final String OPERATION_GETBUNDLETYPEASNAME_ID_PARAMETER = "id";
	public static final String OPERATION_GETBUNDLETYPEASNAME_ID_DESCRIPTION = "The id of the bundle.";
	public static final String OPERATION_GETBUNDLETYPEASNAME_OBJECTNAME_PARAMETER = "objectName";
	public static final String OPERATION_GETBUNDLETYPEASNAME_OBJECTNAME_DESCRIPTION = "The object name of the bundle.";

	/**
	 * operation: TabularData getExportedPackage(String name)
	 */
	public static final String OPERATION_GETEXPORTEDPACKAGE_NAME = "getExportedPackage";
	public static final String OPERATION_GETEXPORTEDPACKAGE_DESCRIPTION = "Gets the exported package for the specified package name.";
	public static final String OPERATION_GETEXPORTEDPACKAGE_NAME_PARAMETER = "name";
	public static final String OPERATION_GETEXPORTEDPACKAGE_NAME_DESCRIPTION = "The name of the exported package to be returned.";

	/**
	 * operation: TabularData[] getExportedPackages(Long id), TabularData[]
	 * getExportedPackages(ObjectName objectName) and TabularData[]
	 * getExportedPackages(String name)
	 */
	public static final String OPERATION_GETEXPORTEDPACKAGES_NAME = "getExportedPackages";
	public static final String OPERATION_GETEXPORTEDPACKAGES_DESCRIPTION = "Gets the exported package for the specified package name.";
	public static final String OPERATION_GETEXPORTEDPACKAGES_ID_PARAMETER = "id";
	public static final String OPERATION_GETEXPORTEDPACKAGES_ID_DESCRIPTION = "The id of the bundle.";
	public static final String OPERATION_GETEXPORTEDPACKAGES_OBJECTNAME_PARAMETER = "objectName";
	public static final String OPERATION_GETEXPORTEDPACKAGES_OBJECTNAME_DESCRIPTION = "The object name of the bundle.";
	public static final String OPERATION_GETEXPORTEDPACKAGES_NAME_PARAMETER = "name";
	public static final String OPERATION_GETEXPORTEDPACKAGES_NAME_DESCRIPTION = "The name of the exported package to be returned.";

	/**
	 * operation: ObjectName[] getFragments(ObjectName objectName) and
	 * ObjectName[] getFragments(Long id)
	 */
	public static final String OPERATION_GETFRAGMENTS_NAME = "getFragments";
	public static final String OPERATION_GETFRAGMENTS_DESCRIPTION = "Returns the of attached fragment bundles for the specified bundle.";
	public static final String OPERATION_GETFRAGMENTS_ID_PARAMETER = "id";
	public static final String OPERATION_GETFRAGMENTS_ID_DESCRIPTION = "The id of the bundle.";
	public static final String OPERATION_GETFRAGMENTS_OBJECTNAME_PARAMETER = "objectName";
	public static final String OPERATION_GETFRAGMENTS_OBJECTNAME_DESCRIPTION = "The object name of the bundle.";

	/**
	 * operation: ObjectName[] getHosts(ObjectName objectName) and ObjectName[]
	 * getHosts(Long id)
	 */
	public static final String OPERATION_GETHOSTS_NAME = "getHosts";
	public static final String OPERATION_GETHOSTS_DESCRIPTION = "Returns the host bundle to which the specified fragment bundle is attached or null if the specified bundle is not attached to a host or is not a fragment bundle.";
	public static final String OPERATION_GETHOSTS_ID_PARAMETER = "id";
	public static final String OPERATION_GETHOSTS_ID_DESCRIPTION = "The id of the bundle.";
	public static final String OPERATION_GETHOSTS_OBJECTNAME_PARAMETER = "objectName";
	public static final String OPERATION_GETHOSTS_OBJECTNAME_DESCRIPTION = "The object name of the bundle.";

	/**
	 * operation: TabularData getRequiredBundles(String symbolicName)
	 */
	public static final String OPERATION_GETREQUIREDBUNDLES_NAME = "getRequiredBundles";
	public static final String OPERATION_GETREQUIREDBUNDLES_DESCRIPTION = "Returns an array of required bundles having the specified symbolic name.";
	public static final String OPERATION_GETREQUIREDBUNDLES_SYMBOLICNAME_PARAMETER = "symbolicName";
	public static final String OPERATION_GETREQUIREDBUNDLES_SYMBOLICNAME_DESCRIPTION = "The bundle symbolic name or null for all required bundles.";

	/**
	 * operation: void refreshPackages(Long[] ids) and void
	 * refreshPackages(ObjectName[] objectNames)
	 */
	public static final String OPERATION_REFRESHPACKAGES_NAME = "refreshPackages";
	public static final String OPERATION_REFRESHPACKAGES_DESCRIPTION = "Forces the update (replacement) or removal of packages exported by the specified bundles.";
	public static final String OPERATION_REFRESHPACKAGES_IDS_PARAMETER = "ids";
	public static final String OPERATION_REFRESHPACKAGES_IDS_DESCRIPTION = "The ids of the bundles whose exported packages are to be updated or removed, or null for all bundles updated or uninstalled since the last call to this method.";
	public static final String OPERATION_REFRESHPACKAGES_OBJECTNAMES_PARAMETER = "objectNames";
	public static final String OPERATION_REFRESHPACKAGES_OBJECTNAMES_DESCRIPTION = "The object names of the bundles whose exported packages are to be updated or removed, or null for all bundles updated or uninstalled since the last call to this method.";

	/**
	 * operation: Boolean resolveBundles(Long[] ids) and void
	 * resolveBundles(ObjectName[] objectNames)
	 */
	public static final String OPERATION_RESOLVEBUNDLES_NAME = "resolveBundles";
	public static final String OPERATION_RESOLVEBUNDLES_DESCRIPTION = "Resolve the specified bundles.";
	public static final String OPERATION_RESOLVEBUNDLES_IDS_PARAMETER = "ids";
	public static final String OPERATION_RESOLVEBUNDLES_IDS_DESCRIPTION = "The ids of the bundles to resolve or null to resolve all unresolved bundles installed in the Framework.";
	public static final String OPERATION_RESOLVEBUNDLES_OBJECTNAMES_PARAMETER = "objectNames";
	public static final String OPERATION_RESOLVEBUNDLES_OBJECTNAMES_DESCRIPTION = "The object names of the bundles to resolve or null to resolve all unresolved bundles installed in the Framework.";
}
