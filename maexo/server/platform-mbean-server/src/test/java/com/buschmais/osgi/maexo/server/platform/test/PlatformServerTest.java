package com.buschmais.osgi.maexo.server.platform.test;

import javax.management.MBeanServer;

import org.osgi.framework.ServiceReference;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;

/**
 * @see AbstractConfigurableBundleCreatorTests
 */
public class PlatformServerTest extends AbstractConfigurableBundleCreatorTests {

	protected String[] getTestBundlesNames() {
		return new String[] { "com.buschmais.osgi.maexo.server, platform-mbean-server, 1.0.0-SNAPSHOT" };
	};

	/**
	 * just see if we can find a MBeanServer instance in the OSGi service
	 * registry
	 */
	public void test_getPlatformMBeanServer() {
		ServiceReference serviceReference = super.bundleContext
				.getServiceReference(MBeanServer.class.getName());
		assertNotNull(serviceReference);
		try {
			MBeanServer mbeanServer = (MBeanServer) super.bundleContext
					.getService(serviceReference);
			assertNotNull(mbeanServer);
		} finally {
			super.bundleContext.ungetService(serviceReference);
		}
	}

}