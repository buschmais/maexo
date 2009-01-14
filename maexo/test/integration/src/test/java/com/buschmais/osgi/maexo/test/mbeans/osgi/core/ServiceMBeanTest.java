package com.buschmais.osgi.maexo.test.mbeans.osgi.core;

import java.util.Arrays;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;
import com.buschmais.osgi.maexo.mbeans.osgi.core.ServiceMBean;
import com.buschmais.osgi.maexo.test.Constants;
import com.buschmais.osgi.maexo.test.MaexoTests;

/**
 * This class tests ServiceMBeanFunctionality
 * 
 * @see MaexoTests
 */
public class ServiceMBeanTest extends MaexoTests {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String[] getTestBundlesNames() {
		return new String[] { Constants.ARTIFACT_SWITCHBOARD,
				Constants.ARTIFACT_PLATFORM_MBEAN_SERVER,
				Constants.ARTIFACT_COMMONS_MBEAN,
				Constants.ARTIFACT_OSGI_CORE_MBEAN,
				Constants.ARTIFACT_TESTBUNDLE };
	}

	/**
	 * Tests method <code>getBundle()</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getBundle() throws Exception {
		ServiceReference service = getTestService();
		ServiceMBean serviceMBean = getTestServiceMBean(service);

		ObjectName serviceMBeanObjectName = serviceMBean.getBundle();

		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				this.bundleContext);
		ObjectName serviceObjectName = objectNameFactoryHelper.getObjectName(
				service.getBundle(), Bundle.class);

		assertEquals(serviceMBeanObjectName, serviceObjectName);
	}

	/**
	 * Tests method <code>getUsingBundles()</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getUsingBundles() throws Exception {
		ServiceReference service = getTestService();
		ServiceMBean serviceMBean = getTestServiceMBean(service);

		ObjectName[] serviceMBeanUsingBundlesObjectNames = serviceMBean
				.getUsingBundles();

		Bundle[] usingBundles = service.getUsingBundles();
		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				this.bundleContext);

		for (int i = 0; i < usingBundles.length; i++) {
			ObjectName serviceObjectName = objectNameFactoryHelper
					.getObjectName(usingBundles[i], Bundle.class);
			assertEquals(serviceObjectName,
					serviceMBeanUsingBundlesObjectNames[i]);
		}
	}

	/**
	 * Tests method <code>getObjectClass()</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getObjectClass() throws Exception {
		ServiceReference service = getTestService();
		ServiceMBean serviceMBean = getTestServiceMBean(service);

		// get ObjectClass as String[]
		String[] serviceMBeanObjectClass = serviceMBean.getObjectClass();
		Object serviceObjectClass = service
				.getProperty(org.osgi.framework.Constants.OBJECTCLASS);

		// get String representations of both String[] and compare them
		String serviceObjectClassString = Arrays
				.deepToString((Object[]) serviceObjectClass);
		String serviceMBeanObjectClassString = Arrays
				.deepToString(serviceMBeanObjectClass);
		assertEquals(serviceObjectClassString, serviceMBeanObjectClassString);
	}

	/**
	 * Tests simple ServiceMBean attributes (
	 * <code>getId(), getRanking(), getPid(), getDescription(), getVendor()</code>
	 * ).
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_simpleAttributes() throws Exception {
		ServiceReference service = getTestService();
		ServiceMBean serviceMBean = getTestServiceMBean(service);
		assertEquals(service
				.getProperty(org.osgi.framework.Constants.SERVICE_ID),
				serviceMBean.getId());
		assertEquals(service
				.getProperty(org.osgi.framework.Constants.SERVICE_RANKING),
				serviceMBean.getRanking());
		assertEquals(service
				.getProperty(org.osgi.framework.Constants.SERVICE_PID),
				serviceMBean.getPid());
		assertEquals(service
				.getProperty(org.osgi.framework.Constants.SERVICE_DESCRIPTION),
				serviceMBean.getDescription());
		assertEquals(service
				.getProperty(org.osgi.framework.Constants.SERVICE_VENDOR),
				serviceMBean.getVendor());
	}

	/**
	 * Tests method <code>getProperties()</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getProperties() throws Exception {
		ServiceReference service = getTestService();
		ServiceMBean serviceMBean = getTestServiceMBean(service);
		// compare properties from ServiceReference and ServiceMBean
		TabularData properties = serviceMBean.getProperties();
		String[] keys = service.getPropertyKeys();
		// compare properties count
		assertEquals(properties.size(), keys.length);
		// check if ServiceReference and ServiceMBean hold the same value for a
		// property key
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			CompositeData cd = properties.get(new Object[] { key });
			Object serviceReferenceProperty = service.getProperty(key);
			// Arrays need extra step
			if (serviceReferenceProperty.getClass().isArray()) {
				serviceReferenceProperty = Arrays
						.deepToString((Object[]) serviceReferenceProperty);
			}
			// compare String representation
			assertEquals(cd.get("value").toString(), serviceReferenceProperty
					.toString());
		}
	}

	/**
	 * Returns a TestService from OSGI container for testing of general Service
	 * functionality.
	 * 
	 * @return the ServiceReference for the Service
	 * @throws InvalidSyntaxException
	 *             on error
	 */
	private ServiceReference getTestService() throws Exception {
		Bundle bundle = getTestBundle();
		ServiceReference[] servicesReferences = bundle.getServicesInUse();
		// for testing there is only one service used by this bundle
		ServiceReference reference = servicesReferences[0];
		return reference;
	}

	/**
	 * Returns a ServiceMBean for the given ServiceReference.
	 * 
	 * @param service
	 *            the ServiceReference
	 * @return the ServiceMBean
	 */
	private ServiceMBean getTestServiceMBean(ServiceReference service) {
		// get ObjectName for ServiceReference
		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				this.bundleContext);
		ObjectName objectName = objectNameFactoryHelper.getObjectName(service,
				ServiceReference.class);
		// get MBeanServer
		ServiceReference serviceReference = super.bundleContext
				.getServiceReference(MBeanServer.class.getName());
		MBeanServerConnection mbeanServer = (MBeanServer) super.bundleContext
				.getService(serviceReference);
		// get ServiceMBean
		final ServiceMBean serviceMBean = (ServiceMBean) MBeanServerInvocationHandler
				.newProxyInstance(mbeanServer, objectName, ServiceMBean.class,
						false);
		return serviceMBean;
	}

}
