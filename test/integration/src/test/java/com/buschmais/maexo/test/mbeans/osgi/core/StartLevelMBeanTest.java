package com.buschmais.maexo.test.mbeans.osgi.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.management.ObjectName;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.startlevel.StartLevel;

import com.buschmais.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;
import com.buschmais.maexo.mbeans.osgi.core.StartLevelMBean;
import com.buschmais.maexo.test.Constants;
import com.buschmais.maexo.test.common.mbeans.MaexoMBeanTests;

/**
 * This class tests <code>StartLevelMBean</code> functionality.
 * 
 * @see MaexoTests
 */
public class StartLevelMBeanTest extends MaexoMBeanTests implements
		FrameworkListener {

	/** The test bundle. */
	private Bundle bundle;

	/** The start level. */
	private StartLevel startLevel;

	/** The start level MBean. */
	private StartLevelMBean startLevelMBean;

	/** Queue of events fired by framework. */
	private BlockingQueue<Integer> frameworkEvents;

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
		startLevel = getStartLevel();
		startLevelMBean = getStartLevelMBean(startLevel);
		frameworkEvents = new LinkedBlockingQueue<Integer>();
	}

	/**
	 * Returns a start level from OSGi container for testing of general start
	 * level functionality.
	 * 
	 * @return The start level.
	 */
	private StartLevel getStartLevel() {
		ServiceReference serviceReference = this.bundleContext
				.getServiceReference(StartLevel.class.getName());
		final StartLevel startLevel = (StartLevel) bundleContext
				.getService(serviceReference);
		return startLevel;
	}

	/**
	 * Returns a start level MBean for the given start level.
	 * 
	 * @param startLevel
	 *            The start level.
	 * @return The start level MBean.
	 */
	private StartLevelMBean getStartLevelMBean(StartLevel startLevel) {
		// get property SERVICE_ID which is needed for object name lookup
		ServiceRegistration serviceRegistrationStartLevel = bundleContext
				.registerService(StartLevel.class.getName(), startLevel, null);
		Object serviceId = serviceRegistrationStartLevel.getReference()
				.getProperty(org.osgi.framework.Constants.SERVICE_ID);
		// put SERVICE_ID property into properties-Map
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(org.osgi.framework.Constants.SERVICE_ID, serviceId);
		// get object name for start level
		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				this.bundleContext);
		ObjectName objectName = objectNameFactoryHelper.getObjectName(
				startLevel, StartLevel.class, properties);
		// get start level MBean
		final StartLevelMBean startLevelMBean = (StartLevelMBean) getMBean(
				objectName, StartLevelMBean.class);
		return startLevelMBean;
	}

	/**
	 * {@inheritDoc}
	 */
	public void frameworkEvent(FrameworkEvent event) {
		frameworkEvents.offer(Integer.valueOf(event.getType()));
	}

	/**
	 * Tests method {@link StartLevelMBean#getBundleStartLevel(Long)}.
	 */
	public void test_getBundleStartLevelByBundleId() {
		final Integer bundleStartLevelMBean = startLevelMBean
				.getBundleStartLevel(Long.valueOf(bundle.getBundleId()));
		final int bundleStartLevel = startLevel.getBundleStartLevel(bundle);
		assertEquals(Integer.valueOf(bundleStartLevel), bundleStartLevelMBean);
	}

	/**
	 * Tests method{@link StartLevelMBean#getBundleStartLevel(ObjectName)}.
	 */
	public void test_getBundleStartLevelByBundleObjectName() {
		ObjectName bundleObjectName = getObjectName(bundle, Bundle.class);
		final Integer bundleStartLevelMBean = startLevelMBean
				.getBundleStartLevel(bundleObjectName);
		final int bundleStartLevel = startLevel.getBundleStartLevel(bundle);
		assertEquals(Integer.valueOf(bundleStartLevel), bundleStartLevelMBean);
	}

	/**
	 * Tests method {@link StartLevelMBean#getInitialBundleStartLevel()}.
	 */
	public void test_getInitialBundleStartLevel() {
		final Integer initialBundleStartLevelMBean = startLevelMBean
				.getInitialBundleStartLevel();
		final int initialBundleStartLevel = startLevel
				.getInitialBundleStartLevel();
		assertEquals(Integer.valueOf(initialBundleStartLevel),
				initialBundleStartLevelMBean);
	}

	/**
	 * Tests method {@link StartLevelMBean#getStartLevel()}.
	 */
	public void test_getStartLevel() {
		final Integer startLevelLevelMBean = startLevelMBean.getStartLevel();
		final int startLevelLevel = startLevel.getStartLevel();
		assertEquals(Integer.valueOf(startLevelLevel), startLevelLevelMBean);
	}

	/**
	 * Tests method {@link StartLevelMBean#isBundlePersistentlyStarted(Long)}.
	 */
	public void test_isBundlePersistentlyStartedByBundleId() {
		final Boolean bundlePersistentlyStartedMBean = startLevelMBean
				.isBundlePersistentlyStarted(Long.valueOf(bundle.getBundleId()));
		final boolean bundlePersistentlyStarted = startLevel
				.isBundlePersistentlyStarted(bundle);
		assertEquals(Boolean.valueOf(bundlePersistentlyStarted),
				bundlePersistentlyStartedMBean);
	}

	/**
	 * Tests method
	 * {@link StartLevelMBean#isBundlePersistentlyStarted(ObjectName)}.
	 */
	public void test_isBundlePersistentlyStartedByBundleObjectName() {
		ObjectName bundleObjectName = getObjectName(bundle, Bundle.class);
		final Boolean bundlePersistentlyStartedMBean = startLevelMBean
				.isBundlePersistentlyStarted(bundleObjectName);
		final boolean bundlePersistentlyStarted = startLevel
				.isBundlePersistentlyStarted(bundle);
		assertEquals(Boolean.valueOf(bundlePersistentlyStarted),
				bundlePersistentlyStartedMBean);
	}

	/**
	 * Tests method {@link StartLevelMBean#setBundleStartLevel(Long, Integer)}.
	 */
	public void test_setBundleStartLevelByBundleId() {
		final long bundleId = bundle.getBundleId();
		// get old bundle start level
		int level = startLevelMBean.getBundleStartLevel(Long.valueOf(bundleId))
				.intValue();
		Integer newLevel = Integer.valueOf(level + 1);
		// set new bundle start level
		startLevelMBean.setBundleStartLevel(Long.valueOf(bundleId), newLevel);

		Integer startLevelLevelMBean = startLevelMBean.getBundleStartLevel(Long
				.valueOf(bundleId));
		final int startLevelLevel = startLevel.getBundleStartLevel(bundle);
		// compare new bundle start level
		assertEquals(Integer.valueOf(startLevelLevel), startLevelLevelMBean);
		assertEquals(newLevel, startLevelLevelMBean);
	}

	/**
	 * Tests method
	 * {@link StartLevelMBean#setBundleStartLevel(ObjectName, Integer)} .
	 */
	public void test_setBundleStartLevelByBundleObjectName() {
		ObjectName bundleObjectName = getObjectName(bundle, Bundle.class);
		// get old bundle start level
		int level = startLevelMBean.getBundleStartLevel(bundleObjectName)
				.intValue();
		Integer newLevel = Integer.valueOf(level + 1);
		// set new bundle start level
		startLevelMBean.setBundleStartLevel(bundleObjectName, newLevel);

		Integer startLevelLevelMBean = startLevelMBean
				.getBundleStartLevel(bundleObjectName);
		int startLevelLevel = startLevel.getBundleStartLevel(bundle);
		// compare new bundle start level
		assertEquals(Integer.valueOf(startLevelLevel), startLevelLevelMBean);
		assertEquals(newLevel, startLevelLevelMBean);
	}

	/**
	 * Tests method {@link StartLevelMBean#setStartLevel(Integer)}.
	 * 
	 * @throws Exception
	 *             on error
	 */
	public void test_setStartLevel() throws Exception {
		bundleContext.addFrameworkListener(this);
		// get old start level
		int level = startLevelMBean.getStartLevel().intValue();
		Integer newLevel = Integer.valueOf(level + 1);
		// set new start level
		startLevelMBean.setStartLevel(newLevel);
		assertEquals(Integer.valueOf(FrameworkEvent.STARTLEVEL_CHANGED),
				frameworkEvents.poll(5,
				TimeUnit.SECONDS));
		Integer startLevelLevelMBean = startLevelMBean.getStartLevel();
		int startLevelLevel = startLevel.getStartLevel();
		// compare new start level
		assertEquals(Integer.valueOf(startLevelLevel), startLevelLevelMBean);
		assertEquals(newLevel, startLevelLevelMBean);
		bundleContext.removeFrameworkListener(this);
	}

	/**
	 * Tests method {@link StartLevelMBean#setInitialBundleStartLevel(Integer)}.
	 */
	public void test_setInitialBundleStartLevel() {
		// get old initial bundle start level
		int level = startLevelMBean.getInitialBundleStartLevel().intValue();
		Integer newLevel = Integer.valueOf(level + 1);
		// set new initial bundle start level
		startLevelMBean.setInitialBundleStartLevel(newLevel);
		Integer startLevelLevelMBean = startLevelMBean
				.getInitialBundleStartLevel();
		int startLevelLevel = startLevel.getInitialBundleStartLevel();
		// compare new initial bundle start level
		assertEquals(Integer.valueOf(startLevelLevel), startLevelLevelMBean);
		assertEquals(newLevel, startLevelLevelMBean);
	}
}
