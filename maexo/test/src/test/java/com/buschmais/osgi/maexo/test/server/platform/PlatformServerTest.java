package com.buschmais.osgi.maexo.test.server.platform;

import javax.management.MBeanServer;

import org.osgi.framework.ServiceReference;

import com.buschmais.osgi.maexo.test.Constants;
import com.buschmais.osgi.maexo.test.MaexoTests;

/**
 * @see MaexoTests
 */
public class PlatformServerTest extends MaexoTests {

	protected String[] getTestBundlesNames() {
		return new String[] { Constants.ARTIFACT_PLATFORM_MBEAN_SERVER };
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