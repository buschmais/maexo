package com.buschmais.osgi.maexo.framework.mbeanexporter.impl;

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
