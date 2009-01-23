package com.buschmais.maexo.mbeans.osgi.core;

import javax.management.ObjectName;

/**
 * Management interface for the OSGi framework.
 */
public interface FrameworkMBean {

	/**
	 * Returns the value of the framework's boot delegation property (see
	 * <code>org.osgi.framework.Constants.FRAMEWORK_BOOTDELEGATION</code>).
	 * 
	 * @return The value of the framework's boot delegation property.
	 */
	String getBootDelegation();

	/**
	 * Returns the value of the framework's execution environment property (see
	 * <code>org.osgi.framework.Constants.FRAMEWORK_EXECUTIONENVIRONMENT</code>
	 * ).
	 * 
	 * @return The value of the framework's execution environment property.
	 */
	String getExecutionEnvironment();

	/**
	 * Returns the value of the framework's language property (see
	 * <code>org.osgi.framework.Constants.FRAMEWORK_LANGUAGE</code>).
	 * 
	 * @return The value of the framework's language property.
	 */
	String getLanguage();

	/**
	 * Returns the value of framework's OS name property (see
	 * <code>org.osgi.framework.Constants.FRAMEWORK_OS_NAME</code>).
	 * 
	 * @return The value of the framework's OS name property.
	 */
	String getOsName();

	/**
	 * Returns the value of framework's OS version property (see
	 * <code>org.osgi.framework.Constants.FRAMEWORK_OS_VERSION</code>).
	 * 
	 * @return The value of the framework's OS version property.
	 */
	String getOsVersion();

	/**
	 * Returns the value of framework's processor property (see
	 * <code>org.osgi.framework.Constants.FRAMEWORK_PROCESSOR</code>).
	 * 
	 * @return The value of the framework's processor property.
	 */
	String getProcessor();

	/**
	 * Returns the value of the framework's system packages property (see
	 * <code>org.osgi.framework.Constants.FRAMEWORK_SYSTEMPACKAGES</code>).
	 * 
	 * @return The value of the framework's system packages property.
	 */
	String getSystemPackages();

	/**
	 * Returns the value of the framework's vendor property (see
	 * <code>org.osgi.framework.Constants.FRAMEWORK_VENDOR</code>).
	 * 
	 * @return The value of the framework's vendor property.
	 */
	String getVendor();

	/**
	 * Returns the value of the framework's version property (see
	 * <code>org.osgi.framework.Constants.FRAMEWORK_VERSION</code>).
	 * 
	 * @return The value of the framework's version property.
	 */
	String getVersion();

	/**
	 * Returns an array of {@link ObjectName}s of all installed bundles.
	 * 
	 * @return The array of {@link ObjectName}s of all installed bundles.
	 */
	ObjectName[] getBundles();

	/**
	 * Returns an array of {@link ObjectName}s of all registered services.
	 * 
	 * @return The array of {@link ObjectName}s of all registered services.
	 */
	ObjectName[] getServices();

	/**
	 * Returns an array of {@link ObjectName}s representing services which match
	 * the specified criteria.
	 * 
	 * @param objectClass
	 *            The class name with which the service was registered or null
	 *            for all services.
	 * @param filter
	 *            The filter criteria.
	 * @return An array of {@link ObjectName} objects or null if no services are
	 *         registered which satisfy the search.
	 */
	ObjectName[] getServices(String objectClass, String filter);

	/**
	 * Installs a bundle from the specified location string.
	 * 
	 * @param location
	 *            The location identifier of the bundle to install.
	 * @return The {@link ObjectName} representing the installed bundle.
	 */
	ObjectName installBundle(String location);

	/**
	 * Installs a bundle from the specified input byte array.
	 * 
	 * @param location
	 *            The location identifier of the bundle to install.
	 * @param input
	 *            The byte array from which this bundle will be read.
	 *             If the installation failed.
	 * @return The {@link ObjectName} representing the installed bundle.
	 */
	ObjectName installBundle(String location, byte[] input);
}
