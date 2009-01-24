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
package com.buschmais.maexo.test.server;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MBeanServerDelegateMBean;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

import org.osgi.framework.ServiceReference;

import com.buschmais.maexo.test.MaexoTests;

/**
 * @see MaexoTests
 */
public class MaexoServerTests extends MaexoTests {

	/**
	 * Returns the agentId for an MBean server.
	 * 
	 * @param mbeanServer
	 *            The MBean server.
	 * @return The agentId.
	 * @throws JMException
	 */
	private String getAgentId(MBeanServer mbeanServer) throws JMException {
		MBeanServerDelegateMBean mbeanServerDelegateMBean = (MBeanServerDelegateMBean) MBeanServerInvocationHandler
				.newProxyInstance(mbeanServer, new ObjectName(
						"JMImplementation:type=MBeanServerDelegate"),
						MBeanServerDelegateMBean.class, false);
		return mbeanServerDelegateMBean.getMBeanServerId();

	}

	/**
	 * Test whether all expected servers are available in the OSGi service
	 * registry
	 * 
	 * @param expectedMBeanServers
	 *            the expected MBean Servers
	 */
	protected void test_ServersAreRegistered(
			Iterable<MBeanServer> expectedMBeanServers) {
		for (MBeanServer expectedMBeanServer : expectedMBeanServers) {
			ServiceReference[] serviceReferences = null;
			try {
				String agentId = this.getAgentId(expectedMBeanServer);
				serviceReferences = super.bundleContext.getServiceReferences(
						MBeanServer.class.getName(), String.format(
								"(agentId=%s)", agentId));
			} catch (Exception e) {
				fail(e.getMessage());
			}
			assertNotNull(serviceReferences);
			assertEquals(1, serviceReferences.length);
			ServiceReference serviceReference = serviceReferences[0];
			try {
				MBeanServer mbeanServer = (MBeanServer) super.bundleContext
						.getService(serviceReference);
				assertNotNull(mbeanServer);
			} finally {
				super.bundleContext.ungetService(serviceReference);
			}
		}
	}
}