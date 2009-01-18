/*
 * Copyright 2008 buschmais GbR
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package com.buschmais.maexo.test.server.platform;

import javax.management.MBeanServer;

import org.osgi.framework.ServiceReference;

import com.buschmais.maexo.test.Constants;
import com.buschmais.maexo.test.MaexoTests;

/**
 * @see MaexoTests
 */
public class PlatformServerTest extends MaexoTests {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.osgi.test.AbstractDependencyManagerTests#
	 * getTestBundlesNames()
	 */
	@Override
	protected String[] getTestBundlesNames() {
		return new String[] { Constants.ARTIFACT_PLATFORM_MBEAN_SERVER };
	}

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