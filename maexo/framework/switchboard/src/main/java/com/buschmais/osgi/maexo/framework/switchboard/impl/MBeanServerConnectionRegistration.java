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
package com.buschmais.osgi.maexo.framework.switchboard.impl;

import javax.management.MBeanServerConnection;

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
public final class MBeanServerConnectionRegistration {

	private MBeanServerConnection mbeanServerConnection;

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
	}

	/**
	 * @return the mbeanServerConnection
	 */
	public MBeanServerConnection getMbeanServerConnection() {
		return mbeanServerConnection;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((mbeanServerConnection == null) ? 0 : mbeanServerConnection
						.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
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
		if (mbeanServerConnection == null) {
			if (other.mbeanServerConnection != null) {
				return false;
			}
		} else if (!mbeanServerConnection.equals(other.mbeanServerConnection)) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.mbeanServerConnection.toString();
	}

}
