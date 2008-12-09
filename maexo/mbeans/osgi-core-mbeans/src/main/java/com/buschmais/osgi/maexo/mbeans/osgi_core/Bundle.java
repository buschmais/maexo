package com.buschmais.osgi.maexo.mbeans.osgi_core;

import javax.management.MBeanException;
import javax.management.ObjectName;
import javax.management.openmbean.OpenMBeanParameterInfo;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularData;

public interface Bundle {

	public static final String MBEAN_DESCRIPTION = "Managed bean for a bundle ";

	// attribute: id
	public static final String ATTRIBUTE_ID_NAME = "id";
	public static final String ATTRIBUTE_ID_DESCRIPTION = "The id of the bundle";

	/**
	 * Returns the id of the bundle
	 * 
	 * @return the id
	 */
	public Long getId();

	// attribute: state
	public static final String ATTRIBUTE_STATE_NAME = "state";
	public static final String ATTRIBUTE_STATE_DESCRIPTION = "The current state of bundle the bundle (integer value as reported by the framework)";

	/**
	 * Returns the numeric representation of the bundle state
	 * 
	 * @return the state
	 */
	public Integer getState();

	// attribute: stateName
	public static final String ATTRIBUTE_STATENAME_NAME = "stateName";
	public static final String ATTRIBUTE_STATENAME_DESCRIPTION = "The current state of bundle the bundle (human readable string value)";

	/**
	 * Returns the human readable representation of the bundle state
	 * 
	 * @return the state
	 */
	public String getStateName();

	// attribute: headers
	public static final String TABULARTYPE_HEADERS_NAME = "headers";
	public static final String TABULARTYPE_HEADERS_DESCRIPTION = "bundle headers";
	public static final String COMPOSITETYPE_HEADERS_ENTRY = "headerEntry";
	public static final String COMPOSITETYPE_HEADERS_ENTRY_DESCRIPTION = "bundle header entry";
	public static final String COMPOSITETYPE_HEADERS_NAME = "name";
	public static final String COMPOSITETYPE_HEADERS_VALUE = "value";
	public static final String ATTRIBUTE_HEADERS_NAME = "headers";
	public static final String ATTRIBUTE_HEADERS_DESCRIPTION = "The bundle headers";

	/**
	 * Returns the bundle's headers
	 * 
	 * @return the headers
	 * @throws MBeanException
	 */
	public TabularData getHeaders() throws MBeanException;

	// attribute: registeredServices
	public static final String ATTRIBUTE_REGISTEREDSERVICES_NAME = "registeredServices";
	public static final String ATTRIBUTE_REGISTEREDSERVICES_DESCRIPTION = "The services which were registered by this bundle";

	/**
	 * Returns the services which have been registered by this bundle
	 * 
	 * @return the services
	 */
	public ObjectName[] getRegisteredServices();

	// operation: start
	public static final String OPERATION_START_NAME = "start";
	public static final String OPERATION_START_DESCRIPTION = "Start the bundle";

	/**
	 * Start the bundle
	 * 
	 * @throws MBeanException
	 */
	public void start() throws MBeanException;

	// operation: stop
	public static final String OPERATION_STOP_NAME = "stop";
	public static final String OPERATION_STOP_DESCRIPTION = "Stop the bundle";

	/**
	 * Stop the bundle
	 * 
	 * @throws MBeanException
	 */
	public void stop() throws MBeanException;

	// operation: update
	public static final String OPERATION_UPDATE_NAME = "update";
	public static final String OPERATION_UPDATE_DESCRIPTION = "Update the bundle";

	/**
	 * Update the bundle
	 * 
	 * @throws MBeanException
	 */
	public void update() throws MBeanException;

	// operation: update from url
	public static final String OPERATION_UPDATEFROMURL_NAME = "update";
	public static final String OPERATION_UPDATEFROMURL_DESCRIPTION = "Update the bundle from the given url";
	public static final OpenMBeanParameterInfo[] OPERATION_UPDATEFROMURL_PARAMETERS = new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
			"url", "The url to update the bundle from", SimpleType.STRING) };

	/**
	 * Update the bundle from the provided url
	 * 
	 * @param url
	 *            the url
	 * @throws MBeanException
	 */
	public void update(String url) throws MBeanException;

	// operation: uninstall
	public static final String OPERATION_UNINSTALL_NAME = "uninstall";
	public static final String OPERATION_UNINSTALL_DESCRIPTION = "Uninstall the bundle";

	/**
	 * Uninstall the bundle
	 */
	public void uninstall() throws MBeanException;
}
