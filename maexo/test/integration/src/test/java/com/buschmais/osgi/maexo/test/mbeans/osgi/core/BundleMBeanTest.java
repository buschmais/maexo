package com.buschmais.osgi.maexo.test.mbeans.osgi.core;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;
import com.buschmais.osgi.maexo.test.Constants;
import com.buschmais.osgi.maexo.test.MaexoTests;

/**
 * @see MaexoTests
 */
public class BundleMBeanTest extends MaexoTests {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String[] getTestBundlesNames() {
		return new String[] { Constants.ARTIFACT_SWITCHBOARD, Constants.ARTIFACT_PLATFORM_MBEAN_SERVER, Constants.ARTIFACT_OSGI_CORE_MBEAN,
				Constants.ARTIFACT_COMMONS_MBEAN };
	}
	
	/**
	 * Tests if all Bundles are registered on MBeanServer.
	 */
	public void test_allBundlesRegisteredAsMBeans() {
		ObjectNameFactoryHelper ObjectNameFactoryHelper = new ObjectNameFactoryHelper(this.bundleContext);
		Bundle[] bundles = this.bundleContext.getBundles();
		ServiceReference serviceReference = super.bundleContext.getServiceReference(MBeanServer.class.getName());
		try {
			MBeanServer mbeanServer = (MBeanServer) super.bundleContext.getService(serviceReference);
			for (Bundle bundle : bundles) {
				ObjectName objectName = ObjectNameFactoryHelper.getObjectName(bundle, Bundle.class);
				assertTrue(String.format("Bundle %s is not registered.", objectName), mbeanServer.isRegistered(objectName));
			}
		} finally {
			super.bundleContext.ungetService(serviceReference);
		}
	}

}
