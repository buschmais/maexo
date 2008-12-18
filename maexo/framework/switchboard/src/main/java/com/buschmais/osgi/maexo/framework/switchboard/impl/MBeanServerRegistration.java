/**
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

import javax.management.MBeanServer;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class MBeanServerRegistration {

	private MBeanServer mbeanServer;

	public MBeanServerRegistration(BundleContext bundleContext,
			ServiceReference serviceReference) {
		this.mbeanServer = (MBeanServer) bundleContext
				.getService(serviceReference);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mbeanServer == null) ? 0 : mbeanServer.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MBeanServerRegistration other = (MBeanServerRegistration) obj;
		if (mbeanServer == null) {
			if (other.mbeanServer != null)
				return false;
		} else if (!mbeanServer.equals(other.mbeanServer))
			return false;
		return true;
	}

	/**
	 * @return the mbeanServer
	 */
	public MBeanServer getMbeanServer() {
		return mbeanServer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.mbeanServer.toString();
	}

}
