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
package com.buschmais.maexo.framework.switchboard.impl;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerDelegateMBean;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Represents an mbean server connection which is registered with the
 * switchboard.
 * 
 * @see SwitchBoardImpl
 * @see MBeanServerConnection
 * @see javax.management.MBeanServer
 */
public class MBeanServerConnectionRegistration {

	private MBeanServerConnection mbeanServerConnection;

	private String agentId;

	/**
	 * Constructor.
	 * <p>
	 * The constructor extracts the mbean server connection from the provided
	 * service reference and the bundle context.
	 * <p>
	 * 
	 * @param bundleContext
	 *            the bundle context
	 * @param serviceReference
	 *            the service reference
	 */
	public MBeanServerConnectionRegistration(BundleContext bundleContext,
			ServiceReference serviceReference) {
		this.mbeanServerConnection = (MBeanServerConnection) bundleContext
				.getService(serviceReference);
		this.agentId = (String) serviceReference.getProperty("agentId");
		if (this.agentId == null) {
			MBeanServerDelegateMBean mbeanServerDelegateMBean;
			try {
				mbeanServerDelegateMBean = (MBeanServerDelegateMBean) MBeanServerInvocationHandler
						.newProxyInstance(
								this.mbeanServerConnection,
								new ObjectName(
										"JMImplementation:type=MBeanServerDelegate"),
								MBeanServerDelegateMBean.class, false);
				this.agentId = mbeanServerDelegateMBean.getMBeanServerId();
			} catch (Exception e) {
				throw new IllegalStateException(
						"cannot determine agentId for connection "
								+ this.mbeanServerConnection, e);
			}
		}
	}

	/**
	 * @return the mbeanServerConnection
	 */
	public final MBeanServerConnection getMBeanServerConnection() {
		return mbeanServerConnection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((agentId == null) ? 0 : agentId.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MBeanServerConnectionRegistration other = (MBeanServerConnectionRegistration) obj;
		if (agentId == null) {
			if (other.agentId != null) {
				return false;
			}
		} else if (!agentId.equals(other.agentId)) {
			return false;
		}
		return true;
	}

}
