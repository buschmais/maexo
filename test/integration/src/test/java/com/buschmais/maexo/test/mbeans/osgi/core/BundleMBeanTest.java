package com.buschmais.maexo.test.mbeans.osgi.core;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.openmbean.TabularData;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceReference;

import com.buschmais.maexo.mbeans.osgi.core.BundleMBean;
import com.buschmais.maexo.test.Constants;
import com.buschmais.maexo.test.common.mbeans.MaexoMBeanTests;

/**
 * This class tests BundleMBean functionality.
 * 
 * @see MaexoTests
 */
public class BundleMBeanTest extends MaexoMBeanTests implements BundleListener {

	/** Set containing all triggered BundleEvents. */
	private BlockingQueue<Integer> bundleEvents;

	/** The TestBundle. */
	private Bundle bundle;

	/** The TestBundleMBean. */
	private BundleMBean bundleMBean;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		bundle = getTestBundle();
		bundleMBean = getTestBundleMBean(bundle);
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
				Constants.ARTIFACT_TESTBUNDLE, Constants.ARTIFACT_EASYMOCK };
	}

	/**
	 * Returns a BundleMBean for the given Bundle.
	 * 
	 * @param bundle
	 *            the Bundle
	 * @return the BundleMBean
	 */
	private BundleMBean getTestBundleMBean(Bundle bundle) {
		// get corresponding BundleMBean
		ObjectName objectName = getObjectName(bundle, Bundle.class);
		ServiceReference serviceReference = super.bundleContext
				.getServiceReference(MBeanServer.class.getName());
		MBeanServerConnection mbeanServer = (MBeanServer) super.bundleContext
				.getService(serviceReference);
		final BundleMBean bundleMBean = (BundleMBean) MBeanServerInvocationHandler
				.newProxyInstance(mbeanServer, objectName, BundleMBean.class,
						false);
		return bundleMBean;
	}

	/**
	 * Tests if all Bundles are registered on MBeanServer.
	 */
	public void test_allBundlesRegisteredAsMBeans() throws IOException {
		Bundle[] bundles = this.bundleContext.getBundles();
		ServiceReference serviceReference = super.bundleContext
				.getServiceReference(MBeanServer.class.getName());
		try {
			MBeanServerConnection mbeanServer = (MBeanServer) super.bundleContext
					.getService(serviceReference);
			for (Bundle bundle : bundles) {
				ObjectName objectName = getObjectName(bundle, Bundle.class);
				assertTrue(String.format("BundleMBean %s is not registered.",
						objectName), mbeanServer.isRegistered(objectName));
			}
		} finally {
			super.bundleContext.ungetService(serviceReference);
		}
	}

	/**
	 * Tests Bundle attributes.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_testBundleAttributes() throws Exception {
		assertEquals(Long.valueOf(bundle.getBundleId()), bundleMBean
				.getBundleId());
		assertEquals(Long.valueOf(bundle.getLastModified()), bundleMBean
				.getLastModified());
		assertEquals(bundle.getLocation(), bundleMBean.getLocation());
	}

	/**
	 * Tests method <code>getServiceinUse()</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getServicesInUse() throws Exception {
		ServiceReference[] bundleServiceReferences = bundle.getServicesInUse();
		ObjectName[] bundleMBeanServicesInUse = bundleMBean.getServicesInUse();
		assertEquals(bundleServiceReferences.length,
				bundleMBeanServicesInUse.length);
		for (int i = 0; i < bundleServiceReferences.length; i++) {
			ServiceReference reference = bundleServiceReferences[i];
			ObjectName bundleServiceObjectName = getObjectName(reference,
					ServiceReference.class);
			assertEquals(bundleServiceObjectName, bundleMBeanServicesInUse[i]);
		}

	}

	/**
	 * Tests method <code>getRegisteredServices()</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_getRegisteredServices() throws Exception {
		final ServiceReference[] registeredBundleServices = bundle
				.getRegisteredServices();
		final ObjectName[] objectNameMBeanServices = bundleMBean
				.getRegisteredServices();
		assertEquals(registeredBundleServices.length,
				objectNameMBeanServices.length);
		for (int i = 0; i < registeredBundleServices.length; i++) {
			ServiceReference registeredBundleService = registeredBundleServices[i];
			final ObjectName objectNameBundleService = getObjectName(
					registeredBundleService, ServiceReference.class);
			assertEquals(objectNameBundleService, objectNameMBeanServices[i]);
		}
	}

	/**
	 * Tests method <code>getHeaders()</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	@SuppressWarnings("unchecked")
	public void test_getHeaders() throws Exception {
		final Enumeration bundleKeys = bundle.getHeaders().keys();
		final Enumeration bundleValues = bundle.getHeaders().elements();
		final TabularData bundleMBeanHeaders = bundleMBean.getHeaders();
		int bundleHeaderCount = 0;
		while (bundleKeys.hasMoreElements()) {
			bundleHeaderCount++;
			String key = (String) bundleKeys.nextElement();
			String value = (String) bundleValues.nextElement();
			assertTrue(bundleMBeanHeaders.get(new String[] { key }).values()
					.contains(value));
		}
		assertEquals(bundleHeaderCount, bundleMBeanHeaders.size());
	}

	/**
	 * Tests methods causing change Events (
	 * <code>start(), stop(), update(), uninstall()</code>.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_changeEvents() throws Exception {

		String url = bundleMBean.getLocation();
		// get byte[] for update(byte[] array)
		String prefix = "file:/";
		// read file
		FileInputStream inStream;
		if (url.startsWith(prefix)) {
			inStream = new FileInputStream(url.substring(prefix.length()));
		} else {
			inStream = new FileInputStream(url);
		}
		// convert into OutputStream
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] tempArray = new byte[4096];
		int readBytes = 0;
		do {
			readBytes = inStream.read(tempArray);
			outStream.write(tempArray, 0, readBytes);
		} while (readBytes == 4096);
		// get byte[]
		byte[] bundleArray = outStream.toByteArray();
		
		bundleContext.addBundleListener(this);
		
		// make sure bundle is started
		if (Bundle.ACTIVE != bundle.getState()) {
			bundleMBean.start();
			synchronized (this) {
				this.wait(5000);
			}
			assertTrue(bundleEvents.contains(Integer
					.valueOf(BundleEvent.STARTED)));
		}
		bundleEvents = new ArrayBlockingQueue<Integer>(20);
		// run methods to test
		bundleMBean.stop();
		bundleMBean.start();
		bundleMBean.update();
		bundleMBean.update(url);
		bundleMBean.update(bundleArray);
		bundleMBean.uninstall();

		// create Queue containing expected BundleEvents
		Queue<Integer> expectedEvents = new LinkedList<Integer>();
		// bundleMBean.stop();
		expectedEvents.offer(Integer.valueOf(BundleEvent.STOPPED));
		// bundleMBean.start();
		expectedEvents.offer(Integer.valueOf(BundleEvent.STARTED));
		// bundleMBean.update();
		expectedEvents.offer(Integer.valueOf(BundleEvent.STOPPED));
		expectedEvents.offer(Integer.valueOf(BundleEvent.UNRESOLVED));
		expectedEvents.offer(Integer.valueOf(BundleEvent.UPDATED));
		expectedEvents.offer(Integer.valueOf(BundleEvent.RESOLVED));
		expectedEvents.offer(Integer.valueOf(BundleEvent.STARTED));
		// bundleMBean.update(url);
		expectedEvents.offer(Integer.valueOf(BundleEvent.STOPPED));
		expectedEvents.offer(Integer.valueOf(BundleEvent.UNRESOLVED));
		expectedEvents.offer(Integer.valueOf(BundleEvent.UPDATED));
		expectedEvents.offer(Integer.valueOf(BundleEvent.RESOLVED));
		expectedEvents.offer(Integer.valueOf(BundleEvent.STARTED));
		// bundleMBean.update(bundleArray);
		expectedEvents.offer(Integer.valueOf(BundleEvent.STOPPED));
		expectedEvents.offer(Integer.valueOf(BundleEvent.UNRESOLVED));
		expectedEvents.offer(Integer.valueOf(BundleEvent.UPDATED));
		expectedEvents.offer(Integer.valueOf(BundleEvent.RESOLVED));
		expectedEvents.offer(Integer.valueOf(BundleEvent.STARTED));
		// bundleMBean.uninstall();
		expectedEvents.offer(Integer.valueOf(BundleEvent.STOPPED));
		expectedEvents.offer(Integer.valueOf(BundleEvent.UNRESOLVED));
		expectedEvents.offer(Integer.valueOf(BundleEvent.UNINSTALLED));
		
		// compare expected BundleEvents with fired BundleEvents
		while (!expectedEvents.isEmpty()) {
			Integer event = this.bundleEvents.poll(5, TimeUnit.SECONDS);
			Integer expectedEvent = expectedEvents.poll();
			assertEquals(expectedEvent, event);
		}

		this.bundleContext.removeBundleListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public void bundleChanged(BundleEvent event) {
		final Integer eventType = Integer.valueOf(event.getType());
		bundleEvents.offer(eventType);
	}

}
