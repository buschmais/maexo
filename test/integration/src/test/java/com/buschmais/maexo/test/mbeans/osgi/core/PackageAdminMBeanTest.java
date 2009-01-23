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
import com.buschmais.maexo.mbeans.osgi.core.PackageAdminMBean;
import com.buschmais.maexo.mbeans.osgi.core.PackageAdminMBeanConstants;
import com.buschmais.maexo.test.Constants;
import com.buschmais.maexo.test.common.mbeans.MaexoMBeanTests;

/**
 * This class tests <code>PackageAdminMBean</code> functionality.
 * 
 * @see MaexoTests
 */
public class PackageAdminMBeanTest extends MaexoMBeanTests implements
		FrameworkListener, NotificationListener {

	/** Name of the test package. */
	private static final String TESTPACKAGE_NAME = "com.buschmais.maexo.test.testbundle";

	/** Queue of events fired by framework. */
	private BlockingQueue<Integer> frameworkEvents;

	/** The test bundle. */
	private Bundle bundle;

	/** The package admin. */
	private PackageAdmin packageAdmin;

	/** The package admin MBean. */
	private PackageAdminMBean packageAdminMBean;

	/** Set containing all triggered bundle events. */
	private BlockingQueue<Notification> notifications;

	/**
	 * Compares all exported packages to information stored in
	 * <code>exportedPackagesMBean</code>.
	 * 
	 * @param exportedPackages
	 *            Exported packages to compare.
	 * @param exportedPackagesMBean
	 *            MBean holding information about all exported packages.
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
	 * Compares all attributes of the exported package to all attributes of
	 * exported package composite data.
	 * 
	 * @param exportedPackageCompositeData
	 *            Composite data containing all information about an exported
	 *            package.
	 * @param exportedPackage
	 *            An exported package.
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
		// compare exporting bundle by object name
		final Bundle exportingBundle = exportedPackage.getExportingBundle();
		final ObjectName compositeDataExportingBundle = (ObjectName) exportedPackageCompositeData
				.get(PackageAdminMBeanConstants.EXPORTEDPACKAGE_ITEM_EXPORTINGBUNDLE);
		assertEquals(getObjectName(exportingBundle, Bundle.class),
				compositeDataExportingBundle);
		// compare importing bundles by object name
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
	 * Compares an array of object names to object names of an array of
	 * resources.
	 * 
	 * @param objectNames
	 *            Array of object names.
	 * @param resources
	 *            Array of resources.
	 * @param resourceInterface
	 *            Interface of the given resources.
	 */
	private void compareItemsObjectNames(final ObjectName[] objectNames,
			Object[] resources, Class<?> resourceInterface) {
		if (null != objectNames && null != resources) {
			assertEquals(resources.length, objectNames.length);
			for (int i = 0; i < resources.length; i++) {
				ObjectName fragmentName = getObjectName(resources[i],
						resourceInterface);
				assertEquals(fragmentName, objectNames[i]);
			}
		}
	}

	/**
	 * Compares all attributes of the required bundle to all attributes of
	 * required bundle composite data.
	 * 
	 * @param requiredBundleCompositeData
	 *            Composite data containing all information about an required
	 *            bundle.
	 * @param requiredBundle
	 *            An required bundle.
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
	 * Returns a package admin from OSGi container for testing of general
	 * package admin functionality.
	 * 
	 * @return The package admin.
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
	 * Returns a package admin MBean for the given package admin.
	 * 
	 * @param packageAdmin
	 *            The package admin.
	 * @return The package admin MBean.
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
	 * Registers a notification listener which listens to events fired by
	 * switchboard.
	 * 
	 * @return The listener.
	 * @throws MalformedObjectNameException
	 *             If notification listeners object name is incorrect.
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
	 * Tests method {@link PackageAdminMBean#getBundles(String, String)}.
	 */
	public void test_getBundles() {
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
	 * Tests method {@link PackageAdminMBean#getBundleType(Long)}.
	 */
	public void test_getBundleTypeLong() {
		assertEquals(Integer.valueOf(packageAdmin.getBundleType(bundle)),
				packageAdminMBean.getBundleType(Long.valueOf(bundle
						.getBundleId())));
	}

	/**
	 * Tests method {@link PackageAdminMBean#getBundleType(ObjectName)}.
	 */
	public void test_getBundleTypeObjectName() {
		ObjectName objectName = getObjectName(bundle, Bundle.class);
		assertEquals(Integer.valueOf(packageAdmin.getBundleType(bundle)),
				packageAdminMBean.getBundleType(objectName));
	}

	/**
	 * Tests method {@link PackageAdminMBean#getExportedPackage(String)}.
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
	 * Tests method {@link PackageAdminMBean#getExportedPackages(ObjectName)}.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getExportedPackagesByBundlesObjectName() throws Exception {
		ObjectName objectName = getObjectName(bundle, Bundle.class);
		ExportedPackage[] exportedPackages = packageAdmin
				.getExportedPackages(bundle);
		TabularData exportedPackagesMBean = packageAdminMBean
				.getExportedPackages(objectName);

		compareAllExportedPackages(exportedPackages, exportedPackagesMBean);
	}

	/**
	 * Tests method {@link PackageAdminMBean#getExportedPackages(Long)}.
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
	 * Tests method {@link PackageAdminMBean#getExportedPackages(String)}.
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
	 * Tests method {@link PackageAdminMBean#getFragments(Long)}.
	 */
	public void test_getFragmentsByBundleId() {
		Long id = Long.valueOf(bundle.getBundleId());
		final ObjectName[] fragmentObjectNames = packageAdminMBean
				.getFragments(id);
		Bundle[] fragments = packageAdmin.getFragments(bundle);

		compareItemsObjectNames(fragmentObjectNames, fragments, Bundle.class);
	}

	/**
	 * Tests method {@link PackageAdminMBean#getFragments(ObjectName)}.
	 */
	public void test_getFragmentsByBundleName() {
		ObjectName bundleName = getObjectName(bundle, Bundle.class);

		final ObjectName[] fragmentObjectNames = packageAdminMBean
				.getFragments(bundleName);
		Bundle[] fragments = packageAdmin.getFragments(bundle);
		
		compareItemsObjectNames(fragmentObjectNames, fragments, Bundle.class);
	}

	/**
	 * Tests method {@link PackageAdminMBean#getHosts(Long)}.
	 */
	public void test_getHostsByBundleId() {
		Long id = Long.valueOf(bundle.getBundleId());
		final ObjectName[] hostObjectNames = packageAdminMBean.getHosts(id);
		Bundle[] hosts = packageAdmin.getHosts(bundle);

		compareItemsObjectNames(hostObjectNames, hosts, Bundle.class);
	}

	/**
	 * Tests method {@link PackageAdminMBean#getHosts(ObjectName)}.
	 */
	public void test_getHostsByBundleName() {
		ObjectName bundleName = getObjectName(bundle, Bundle.class);

		final ObjectName[] hostObjectNames = packageAdminMBean
				.getHosts(bundleName);
		Bundle[] hosts = packageAdmin.getHosts(bundle);
		compareItemsObjectNames(hostObjectNames, hosts, Bundle.class);
	}

	/**
	 * Tests method {@link PackageAdminMBean#getRequiredBundles(String)}.
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
	 * Tests method {@link PackageAdminMBean#refreshPackages(Long[])}.
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
	 * Tests method {@link PackageAdminMBean#refreshPackages(ObjectName[])}.
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
	 * Tests method {@link PackageAdminMBean#resolveBundles(Long[])}.
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
	 * Uninstalls test bundle, waits for unregister notifications of MBeans,
	 * reinstalls bundle, waits for register notifications of MBeans and returns
	 * new bundle.
	 * 
	 * @return The new bundle.
	 * @throws BundleException
	 *             If uninstall or install failed.
	 * @throws InterruptedException
	 *             If thread was interrupted while waiting for notifications.
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
	 * Tests method {@link PackageAdminMBean#resolveBundles(ObjectName[])}.
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
