package com.buschmais.maexo.test.mbeans.osgi.core;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServerNotification;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.Version;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.packageadmin.RequiredBundle;

import com.buschmais.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;
import com.buschmais.maexo.mbeans.osgi.core.PackageAdminMBeanConstants;
import com.buschmais.maexo.mbeans.osgi.core.PackageAdminMBean;
import com.buschmais.maexo.test.Constants;
import com.buschmais.maexo.test.common.mbeans.MaexoMBeanTests;

public class PackageAdminMBeanTest extends MaexoMBeanTests implements
		FrameworkListener, NotificationListener {

	private static final String TESTPACKAGE_NAME = "com.buschmais.maexo.test.testbundle";

	/** Queue of events fired by framework. */
	private BlockingQueue<Integer> frameworkEvents;

	/** The TestBundle. */
	private Bundle bundle;

	/** PackageAdmin. */
	private PackageAdmin packageAdmin;

	/** PackageAdminMBean. */
	private PackageAdminMBean packageAdminMBean;

	/** Set containing all triggered BundleEvents. */
	private BlockingQueue<Notification> notifications;

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
		// compare name
		final String exportedPackageName = exportedPackage.getName();
		final String compositeDataName = (String) exportedPackageCompositeData
				.get(PackageAdminMBeanConstants.EXPORTEDPACKAGE_ITEM_NAME);
		assertEquals(exportedPackageName, compositeDataName);
		// compare exporting bundle by objectName
		final Bundle exportingBundle = exportedPackage.getExportingBundle();
		final ObjectName compositeDataExportingBundle = (ObjectName) exportedPackageCompositeData
				.get(PackageAdminMBeanConstants.EXPORTEDPACKAGE_ITEM_EXPORTINGBUNDLE);
		assertEquals(getObjectName(exportingBundle, Bundle.class),
				compositeDataExportingBundle);
		// compare importing bundles by objectName
		final Bundle[] importingBundles = exportedPackage.getImportingBundles();
		final ObjectName[] compositeDataImportingBundles = (ObjectName[]) exportedPackageCompositeData
				.get(PackageAdminMBeanConstants.EXPORTEDPACKAGE_ITEM_IMPORTINGBUNDLE);
		for (int i = 0; i < importingBundles.length; i++) {
			assertEquals(getObjectName(importingBundles[i], Bundle.class),
					compositeDataImportingBundles[i]);
		}
		// compare specification version
		final String exportedPackageSpecificationVersion = exportedPackage
				.getSpecificationVersion();
		final String compositeDateSpecificationVersion = (String) exportedPackageCompositeData
				.get(PackageAdminMBeanConstants.EXPORTEDPACKAGE_ITEM_SPECIFICATIONVERSION);
		assertEquals(exportedPackageSpecificationVersion,
				compositeDateSpecificationVersion);
		// compare version
		final Version exportedPackageVersion = exportedPackage.getVersion();
		final String compositeDataVersion = (String) exportedPackageCompositeData
				.get(PackageAdminMBeanConstants.EXPORTEDPACKAGE_ITEM_VERSION);
		assertEquals(exportedPackageVersion.toString(), compositeDataVersion);
		// compare isRemovalPending
		final boolean exportedPackageRemovalPending = exportedPackage
				.isRemovalPending();
		final Boolean compositeDateRemovalPending = (Boolean) exportedPackageCompositeData
				.get(PackageAdminMBeanConstants.EXPORTEDPACKAGE_ITEM_REMOVALPENDING);
		assertEquals(compositeDateRemovalPending, Boolean
				.valueOf(exportedPackageRemovalPending));
	}

	/**
	 * Compares an array of object names to object names of an array of items.
	 * 
	 * @param objectNames
	 *            array of object names
	 * @param items
	 *            array of items
	 * @param clazz
	 *            class type of the given items
	 */
	private void compareItemsObjectNames(final ObjectName[] objectNames,
			Object[] items, Class<?> clazz) {
		if (null != objectNames && null != items) {
			assertEquals(items.length, objectNames.length);
			for (int i = 0; i < items.length; i++) {
				ObjectName fragmentName = getObjectName(items[i], clazz);
				assertEquals(fragmentName, objectNames[i]);
			}
		}
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
		// compare symbolic name
		final String requiredBundleSymbolicName = requiredBundle
				.getSymbolicName();
		final String compositeDataSymbolicName = (String) requiredBundleCompositeData
				.get(PackageAdminMBeanConstants.REQUIREDBUNDLE_ITEM_SYMBOLICNAME);
		assertEquals(requiredBundleSymbolicName, compositeDataSymbolicName);
		// compare bundle
		final Bundle bundle = requiredBundle.getBundle();
		final ObjectName compositeDataBundleObjectName = (ObjectName) requiredBundleCompositeData
				.get(PackageAdminMBeanConstants.REQUIREDBUNDLE_ITEM_BUNDLE);
		ObjectName bundleObjectName = getObjectName(bundle, Bundle.class);
		assertEquals(bundleObjectName, compositeDataBundleObjectName);
		// compare version
		final Version bundleVersion = requiredBundle.getVersion();
		final String compositeDataVersion = (String) requiredBundleCompositeData
				.get(PackageAdminMBeanConstants.REQUIREDBUNDLE_ITEM_VERSION);
		assertEquals(bundleVersion.toString(), compositeDataVersion);
		// compare isRemovalPending
		final Boolean removalPending = Boolean.valueOf(requiredBundle
				.isRemovalPending());
		final Boolean compositeDataRemovalPending = (Boolean) requiredBundleCompositeData
				.get(PackageAdminMBeanConstants.REQUIREDBUNDLE_ITEM_REMOVALPENDING);
		assertEquals(removalPending, compositeDataRemovalPending);
		// compare requiring bundles
		final Bundle[] requiringBundles = requiredBundle.getRequiringBundles();
		final ObjectName[] compositeDataRequiringBundlesObjectNames = (ObjectName[]) requiredBundleCompositeData
				.get(PackageAdminMBeanConstants.REQUIREDBUNDLE_ITEM_REQUIRINGBUNDLES);
		for (int i = 0; i < requiringBundles.length; i++) {
			ObjectName requiringBundleObjectname = getObjectName(
					requiringBundles[i], Bundle.class);
			assertEquals(requiringBundleObjectname,
					compositeDataRequiringBundlesObjectNames[i]);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void frameworkEvent(FrameworkEvent event) {
		frameworkEvents.offer(Integer.valueOf(event.getType()));
	}

	/**
	 * Returns a PackageAdmin from OSGI container for testing of general
	 * PackageAdmin functionality.
	 * 
	 * @return the PackageAdmin
	 */
	private PackageAdmin getPackageAdmin() {
		ServiceReference serviceReference = this.bundleContext
				.getServiceReference(PackageAdmin.class
						.getName());
		final PackageAdmin packageAdmin = (PackageAdmin) this.bundleContext
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
		// get PackageAdminMBean
		final PackageAdminMBean packageAdminMBean = (PackageAdminMBean) getMBean(
				objectName, PackageAdminMBean.class);
		return packageAdminMBean;
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
	 * {@inheritDoc}
	 */
	public void handleNotification(Notification notification, Object handback) {
		notifications.offer(notification);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		frameworkEvents = new LinkedBlockingQueue<Integer>();
		notifications = new LinkedBlockingQueue<Notification>();
		bundle = getTestBundle();
		packageAdmin = getPackageAdmin();
		packageAdminMBean = getPackageAdminMBean(packageAdmin);
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
			ObjectName packageAdminObjectName = getObjectName(
					packageAdminBundles[i], Bundle.class);
			assertEquals(packageAdminObjectName,
					packageAdminMBeanObjectNames[i]);
		}
		// negative test - both methods should return null
		assertNull(packageAdminMBean.getBundles("murks", null));
		assertNull(packageAdmin.getBundles("murks", null));
	}

	/**
	 * Tests method <code>getBundleType(Long id)</code>.
	 */
	public void test_getBundleTypeLong() {
		assertEquals(Integer.valueOf(packageAdmin.getBundleType(bundle)),
				packageAdminMBean.getBundleType(Long.valueOf(bundle
						.getBundleId())));
	}

	/**
	 * Tests method <code>getBundleType(ObjectName objectName)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getBundleTypeObjectName() throws Exception {
		ObjectName objectName = getObjectName(bundle, Bundle.class);
		assertEquals(Integer.valueOf(packageAdmin.getBundleType(bundle)),
				packageAdminMBean.getBundleType(objectName));
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
		ObjectName objectName = getObjectName(bundle, Bundle.class);
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

		compareItemsObjectNames(fragmentObjectNames, fragments, Bundle.class);
	}

	/**
	 * Tests method
	 * <code>ObjectName[] getFragments(ObjectName objectName)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getFragmentsByBundleName() throws Exception {
		ObjectName bundleName = getObjectName(bundle, Bundle.class);

		final ObjectName[] fragmentObjectNames = packageAdminMBean
				.getFragments(bundleName);
		Bundle[] fragments = packageAdmin.getFragments(bundle);
		
		compareItemsObjectNames(fragmentObjectNames, fragments, Bundle.class);
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

		compareItemsObjectNames(hostObjectNames, hosts, Bundle.class);
	}

	/**
	 * Tests method <code>ObjectName[] getHosts(ObjectName objectName)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getHostsByBundleName() throws Exception {
		ObjectName bundleName = getObjectName(bundle, Bundle.class);

		final ObjectName[] hostObjectNames = packageAdminMBean
				.getHosts(bundleName);
		Bundle[] hosts = packageAdmin.getHosts(bundle);
		compareItemsObjectNames(hostObjectNames, hosts, Bundle.class);
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
		assertEquals(Integer.valueOf(FrameworkEvent.PACKAGES_REFRESHED),
				frameworkEvents.poll(5, TimeUnit.SECONDS));
		
		bundleContext.removeFrameworkListener(this);
	}

	/**
	 * Tests method <code>void refreshPackages(ObjectName[] objectNames)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_refreshPackagesByBundleObjectNames() throws Exception {
		ObjectName objectName = getObjectName(bundle, Bundle.class);
		this.bundleContext.addFrameworkListener(this);
		packageAdminMBean.refreshPackages(new ObjectName[] { objectName });
		assertEquals(Integer.valueOf(FrameworkEvent.PACKAGES_REFRESHED),
				frameworkEvents.poll(5, TimeUnit.SECONDS));
		this.bundleContext.removeFrameworkListener(this);
	}

	/**
	 * Tests method <code>Boolean resolveBundles(Long[] ids)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_resolveBundlesByBundleIds() throws Exception {
		ServiceRegistration notificationListenerServiceRegistration = registerNotificationListener();
		Bundle newBundle = reinstallTestBundle();
		
		Long bundleId = Long.valueOf(newBundle.getBundleId());
		assertTrue(packageAdminMBean.resolveBundles(new Long[] { bundleId })
				.booleanValue());
		notificationListenerServiceRegistration.unregister();
	}

	/**
	 * Uninstalls test bundle, waits for unregister notifications of mbeans,
	 * reinstalls bundle, waits for register notifications of mbeans and returns
	 * new bundle.
	 * 
	 * @return the new bundle
	 * @throws BundleException
	 *             if uninstall or install failed
	 * @throws InterruptedException
	 *             if thread was interrupted while waiting for notifications
	 */
	private Bundle reinstallTestBundle() throws BundleException,
			InterruptedException {
		notifications.clear();
		String location = bundle.getLocation();
		final ServiceReference[] servicesInUse = bundle.getServicesInUse();
		int servicesToUnregister = servicesInUse != null ? servicesInUse.length
				: 0;
		bundle.uninstall();
		// catch notifications from unregistering bundle = 1(the bundle itself)
		// + number of services
		for (int i = 0; i <= servicesToUnregister; i++) {
			notifications.poll(5, TimeUnit.SECONDS);
		}
		Bundle newBundle = bundleContext.installBundle(location);
		// wait for notification "bundle registered"
		Notification notification = notifications.poll(5, TimeUnit.SECONDS);
		assertTrue(notification instanceof MBeanServerNotification);
		assertEquals(MBeanServerNotification.REGISTRATION_NOTIFICATION,
				notification.getType());
		return newBundle;
	}

	/**
	 * Tests method
	 * <code>Boolean resolveBundles(ObjectName[] objectNames)</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_resolveBundlesByBundleObjectNames() throws Exception {
		ServiceRegistration notificationListenerServiceRegistration = registerNotificationListener();
		Bundle newBundle = reinstallTestBundle();

		ObjectName objectName = getObjectName(newBundle, Bundle.class);
		assertTrue(packageAdminMBean.resolveBundles(
				new ObjectName[] { objectName }).booleanValue());
		notificationListenerServiceRegistration.unregister();
	}

}
