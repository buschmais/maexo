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
package com.buschmais.maexo.server.factory.impl;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerDelegateMBean;
import javax.management.MBeanServerFactory;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OSGi bundle activator for the MBeans server factory bundle.
 */
public final class Activator implements BundleActivator {

	private static final Logger logger = LoggerFactory
			.getLogger(Activator.class);

	/**
	 * The list of service references holding MBean servers.
	 */
	private Map<String, ServiceRegistration> mbeanServerRegistrations;

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
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void start(BundleContext bundleContext) throws Exception {
		logger.info("Starting maexo MBean Server Factory");
		List<MBeanServer> mbeanServers = MBeanServerFactory
				.findMBeanServer(null);
		this.mbeanServerRegistrations = new HashMap<String, ServiceRegistration>();
		if (mbeanServers != null) {
			for (MBeanServer mbeanServer : mbeanServers) {
				String agentId = this.getAgentId(mbeanServer);
				logger
						.info("registering MBean server with agentId={}",
								agentId);
				Dictionary<String, Object> properties = new Hashtable<String, Object>();
				properties.put("agentId", agentId);
				ServiceRegistration mbeanServerRegistration = bundleContext
						.registerService(new String[] {
								MBeanServer.class.getName(),
								MBeanServerConnection.class.getName() },
								mbeanServer, properties);
				this.mbeanServerRegistrations.put(agentId,
						mbeanServerRegistration);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.mbeanServerRegistrations != null) {
			for (Entry<String, ServiceRegistration> entry : this.mbeanServerRegistrations
					.entrySet()) {
				String agentId = entry.getKey();
				ServiceRegistration mbeanServerRegistration = entry.getValue();
				logger.info("Unregistering MBean server with agentId={}",
						agentId);
				mbeanServerRegistration.unregister();
			}
		}
	}
}
