package com.buschmais.maexo.test.common.mbeans;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

import org.osgi.framework.Bundle;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import com.buschmais.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;
import com.buschmais.maexo.test.Constants;
import com.buschmais.maexo.test.MaexoTests;

public class MaexoMBeanTests extends MaexoTests {
	
	/** Symbolic name for the testbundle. */
	private static final String TESTBUNDLE_SYMBOLIC_NAME = "maexo-test.testbundle";

	/** Object name for the Switchboard notification listener. */
	protected final String SWITCHBOARDNOTIFICATIONLISTENER_OBJECTNAME = "JMImplementation:type=MBeanServerDelegate";
	
	/** Timeout for sleeping threads. */
	protected final int timeout = 5000;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String[] getTestFrameworkBundlesNames() {
		return Constants.TEST_FRAMEWORK_BUNDLES_NAMES;
	}

	/**
	 * Returns a TestBundle from OSGI container for testing of general Bundle
	 * functionality.
	 * 
	 * @return the bundle
	 * @throws InvalidSyntaxException
	 *             if no PackageAdmin could be found
	 */
	protected Bundle getTestBundle() throws InvalidSyntaxException {
		ServiceReference serviceReference = this.bundleContext
				.getServiceReference(org.osgi.service.packageadmin.PackageAdmin.class
						.getName());
		final org.osgi.service.packageadmin.PackageAdmin packageAdmin = (org.osgi.service.packageadmin.PackageAdmin) bundleContext
				.getService(serviceReference);
		Bundle[] bundles = packageAdmin.getBundles(TESTBUNDLE_SYMBOLIC_NAME,
				"0.0.0");
		assertTrue(bundles.length == 1);
		Bundle bundle = bundles[0];
		return bundle;
	}

	/**
	 * Returns the object name for the given Object.
	 * 
	 * @return the object name
	 */
	protected ObjectName getObjectName(Object o, Class<?> c) {
		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				this.bundleContext);
		return objectNameFactoryHelper.getObjectName(o, c);
	}

	/**
	 * Returns an MBean registered on MBeanServer under given object name and
	 * class type.
	 * 
	 * @param objectName
	 *            object name of the MBean
	 * @param clazz
	 *            class type of the MBean
	 * @return the MBean
	 */
	protected Object getMBean(ObjectName objectName, Class<?> clazz) {
		// get MBeanServer
		ServiceReference serviceReference = bundleContext
				.getServiceReference(MBeanServer.class.getName());
		MBeanServerConnection mbeanServer = (MBeanServer) bundleContext
				.getService(serviceReference);
		// get new MBean from MBeanServer
		final Object mBean = MBeanServerInvocationHandler.newProxyInstance(
				mbeanServer, objectName, clazz, false);
		return mBean;
	}

	/**
	 * Returns the bundle specified by given location as byte array.
	 * 
	 * @param location
	 *            location of the bundle
	 * @return the bundle as byte array
	 * @throws MalformedURLException
	 *             if location was malformed
	 * @throws IOException
	 *             if bundle could not be found under given location
	 */
	protected byte[] getByteArrayForBundleLocation(String location)
			throws MalformedURLException, IOException {
		URL url = new URL(location);
		InputStream inStream = url.openStream();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		final int arraySize = 4096;
		byte[] tempArray = new byte[arraySize];
		int readBytes = 0;
		do {
			readBytes = inStream.read(tempArray);
			outStream.write(tempArray, 0, readBytes);
		} while (readBytes == arraySize);
		byte[] bundleArray = outStream.toByteArray();
		return bundleArray;
	}
}
