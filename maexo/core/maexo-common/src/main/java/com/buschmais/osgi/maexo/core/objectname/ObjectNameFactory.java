package com.buschmais.osgi.maexo.core.objectname;

import javax.management.ObjectName;

public interface ObjectNameFactory {

	public static final String DEFAULT_DOMAIN = "com.buschmais.maexo";

	public static final String SERVICE_PROPERTY_RESOURCEINTERFACE = "resourceInterface";

	public static final String PROPERTY_ID = "id";

	public static final String PROPERTY_NAME = "name";

	public static final String PROPERTY_TYPE = "type";

	/**
	 * Returns the object name for the given resource
	 * 
	 * @param resource
	 *            the resource
	 * @return the object name
	 */
	public ObjectName getObjectName(Object resource);

}
