package com.buschmais.maexo.test.mbeans.osgi.core;

import java.util.Arrays;

import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

import com.buschmais.maexo.mbeans.osgi.core.ServiceMBean;
import com.buschmais.maexo.test.Constants;
import com.buschmais.maexo.test.common.mbeans.MaexoMBeanTests;

/**
 * This class tests <code>ServiceMBean</code> functionality.
 * 
 * @see MaexoTests
 */
public class ServiceMBeanTest extends MaexoMBeanTests {

	
	/** The service reference. */
	private ServiceReference service;

	/** The service MBean. */
	private ServiceMBean serviceMBean;
	
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
	 * {@inheritDoc}
	 */
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		service = getTestService();
		serviceMBean = getTestServiceMBean(service);
	}

	/**
	 * Tests method {@link ServiceMBean#getBundle()}.
	 */
	public void test_getBundle() {
		ObjectName serviceMBeanObjectName = serviceMBean.getBundle();

		ObjectName serviceObjectName = getObjectName(service.getBundle(),
				Bundle.class);

		assertEquals(serviceMBeanObjectName, serviceObjectName);
	}

	/**
	 * Tests method {@link ServiceMBean#getUsingBundles()}.
	 */
	public void test_getUsingBundles() {
		ObjectName[] serviceMBeanUsingBundlesObjectNames = serviceMBean
				.getUsingBundles();

		Bundle[] usingBundles = service.getUsingBundles();

		for (int i = 0; i < usingBundles.length; i++) {
			ObjectName serviceObjectName = getObjectName(usingBundles[i],
					Bundle.class);
			assertEquals(serviceObjectName,
					serviceMBeanUsingBundlesObjectNames[i]);
		}
	}

	/**
	 * Tests method {@link ServiceMBean#getObjectClass()}.
	 */
	public void test_getObjectClass() {
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
	 * Tests simple <code>ServiceMBean</code> attributes (
	 * <code>getId(), getRanking(), getPid(), getDescription(), getVendor()</code>
	 * ).
	 */
	public void test_simpleAttributes() throws Exception {
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
	 * Tests method {@link ServiceMBean#getProperties()}.
	 */
	public void test_getProperties() {
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
	 * Returns a test service from OSGi container for testing of general service
	 * functionality.
	 * 
	 * @return The service reference for the service.
	 * @throws InvalidSyntaxException
	 *             On error.
	 */
	private ServiceReference getTestService() throws Exception {
		Bundle bundle = getTestBundle();
		ServiceReference[] servicesReferences = bundle.getServicesInUse();
		// for testing there is only one service used by this bundle
		ServiceReference reference = servicesReferences[0];
		return reference;
	}

	/**
	 * Returns a service MBean for the given service reference.
	 * 
	 * @param service
	 *            The service reference.
	 * @return The service MBean.
	 */
	private ServiceMBean getTestServiceMBean(ServiceReference service) {
		// get ObjectName for ServiceReference
		ObjectName objectName = getObjectName(service, ServiceReference.class);
		// get ServiceMBean
		final ServiceMBean serviceMBean = (ServiceMBean) getMBean(objectName,
				ServiceMBean.class);
		return serviceMBean;
	}

}
