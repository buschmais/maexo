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

import javax.management.MBeanServer;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Represents an mbean server which is registered with the switchboard.
 * 
 * @see SwitchBoardImpl
 * @see MBeanServer
 */
public final class MBeanServerRegistration extends
		MBeanServerConnectionRegistration {

	/**
	 * Constructor.
	 * <p>
	 * The constructor extracts the mbean server from the provided service
	 * reference and the bundle context.
	 * <p>
	 * 
	 * @param bundleContext
	 *            the bundle context
	 * @param serviceReference
	 *            the service reference
	 */
	public MBeanServerRegistration(BundleContext bundleContext,
			ServiceReference serviceReference) {
		super(bundleContext, serviceReference);
	}

	/**
	 * @return the mbeanServer
	 */
	public MBeanServer getMBeanServer() {
		return (MBeanServer) super.getMBeanServerConnection();
	}
}
