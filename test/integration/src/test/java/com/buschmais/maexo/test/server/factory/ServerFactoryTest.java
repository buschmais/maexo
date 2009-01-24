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
package com.buschmais.maexo.test.server.factory;

import java.lang.management.ManagementFactory;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;

import com.buschmais.maexo.test.Constants;
import com.buschmais.maexo.test.MaexoTests;
import com.buschmais.maexo.test.server.MaexoServerTests;

/**
 * @see MaexoTests
 */
public class ServerFactoryTest extends MaexoServerTests {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.osgi.test.AbstractDependencyManagerTests#
	 * getTestBundlesNames()
	 */
	@Override
	protected String[] getTestBundlesNames() {
		return new String[] { Constants.ARTIFACT_MBEAN_SERVER_FACTORY };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.springframework.osgi.test.AbstractDependencyManagerTests#
	 * preProcessBundleContext(org.osgi.framework.BundleContext)
	 */
	@Override
	protected void preProcessBundleContext(BundleContext platformBundleContext)
			throws Exception {
		// getting the platform MBean server registers an instance in the MBean server factory 
		ManagementFactory.getPlatformMBeanServer();
		super.preProcessBundleContext(platformBundleContext);
	}

	/**
	 * Checks if we can find the same MBeanServer instances in the OSGi service
	 * registry as delivered by
	 * {@link MBeanServerFactory#findMBeanServer(String)}
	 * 
	 * @throws InvalidSyntaxException
	 */
	@SuppressWarnings("unchecked")
	public void test_getServersFromFactoy() {
		List<MBeanServer> mbeanServers = MBeanServerFactory
				.findMBeanServer(null);
		assertFalse(mbeanServers.isEmpty());
		super.test_ServersAreRegistered(mbeanServers);
	}
}