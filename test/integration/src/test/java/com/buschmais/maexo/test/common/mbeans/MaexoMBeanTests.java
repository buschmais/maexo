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
import org.osgi.service.packageadmin.PackageAdmin;

import com.buschmais.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;
import com.buschmais.maexo.test.Constants;
import com.buschmais.maexo.test.MaexoTests;

public class MaexoMBeanTests extends MaexoTests {
	
	/** Symbolic name for the test bundle. */
	private static final String TESTBUNDLE_SYMBOLIC_NAME = "maexo-test.testbundle";

	/** Object name for the switchboard notification listener. */
	protected final String SWITCHBOARDNOTIFICATIONLISTENER_OBJECTNAME = "JMImplementation:type=MBeanServerDelegate";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String[] getTestFrameworkBundlesNames() {
		return Constants.TEST_FRAMEWORK_BUNDLES_NAMES;
	}

	/**
	 * Returns a test bundle from OSGI container for testing of general bundle
	 * functionality.
	 * 
	 * @return The bundle.
	 * @throws InvalidSyntaxException
	 *             If no <code>org.osgi.service.packageadmin.PackageAdmin</code>
	 *             could be found.
	 */
	protected Bundle getTestBundle() throws InvalidSyntaxException {
		ServiceReference serviceReference = this.bundleContext
				.getServiceReference(PackageAdmin.class
						.getName());
		final PackageAdmin packageAdmin = (PackageAdmin) bundleContext
				.getService(serviceReference);
		Bundle[] bundles = packageAdmin.getBundles(TESTBUNDLE_SYMBOLIC_NAME,
				"0.0.0");
		assertTrue(bundles.length == 1);
		Bundle bundle = bundles[0];
		return bundle;
	}

	/**
	 * Returns the object name for the given resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @param resourceInterface
	 *            The interface to use for looking up the object name factory.
	 * @return The object name.
	 */
	protected ObjectName getObjectName(Object resource,
			Class<?> resourceInterface) {
		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				this.bundleContext);
		return objectNameFactoryHelper.getObjectName(resource,
				resourceInterface);
	}

	/**
	 * Returns an MBean registered on MBean server under given object name and
	 * interface.
	 * 
	 * @param objectName
	 *            Object name of the MBean.
	 * @param resourceInterface
	 *            The interface of the MBean.
	 * @return The MBean.
	 */
	protected Object getMBean(ObjectName objectName, Class<?> resourceInterface) {
		// get MBeanServer
		ServiceReference serviceReference = bundleContext
				.getServiceReference(MBeanServer.class.getName());
		MBeanServerConnection mbeanServer = (MBeanServer) bundleContext
				.getService(serviceReference);
		// get new MBean from MBeanServer
		final Object mBean = MBeanServerInvocationHandler.newProxyInstance(
				mbeanServer, objectName, resourceInterface, false);
		return mBean;
	}

	/**
	 * Returns the bundle specified by given location as byte array.
	 * 
	 * @param location
	 *            Location of the bundle.
	 * @return The bundle as byte array.
	 * @throws MalformedURLException
	 *             If location was malformed.
	 * @throws IOException
	 *             If bundle could not be found under given location.
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
