/*
 * Copyright 2009 buschmais GbR
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
package com.buschmais.maexo.test.common.mbeans;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

import org.osgi.framework.Bundle;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;

import com.buschmais.maexo.framework.commons.mbean.objectname.ObjectNameFactory;
import com.buschmais.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;
import com.buschmais.maexo.test.Constants;
import com.buschmais.maexo.test.MaexoTests;

public class MaexoMBeanTests extends MaexoTests {

	/** Symbolic name for the test bundle. */
	private static final String TESTBUNDLE_SYMBOLIC_NAME = "maexo-test.testbundle";

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
				.getServiceReference(PackageAdmin.class.getName());
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
	 * Returns the object name for the given resource.
	 *
	 * @param resource
	 *            The resource.
	 * @param resourceInterface
	 *            The interface to use for looking up the object name factory.
	 * @param properties
	 *            The properties which are required by the specific
	 *            {@link ObjectNameFactory}.
	 * @return The object name.
	 */
	protected ObjectName getObjectName(Object resource,
			Class<?> resourceInterface, Map<String, Object> properties) {
		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				this.bundleContext);
		return objectNameFactoryHelper.getObjectName(resource,
				resourceInterface, properties);
	}

	/**
	 * Returns the instance of an {@link MBeanServerConnection} from the OSGi
	 * service registry.
	 *
	 * @return
	 */
	protected MBeanServerConnection getMBeanServerConnection() {
		// get MBeanServer
		ServiceReference serviceReference = bundleContext
				.getServiceReference(MBeanServer.class.getName());
		return (MBeanServer) bundleContext.getService(serviceReference);

	}

	/**
	 * Returns an MBean registered on MBean server under given object name and
	 * interface.
	 *
	 * @param objectName
	 *            Object name of the MBean.
	 * @param mbeanInterface
	 *            The interface of the MBean.
	 * @return The MBean.
	 */
	protected Object getMBean(ObjectName objectName, Class<?> mbeanInterface) {
		MBeanServerConnection mbeanServerConnection = this
				.getMBeanServerConnection();
		// get new MBean from MBeanServerConnection
		final Object mBean = MBeanServerInvocationHandler.newProxyInstance(
				mbeanServerConnection, objectName, mbeanInterface, false);
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
