package com.buschmais.maexo.test.mbeans.osgi.core;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import com.buschmais.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;
import com.buschmais.maexo.mbeans.osgi.core.BundleMBean;
import com.buschmais.maexo.mbeans.osgi.core.FrameworkMBean;
import com.buschmais.maexo.test.Constants;
import com.buschmais.maexo.test.common.mbeans.MaexoMBeanTests;
import com.buschmais.maexo.test.testbundle.TestInterface;

public class FrameworkMBeanTest extends MaexoMBeanTests implements
		NotificationListener {

	/** FrameworkMBean. */
	private FrameworkMBean frameworkMBean;

	/** The TestBundle. */
	private Bundle bundle;

	/** Name of the test service. */
	private final Class<TestInterface> testService = TestInterface.class;

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
		bundle = getTestBundle();
		frameworkMBean = getFrameworkMBean();
	}

	/**
	 * {@inheritDoc}
	 */
	public void handleNotification(Notification notification, Object handback) {
		synchronized (this) {
			this.notify();
		}
	}

	/**
	 * Returns a FrameworkMBean for the given BundleContext.
	 * 
	 * @return the getFrameworkMBean
	 */
	private FrameworkMBean getFrameworkMBean() {
		// get property SERVICE_ID which is needed for ObjectName lookup
		ServiceRegistration serviceRegistrationBundleContext = bundleContext
				.registerService(BundleContext.class.getName(), bundleContext,
						null);
		Object serviceId = serviceRegistrationBundleContext.getReference()
				.getProperty(org.osgi.framework.Constants.SERVICE_ID);
		// put SERVICE_ID property into properties-Map
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(org.osgi.framework.Constants.SERVICE_ID, serviceId);
		// get ObjectName for FrameworkMBean
		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				this.bundleContext);
		ObjectName objectName = objectNameFactoryHelper.getObjectName(
				bundleContext, BundleContext.class, properties);
		final FrameworkMBean frameworkMBean = (FrameworkMBean) getMBean(
				objectName, FrameworkMBean.class);
		return frameworkMBean;
	}

	/**
	 * Tests simple FrameworkMBean attributes (
	 * <code>getBootDelegation(), getExecutionEnvironment(), getLanguage(), getOsName(), getOsVersion(), getProcessor(), 
	 * getSystemPackages(), getVendor(), getVersion()</code> ).
	 */
	public void test_simpleAttributes() {
		assertEquals(
				bundleContext
						.getProperty(org.osgi.framework.Constants.FRAMEWORK_BOOTDELEGATION),
				frameworkMBean.getBootDelegation());
		assertEquals(
				bundleContext
						.getProperty(org.osgi.framework.Constants.FRAMEWORK_EXECUTIONENVIRONMENT),
				frameworkMBean.getExecutionEnvironment());
		assertEquals(bundleContext
				.getProperty(org.osgi.framework.Constants.FRAMEWORK_LANGUAGE),
				frameworkMBean.getLanguage());
		assertEquals(bundleContext
				.getProperty(org.osgi.framework.Constants.FRAMEWORK_OS_NAME),
				frameworkMBean.getOsName());
		assertEquals(
				bundleContext
						.getProperty(org.osgi.framework.Constants.FRAMEWORK_OS_VERSION),
				frameworkMBean.getOsVersion());
		assertEquals(bundleContext
				.getProperty(org.osgi.framework.Constants.FRAMEWORK_PROCESSOR),
				frameworkMBean.getProcessor());
		assertEquals(
				bundleContext
						.getProperty(org.osgi.framework.Constants.FRAMEWORK_SYSTEMPACKAGES),
				frameworkMBean.getSystemPackages());
		assertEquals(bundleContext
				.getProperty(org.osgi.framework.Constants.FRAMEWORK_VENDOR),
				frameworkMBean.getVendor());
		assertEquals(bundleContext
				.getProperty(org.osgi.framework.Constants.FRAMEWORK_VERSION),
				frameworkMBean.getVersion());
	}

	/**
	 * Tests method <code>ObjectName[] getServices()</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getServices() throws Exception {
		ObjectName[] objectNames = frameworkMBean.getServices();
		final ServiceReference[] serviceReferences = bundleContext
				.getServiceReferences(null, null);
		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				this.bundleContext);
		ObjectName[] serviceReferencesObjectNames = objectNameFactoryHelper
				.getObjectNames(serviceReferences, ServiceReference.class);
		for (int i = 0; i < serviceReferencesObjectNames.length; i++) {
			assertEquals(serviceReferencesObjectNames[i], objectNames[i]);
		}
	}

	/**
	 * Tests method
	 * <code>ObjectName[] getServices(String objectClass, String filter)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getServicesForObjectClassAndFilter() throws Exception {
		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				this.bundleContext);

		ServiceReference[] servicesReferences = bundle.getServicesInUse();

		final ObjectName[] servicesMBean = frameworkMBean.getServices(
				testService.getName(), null);
		ObjectName[] serviceNames = objectNameFactoryHelper.getObjectNames(
				servicesReferences, ServiceReference.class);
		for (int i = 0; i < serviceNames.length; i++) {
			assertEquals(serviceNames[i], servicesMBean[i]);
		}
	}

	/**
	 * Tests method <code>ObjectName installBundle(String location)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_installBundleByLocation() throws Exception {
		ObjectName oldBundleObjectName = getObjectName(bundle, Bundle.class);
		String location = bundle.getLocation();
		Long bundleId = Long.valueOf(bundle.getBundleId());
		// uninstall test bundle
		bundle.uninstall();
		ServiceRegistration notificationListenerServiceRegistration = registerNotificationListener();
		// reinstall test bundle
		ObjectName newBundleObjectName = frameworkMBean.installBundle(location);
		// wait for Switchboard to register new BundleMBean on MBeanServer
		synchronized (this) {
			this.wait(timeout);
		}
		final BundleMBean bundleMBean = (BundleMBean) getMBean(
				newBundleObjectName, BundleMBean.class);
		// assert new bundle has same object name but different id
		assertNotSame(bundleId, bundleMBean.getBundleId());
		assertEquals(oldBundleObjectName, newBundleObjectName);
		notificationListenerServiceRegistration.unregister();
	}

	/**
	 * Tests method
	 * <code>ObjectName installBundle(String location, byte[] input)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_installBundleByLocationAndByteArray() throws Exception {
		ObjectName oldBundleObjectName = getObjectName(bundle, Bundle.class);
		String location = bundle.getLocation();
		Long bundleId = Long.valueOf(bundle.getBundleId());
		// get byte[] from test bundle
		byte[] bundleArray = getByteArrayForBundleLocation(location);
		// uninstall test bundle
		bundle.uninstall();
		ServiceRegistration notificationListenerServiceRegistration = registerNotificationListener();
		// reinstall test bundle
		ObjectName newBundleObjectName = frameworkMBean.installBundle(location,
				bundleArray);
		// wait for Switchboard to register new BundleMBean on MBeanServer
		synchronized (this) {
			this.wait(timeout);
		}
		final BundleMBean bundleMBean = (BundleMBean) getMBean(
				newBundleObjectName, BundleMBean.class);
		// assert new bundle has same object name but different id
		assertNotSame(bundleId, bundleMBean.getBundleId());
		assertEquals(oldBundleObjectName, newBundleObjectName);
		notificationListenerServiceRegistration.unregister();
	}


	/**
	 * Registers notification listener which listens to events fired by
	 * Switchboard.
	 * 
	 * @return the Listener
	 * @throws MalformedObjectNameException
	 *             If NotificationListeners ObjectName is incorrect
	 */
	private ServiceRegistration registerNotificationListener()
			throws MalformedObjectNameException {
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(ObjectName.class.getName(), new ObjectName(
				SWITCHBOARDNOTIFICATIONLISTENER_OBJECTNAME));
		ServiceRegistration notificationListenerServiceRegistration = this.bundleContext
				.registerService(NotificationListener.class.getName(), this,
						properties);
		return notificationListenerServiceRegistration;
	}
}
