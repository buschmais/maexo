package com.buschmais.osgi.maexo.test.mbeans.osgi.core;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.Version;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.packageadmin.RequiredBundle;

import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;
import com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminConstants;
import com.buschmais.osgi.maexo.mbeans.osgi.core.PackageAdminMBean;
import com.buschmais.osgi.maexo.test.Constants;
import com.buschmais.osgi.maexo.test.MaexoTests;

public class PackageAdminMBeanTest extends MaexoTests implements
		FrameworkListener, NotificationListener {

	private static final String TESTPACKAGE_NAME = "com.buschmais.osgi.maexo.test.testbundle";

	private Set<Integer> frameworkEvents;
	
	/** The TestBundle. */
	private Bundle bundle;

	/** PackageAdmin. */
	private PackageAdmin packageAdmin;

	/** PackageAdminMBean. */
	private PackageAdminMBean packageAdminMBean;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		frameworkEvents = new HashSet<Integer>();
		bundle = getTestBundle();
		packageAdmin = getPackageAdmin();
		packageAdminMBean = getPackageAdminMBean(packageAdmin);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void frameworkEvent(FrameworkEvent event) {
		frameworkEvents.add(Integer.valueOf(event.getType()));
		synchronized (this) {
			this.notify();
		}
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
	 * Returns a PackageAdmin from OSGI container for testing of general
	 * PackageAdmin functionality.
	 * 
	 * @return the PackageAdmin
	 */
	private PackageAdmin getPackageAdmin() {
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
	 * Compares all attributes of the exportedPackage to all attributes of
	 * exportedPackageCompositeData.
	 * 
	 * @param exportedPackageCompositeData
	 *            CompositeData containing all information about an
	 *            ExportedPackage
	 * @param exportedPackage
	 *            An ExportedPackage
	 */
	@SuppressWarnings("deprecation")
	private void compareExportedPackageAttributes(
			final CompositeData exportedPackageCompositeData,
			final ExportedPackage exportedPackage) {
		// We need to compare all properties.
		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				bundleContext);
		// compare name
		final String exportedPackageName = exportedPackage.getName();
		final String compositeDataName = (String) exportedPackageCompositeData
				.get(PackageAdminConstants.EXPORTED_PACKAGE_PROPERTY_NAME);
		assertEquals(exportedPackageName, compositeDataName);
		// compare exporting bundle by objectName
		final Bundle exportingBundle = exportedPackage.getExportingBundle();
		final ObjectName compositeDataExportingBundle = (ObjectName) exportedPackageCompositeData
				.get(PackageAdminConstants.EXPORTED_PACKAGE_PROPERTY_EXPORTING_BUNDLE);
		assertEquals(objectNameFactoryHelper.getObjectName(exportingBundle,
				Bundle.class), compositeDataExportingBundle);
		// compare importing bundles by objectName
		final Bundle[] importingBundles = exportedPackage.getImportingBundles();
		final ObjectName[] compositeDataImportingBundles = (ObjectName[]) exportedPackageCompositeData
				.get(PackageAdminConstants.EXPORTED_PACKAGE_PROPERTY_IMPORTING_BUNDLE);
		for (int i = 0; i < importingBundles.length; i++) {
			assertEquals(objectNameFactoryHelper.getObjectName(
					importingBundles[i], Bundle.class),
					compositeDataImportingBundles[i]);
		}
		// compare specification version
		final String exportedPackageSpecificationVersion = exportedPackage
				.getSpecificationVersion();
		final String compositeDateSpecificationVersion = (String) exportedPackageCompositeData
				.get(PackageAdminConstants.EXPORTED_PACKAGE_PROPERTY_SPECIFICATION_VERSION);
		assertEquals(exportedPackageSpecificationVersion,
				compositeDateSpecificationVersion);
		// compare version
		final Version exportedPackageVersion = exportedPackage.getVersion();
		final String compositeDataVersion = (String) exportedPackageCompositeData
				.get(PackageAdminConstants.EXPORTED_PACKAGE_PROPERTY_VERSION);
		assertEquals(exportedPackageVersion.toString(), compositeDataVersion);
		// compare isRemovalPending
		final boolean exportedPackageRemovalPending = exportedPackage
				.isRemovalPending();
		final Boolean compositeDateRemovalPending = (Boolean) exportedPackageCompositeData
				.get(PackageAdminConstants.EXPORTED_PACKAGE_PROPERTY_REMOVAL_PENDING);
		assertEquals(compositeDateRemovalPending, Boolean
				.valueOf(exportedPackageRemovalPending));
	}

	/**
	 * Compares all attributes of the requiredBundle to all attributes of
	 * requiredBundleCompositeData.
	 * 
	 * @param requiredBundleCompositeData
	 *            CompositeData containing all information about an
	 *            RequiredBundle
	 * @param requiredBundle
	 *            an RequiredBundle
	 */
	private void compareRequiredBundleAttributes(
			final CompositeData requiredBundleCompositeData,
			final RequiredBundle requiredBundle) {
		// We need to compare all properties.
		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				bundleContext);
		// compare symbolic name
		final String requiredBundleSymbolicName = requiredBundle
				.getSymbolicName();
		final String compositeDataSymbolicName = (String) requiredBundleCompositeData
				.get(PackageAdminConstants.REQUIRED_BUNDLE_PROPERTY_SYMBOLIC_NAME);
		assertEquals(requiredBundleSymbolicName, compositeDataSymbolicName);
		// compare bundle
		final Bundle bundle = requiredBundle.getBundle();
		final ObjectName compositeDataBundleObjectName = (ObjectName) requiredBundleCompositeData
				.get(PackageAdminConstants.REQUIRED_BUNDLE_PROPERTY_BUNDLE);
		ObjectName bundleObjectName = objectNameFactoryHelper.getObjectName(
				bundle, Bundle.class);
		assertEquals(bundleObjectName, compositeDataBundleObjectName);
		// compare version
		final Version bundleVersion = requiredBundle.getVersion();
		final String compositeDataVersion = (String) requiredBundleCompositeData
				.get(PackageAdminConstants.REQUIRED_BUNDLE_PROPERTY_VERSION);
		assertEquals(bundleVersion.toString(), compositeDataVersion);
		// compare isRemovalPending
		final Boolean removalPending = Boolean.valueOf(requiredBundle
				.isRemovalPending());
		final Boolean compositeDataRemovalPending = (Boolean) requiredBundleCompositeData
				.get(PackageAdminConstants.REQUIRED_BUNDLE_PROPERTY_REMOVAL_PENDING);
		assertEquals(removalPending, compositeDataRemovalPending);
		// compare requiring bundles
		final Bundle[] requiringBundles = requiredBundle.getRequiringBundles();
		final ObjectName[] compositeDataRequiringBundlesObjectNames = (ObjectName[]) requiredBundleCompositeData
				.get(PackageAdminConstants.REQUIRED_BUNDLE_PROPERTY_REQUIRING_BUNDLES);
		for (int i = 0; i < requiringBundles.length; i++) {
			ObjectName requiringBundleObjectname = objectNameFactoryHelper
					.getObjectName(requiringBundles[i], Bundle.class);
			assertEquals(requiringBundleObjectname,
					compositeDataRequiringBundlesObjectNames[i]);
		}
	}
	
	/**
	 * Returns the object name for the given bundle.
	 * 
	 * @return the object name
	 */
	private ObjectName getObjectName(Bundle bundle) {
		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				this.bundleContext);
		ObjectName objectName = objectNameFactoryHelper.getObjectName(bundle,
				Bundle.class);
		return objectName;
	}

	/**
	 * Compares all exported packages to information stored in
	 * exportedPackagesMBean.
	 * 
	 * @param exportedPackages
	 *            exported packages to compare
	 * @param exportedPackagesMBean
	 *            MBean holding information about all exported packages
	 */
	private void compareAllExportedPackages(ExportedPackage[] exportedPackages,
			TabularData exportedPackagesMBean) {
		for (int i = 0; i < exportedPackages.length; i++) {
			final String exportedPackageName = exportedPackages[i].getName();
			compareExportedPackageAttributes(exportedPackagesMBean
					.get(new Object[] { exportedPackageName }),
					exportedPackages[i]);
		}
	}

	/**
	 * Tests method <code>getBundleType(Long id)</code>.
	 */
	public void test_getBundleTypeLong() {
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
		ObjectName objectName = getObjectName(bundle);
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
		// positive test
		String symbolicName = bundle.getSymbolicName();
		ObjectName[] packageAdminMBeanObjectNames = packageAdminMBean
				.getBundles(symbolicName, null);
		final Bundle[] packageAdminBundles = packageAdmin.getBundles(
				symbolicName, null);
		for (int i = 0; i < packageAdminBundles.length; i++) {
			ObjectName packageAdminObjectName = getObjectName(packageAdminBundles[i]);
			assertEquals(packageAdminMBeanObjectNames[i],
					packageAdminObjectName);
		}
		// negative test - both methods should return null
		assertNull(packageAdminMBean.getBundles("murks", null));
		assertNull(packageAdmin.getBundles("murks", null));
	}

	/**
	 * Tests method <code>CompositeData getExportedPackage(String name)</code>.
	 */
	public void test_getExportedPackageByString() {
		// positive test
		final CompositeData exportedPackageCompositeData = packageAdminMBean
				.getExportedPackage(TESTPACKAGE_NAME);
		final ExportedPackage exportedPackage = packageAdmin
				.getExportedPackage(TESTPACKAGE_NAME);

		compareExportedPackageAttributes(exportedPackageCompositeData,
				exportedPackage);
		// negative test - both methods should return null
		assertNull(packageAdminMBean.getExportedPackage("murks"));
		assertNull(packageAdmin.getExportedPackage("murks"));
	}

	/**
	 * Tests method
	 * <code>TabularData getExportedPackages(ObjectName objectName)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getExportedPackagesByBundleName() throws Exception {
		ObjectName objectName = getObjectName(bundle);
		ExportedPackage[] exportedPackages = packageAdmin
				.getExportedPackages(bundle);
		TabularData exportedPackagesMBean = packageAdminMBean
				.getExportedPackages(objectName);

		compareAllExportedPackages(exportedPackages, exportedPackagesMBean);
	}


	/**
	 * Tests method <code>TabularData getExportedPackages(Long id)</code>.
	 */
	public void test_getExportedPackagesById() {
		Long id = Long.valueOf(bundle.getBundleId());

		ExportedPackage[] exportedPackages = packageAdmin
				.getExportedPackages(bundle);
		TabularData exportedPackagesMBean = packageAdminMBean
				.getExportedPackages(id);

		compareAllExportedPackages(exportedPackages, exportedPackagesMBean);
	}

	/**
	 * Tests method <code>TabularData getExportedPackages(String name)</code>.
	 */
	public void test_getExportedPackagesByPackageName() {
		// positive test
		final TabularData exportedPackagesMBean = packageAdminMBean
				.getExportedPackages(TESTPACKAGE_NAME);
		final ExportedPackage[] exportedPackages = packageAdmin
				.getExportedPackages(TESTPACKAGE_NAME);
		assertEquals(exportedPackages.length, exportedPackagesMBean.size());

		compareAllExportedPackages(exportedPackages, exportedPackagesMBean);
		// negative test
		assertNull(packageAdminMBean.getExportedPackages("murks"));
		assertNull(packageAdmin.getExportedPackages("murks"));
	}

	/**
	 * Tests method
	 * <code>ObjectName[] getFragments(ObjectName objectName)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getFragmentsByBundleName() throws Exception {
		ObjectName bundleName = getObjectName(bundle);

		final ObjectName[] fragmentObjectNames = packageAdminMBean
				.getFragments(bundleName);
		Bundle[] fragments = packageAdmin.getFragments(bundle);
		if (null != fragmentObjectNames && null != fragments) {
			assertEquals(fragmentObjectNames.length, fragments.length);
			for (int i = 0; i < fragments.length; i++) {
				ObjectName fragmentName = getObjectName(fragments[i]);
				assertEquals(fragmentObjectNames[i], fragmentName);
			}
		}
	}

	/**
	 * Tests method <code>ObjectName[] getFragments(Long id)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getFragmentsByBundleId() throws Exception {
		Long id = Long.valueOf(bundle.getBundleId());
		final ObjectName[] fragmentObjectNames = packageAdminMBean
				.getFragments(id);
		Bundle[] fragments = packageAdmin.getFragments(bundle);

		if (null != fragmentObjectNames && null != fragments) {
			assertEquals(fragmentObjectNames.length, fragments.length);
			for (int i = 0; i < fragments.length; i++) {
				ObjectName fragmentName = getObjectName(fragments[i]);
				assertEquals(fragmentObjectNames[i], fragmentName);
			}
		}
	}

	/**
	 * Tests method <code>ObjectName[] getHosts(ObjectName objectName)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getHostsByBundleName() throws Exception {
		ObjectName bundleName = getObjectName(bundle);

		final ObjectName[] hostObjectNames = packageAdminMBean
				.getHosts(bundleName);
		Bundle[] hosts = packageAdmin.getHosts(bundle);
		if (null != hostObjectNames && null != hosts) {
			assertEquals(hostObjectNames.length, hosts.length);
			for (int i = 0; i < hosts.length; i++) {
				ObjectName hostName = getObjectName(hosts[i]);
				assertEquals(hostObjectNames[i], hostName);
			}
		}
	}

	/**
	 * Tests method <code>ObjectName[] getHosts(Long id)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getHostsByBundleId() throws Exception {
		Long id = Long.valueOf(bundle.getBundleId());
		final ObjectName[] hostObjectNames = packageAdminMBean.getHosts(id);
		Bundle[] hosts = packageAdmin.getHosts(bundle);

		if (null != hostObjectNames && null != hosts) {
			assertEquals(hostObjectNames.length, hosts.length);
			for (int i = 0; i < hosts.length; i++) {
				ObjectName hostName = getObjectName(hosts[i]);
				assertEquals(hostObjectNames[i], hostName);
			}
		}
	}
	
	/**
	 * Tests method
	 * <code>TabularData getRequiredBundles(String symbolicName)</code>.
	 */
	public void test_getRequiredBundles() {
		// positive test for given symbolic name
		String bundleSymbolicName = bundle.getSymbolicName();
		TabularData requiredBundleTabularData = packageAdminMBean
				.getRequiredBundles(bundleSymbolicName);
		RequiredBundle[] requiredBundles = packageAdmin
				.getRequiredBundles(bundleSymbolicName);

		assertEquals(requiredBundles.length, requiredBundleTabularData.size());
		for (int i = 0; i < requiredBundles.length; i++) {
			String requiredBundleName = requiredBundles[i].getSymbolicName();
			compareRequiredBundleAttributes(requiredBundleTabularData
					.get(new Object[] { requiredBundleName }),
					requiredBundles[i]);
		}
		// positive test for null as given symbolic name (no limitation)
		bundleSymbolicName = null;
		requiredBundleTabularData = packageAdminMBean
				.getRequiredBundles(bundleSymbolicName);
		requiredBundles = packageAdmin.getRequiredBundles(bundleSymbolicName);
		
		assertEquals(requiredBundles.length, requiredBundleTabularData.size());
		for (int i = 0; i < requiredBundles.length; i++) {
			String requiredBundleName = requiredBundles[i].getSymbolicName();
			compareRequiredBundleAttributes(requiredBundleTabularData
					.get(new Object[] { requiredBundleName }),
					requiredBundles[i]);
		}
		// negative test - both methods should return null
		bundleSymbolicName = "murks";
		assertNull(packageAdminMBean.getRequiredBundles(bundleSymbolicName));
		assertNull(packageAdmin.getRequiredBundles(bundleSymbolicName));
	}

	/**
	 * Tests method <code>void refreshPackages(Long[] ids)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_refreshPackagesByBundleIds() throws Exception {
		bundleContext.addFrameworkListener(this);
		packageAdminMBean.refreshPackages(new Long[] { Long.valueOf(bundle
				.getBundleId()) });
		synchronized (this) {
			this.wait(5000);
		}
		assertTrue(frameworkEvents.contains(Integer
				.valueOf(FrameworkEvent.PACKAGES_REFRESHED)));
		bundleContext.removeFrameworkListener(this);
	}
	
	/**
	 * Tests method <code>void refreshPackages(ObjectName[] objectNames)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_refreshPackagesByBundleObjectNames() throws Exception {
		ObjectName objectName = getObjectName(bundle);
		this.bundleContext.addFrameworkListener(this);
		packageAdminMBean.refreshPackages(new ObjectName[] { objectName });
		synchronized (this) {
			this.wait(5000);
		}
		assertTrue(frameworkEvents.contains(Integer
				.valueOf(FrameworkEvent.PACKAGES_REFRESHED)));
		this.bundleContext.removeFrameworkListener(this);
	}
	
	/**
	 * Tests method <code>Boolean resolveBundles(Long[] ids)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_resolveBundlesByBundleIds() throws Exception {
		String location = bundle.getLocation();
		bundle.uninstall();
		
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(ObjectName.class.getName(), new ObjectName(
				"JMImplementation:type=MBeanServerDelegate"));
		ServiceRegistration notificationListenerServiceRegistration = this.bundleContext
				.registerService(NotificationListener.class.getName(), this,
						properties);
		Bundle newBundle = bundleContext.installBundle(location);
		synchronized (this) {
			this.wait(5000);
		}
		Long bundleId = Long.valueOf(newBundle.getBundleId());
		assertTrue(packageAdminMBean.resolveBundles(new Long[] { bundleId })
				.booleanValue());
		notificationListenerServiceRegistration.unregister();
	}
	
	/**
	 * Tests method
	 * <code>Boolean resolveBundles(ObjectName[] objectNames)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_resolveBundlesByBundleObjectNames() throws Exception {
		String location = bundle.getLocation();
		bundle.uninstall();

		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(ObjectName.class.getName(), new ObjectName("JMImplementation:type=MBeanServerDelegate"));
		ServiceRegistration notificationListenerServiceRegistration = this.bundleContext
				.registerService(NotificationListener.class.getName(),
						this, properties);

		Bundle newBundle = bundleContext.installBundle(location);
		synchronized (this) {
			this.wait(5000);
		}
		ObjectName objectName = getObjectName(newBundle);
		assertTrue(packageAdminMBean.resolveBundles(
				new ObjectName[] { objectName }).booleanValue());
		notificationListenerServiceRegistration.unregister();
	}
}
