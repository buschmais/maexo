package com.buschmais.osgi.maexo.mbeans.framework.impl.objectname;

import java.util.Properties;

import javax.management.ObjectName;

import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;

import com.buschmais.osgi.maexo.core.objectname.ObjectNameFactory;
import com.buschmais.osgi.maexo.core.objectname.ObjectNameHelper;

/**
 * Object name factory implementation which supports service references
 */
public class BundleObjectNameFactory implements ObjectNameFactory {

	private static final String PROPERTY_TYPE_BUNDLE = "bundle";

	private static final String PROPERTY_VERSION = "version";

	private static final String DEFAULT_BUNDLE_SYMBOLICNAME = "unknown";

	private static final String DEFAULT_BUNDLE_VERSION = "0.0.0";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.maexo.core.registry.ObjectNameFactory#getObjectName(java
	 * .lang.Object)
	 */
	public ObjectName getObjectName(Object resource) {
		Bundle bundle = (Bundle) resource;
		// create object name properties
		Properties objectNameProperties = new Properties();
		// type
		objectNameProperties.setProperty(ObjectNameFactory.PROPERTY_TYPE,
				PROPERTY_TYPE_BUNDLE);
		// create name property: <symbolic name>_<version>
		String symbolicName = bundle.getSymbolicName();
		if (symbolicName == null) {
			symbolicName = DEFAULT_BUNDLE_SYMBOLICNAME;
		}
		objectNameProperties.setProperty(ObjectNameFactory.PROPERTY_NAME,
				symbolicName);
		// create version property
		String bundleVersion = (String) bundle.getHeaders().get(
				Constants.BUNDLE_VERSION);
		if (bundleVersion == null) {
			bundleVersion = DEFAULT_BUNDLE_VERSION;
		}
		objectNameProperties.setProperty(PROPERTY_VERSION, bundleVersion);
		return ObjectNameHelper.getObjectName(objectNameProperties);
	}
}
