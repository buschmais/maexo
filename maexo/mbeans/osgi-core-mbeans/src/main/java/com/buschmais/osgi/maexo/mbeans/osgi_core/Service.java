package com.buschmais.osgi.maexo.mbeans.osgi_core;

import javax.management.MBeanException;
import javax.management.ObjectName;
import javax.management.openmbean.TabularData;

public interface Service {
	public static final String MBEAN_DESCRIPTION = "Managed bean for a service";

	// attribute: bundle
	public static final String ATTRIBUTE_BUNDLE_NAME = "bundle";
	public static final String ATTRIBUTE_BUNDLE_DESCRIPTION = "The bundle which registered this service";

	/**
	 * Returns the bundle which registered this service
	 * 
	 * @return the bundle
	 */
	public ObjectName getBundle();

	// attribute: id
	public static final String ATTRIBUTE_ID_NAME = "id";
	public static final String ATTRIBUTE_ID_DESCRIPTION = "The id of this service";

	/**
	 * Returns the id of this service
	 * 
	 * @return the id
	 */
	public Long getId();

	// attribute: description
	public static final String ATTRIBUTE_DESCRIPTION_NAME = "description";
	public static final String ATTRIBUTE_DESCRIPTION_DESCRIPTION = "The description of this service";

	/**
	 * Returns the description of this service
	 * 
	 * @return the description
	 */
	public String getDescription();

	// attribute: objectClass
	public static final String ATTRIBUTE_OBJECTCLASS_NAME = "objectClass";
	public static final String ATTRIBUTE_OBJECTCLASS_DESCRIPTION = "The object class(es) this services implements";

	/**
	 * Returns the object class(es) of this service
	 * 
	 * @return the object class(es)
	 */
	public String[] getObjectClass();

	// attribute: pid
	public static final String ATTRIBUTE_PID_NAME = "pid";
	public static final String ATTRIBUTE_PID_DESCRIPTION = "The persistent id of this service";

	/**
	 * Returns the persistent id of this services
	 * 
	 * @return the persistent id
	 */
	public String getPid();

	// attribute: properties
	public static final String ATTRIBUTE_PROPERTIES_NAME = "properties";
	public static final String ATTRIBUTE_PROPERTIES_DESCRIPTION = "The properties of this service";
	public static final String COMPOSITETYPE_PROPERTIES_ENTRY = "propertyEntry";
	public static final String COMPOSITETYPE_PROPERTIES_ENTRY_DESCRIPTION = "A service property entry";
	public static final String COMPOSITETYPE_PROPERTIES_NAME = "name";
	public static final String COMPOSITETYPE_PROPERTIES_VALUE = "value";
	public static final String TABULARTYPE_PROPERTIES_NAME = "properties";
	public static final String TABULARTYPE_PROPERTIES_DESCRIPTION = "The service properties";

	/**
	 * Returns the properties of this service
	 * 
	 * @return the properties
	 * @throws MBeanException
	 */
	public TabularData getProperties() throws MBeanException;

	// attribute:ranking
	public static final String ATTRIBUTE_RANKING_NAME = "ranking";
	public static final String ATTRIBUTE_RANKING_DESCRIPTION = "The ranking of this service";

	/**
	 * Returns the ranking of this service
	 * 
	 * @return the ranking
	 */
	public Integer getRanking();

	// attribute: usingBundles
	public static final String ATTRIBUTE_USINGBUNDLES_NAME = "usingBundles";
	public static final String ATTRIBUTE_USINGBUNDLES_DESCRIPTION = "The bundle which are currrently using this service";

	/**
	 * Returns the bundles which are using this service
	 * 
	 * @return the using bundles
	 */
	public ObjectName[] getUsingBundles();

	// attribute: vendor
	public static final String ATTRIBUTE_VENDOR_NAME = "vendor";
	public static final String ATTRIBUTE_VENDOR_DESCRIPTION = "The vendor of this service";

	/**
	 * Returns the vendor of this service
	 * 
	 * @return the vendor
	 */
	public String getVendor();

}
