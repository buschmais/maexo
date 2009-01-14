package com.buschmais.osgi.maexo.test.mbeans.osgi.core;

import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.packageadmin.PackageAdmin;

import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;
import com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean;
import com.buschmais.osgi.maexo.test.Constants;
import com.buschmais.osgi.maexo.test.MaexoTests;

public class PackageAdminMBeanTest extends MaexoTests {

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

	private PackageAdmin getPackageAdmin() throws Exception {
		ServiceReference serviceReference = this.bundleContext
				.getServiceReference(org.osgi.service.packageadmin.PackageAdmin.class
						.getName());
		final org.osgi.service.packageadmin.PackageAdmin packageAdmin = (org.osgi.service.packageadmin.PackageAdmin) bundleContext
				.getService(serviceReference);
		return packageAdmin;
	}

	/**
	 * Returns a PackageAdminMBean for the given PackageAdmin.
	 * 
	 * @param packageAdmin
	 *            the PackageAdmin
	 * @return the PackageAdminMBean
	 */
	private PackageAdminMBean getPackageAdminMBean(PackageAdmin packageAdmin) {
		// get property SERVICE_ID which is needed for ObjectName lookup
		ServiceRegistration serviceRegistrationPackageAdmin = bundleContext
				.registerService(PackageAdmin.class.getName(), packageAdmin,
						null);
		Object serviceId = serviceRegistrationPackageAdmin.getReference()
				.getProperty(org.osgi.framework.Constants.SERVICE_ID);
		// put SERVICE_ID property into properties-Map
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(org.osgi.framework.Constants.SERVICE_ID, serviceId);
		// get ObjectName for PackageAdmin
		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				this.bundleContext);
		ObjectName objectName = objectNameFactoryHelper.getObjectName(
				packageAdmin, PackageAdmin.class, properties);
		// get MBeanServer
		ServiceReference serviceReference = super.bundleContext
				.getServiceReference(MBeanServer.class.getName());
		MBeanServerConnection mbeanServer = (MBeanServer) super.bundleContext
				.getService(serviceReference);
		// get PackageAdminBMean
		final PackageAdminMBean packageAdminMBean = (PackageAdminMBean) MBeanServerInvocationHandler
				.newProxyInstance(mbeanServer, objectName,
						PackageAdminMBean.class, false);
		return packageAdminMBean;
	}

	/**
	 * Tests method <code>getBundleType(Long id)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getBundleTypeLong() throws Exception {
		Bundle bundle = getTestBundle();
		PackageAdmin packageAdmin = getPackageAdmin();
		PackageAdminMBean packageAdminMBean = getPackageAdminMBean(packageAdmin);

		assertEquals(packageAdminMBean.getBundleType(Long.valueOf(bundle
				.getBundleId())), Integer.valueOf(packageAdmin
				.getBundleType(bundle)));
		
	}

	/**
	 * Tests method <code>getBundleType(ObjectName objectName)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getBundleTypeObjectName() throws Exception {
		Bundle bundle = getTestBundle();
		PackageAdmin packageAdmin = getPackageAdmin();
		PackageAdminMBean packageAdminMBean = getPackageAdminMBean(packageAdmin);

		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				this.bundleContext);
		ObjectName objectName = objectNameFactoryHelper.getObjectName(
				bundle,
				Bundle.class);

		assertEquals(packageAdminMBean.getBundleType(objectName), Integer
				.valueOf(packageAdmin.getBundleType(bundle)));
	}
	
	/**
	 * Tests method
	 * <code>getBundles(String symbolicName, String versionRange)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getBundles() throws Exception {
		Bundle bundle = getTestBundle();
		PackageAdmin packageAdmin = getPackageAdmin();
		PackageAdminMBean packageAdminMBean = getPackageAdminMBean(packageAdmin);

		String symbolicName = bundle.getSymbolicName();
		ObjectName[] packageAdminMBeanObjectNames = packageAdminMBean
				.getBundles(symbolicName, null);
		final Bundle[] packageAdminBundles = packageAdmin.getBundles(
				symbolicName, null);
		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				this.bundleContext);
		for (int i = 0; i < packageAdminBundles.length; i++) {
			ObjectName packageAdminObjectName = objectNameFactoryHelper
					.getObjectName(packageAdminBundles[i], Bundle.class);
			assertEquals(packageAdminMBeanObjectNames[i],
					packageAdminObjectName);
		}
	}
	
}
