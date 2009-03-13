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

/**
 * Represents an MBean server connection which is registered with the
 * switchboard.
 *
 * @see SwitchBoardImpl
 * @see MBeanServerConnection
 * @see javax.management.MBeanServer
 */
public class MBeanServerConnectionRegistration {

	private final MBeanServerConnection mbeanServerConnection;

	private String agentId;

	/**
	 * Constructor.
	 * <p>
	 *
	 * @param agentId
	 *            The AgentId of the connection.
	 * @param mbeanServerConnection
	 *            The instance of the {@link MBeanServerConnection}.
	 */
	public MBeanServerConnectionRegistration(String agentId,
			MBeanServerConnection mbeanServerConnection) {
		this.agentId = agentId;
		this.mbeanServerConnection = mbeanServerConnection;
	}

	/**
	 * Returns the MBean server connection.
	 *
	 * @return The MBean server connection.
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
