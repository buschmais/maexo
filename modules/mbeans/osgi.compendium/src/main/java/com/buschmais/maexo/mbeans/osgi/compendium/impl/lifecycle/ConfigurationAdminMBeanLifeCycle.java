/*
 * Copyright 2009 buschmais GbR
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
package com.buschmais.maexo.mbeans.osgi.compendium.impl.lifecycle;

import java.util.HashMap;
import java.util.Map;

import javax.management.DynamicMBean;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationAdmin;

import com.buschmais.maexo.framework.commons.mbean.lifecycle.DefaultServiceMBeanLifeCycleSupport;
import com.buschmais.maexo.mbeans.osgi.compendium.impl.ConfigurationAdminMBeanImpl;

/**
 * This class implements a service event listener which tracks the life cycle of
 * the configuration admin service.
 */
public final class ConfigurationAdminMBeanLifeCycle extends
		DefaultServiceMBeanLifeCycleSupport {

	/**
	 * Holds service references of {@link ConfigurationAdmin} services and the
	 * associated instances of {@link ConfigurationMBeanLifeCycle}s.
	 */
	private Map<ServiceReference, ConfigurationMBeanLifeCycle> configurationMBeanLifeCycles = new HashMap<ServiceReference, ConfigurationMBeanLifeCycle>();

	/**
	 * Constructor.
	 *
	 * @param bundleContext
	 *            The bundle context of the exporting bundle.
	 */
	public ConfigurationAdminMBeanLifeCycle(BundleContext bundleContext) {
		super(bundleContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object getMBean(ServiceReference serviceReference, Object service) {
		ConfigurationAdmin configurationAdmin = (ConfigurationAdmin) service;
		ConfigurationMBeanLifeCycle configurationMBeanLifeCycle = new ConfigurationMBeanLifeCycle(
				super.getBundleContext(), serviceReference, configurationAdmin);
		this.configurationMBeanLifeCycles.put(serviceReference,
				configurationMBeanLifeCycle);
		configurationMBeanLifeCycle.start();
		return new ConfigurationAdminMBeanImpl(configurationAdmin,
				configurationMBeanLifeCycle);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void releaseMBean(ServiceReference serviceReference,
			Object service, Object mbean) {
		ConfigurationMBeanLifeCycle configurationMBeanLifeCycle = this.configurationMBeanLifeCycles
				.remove(serviceReference);
		if (configurationMBeanLifeCycle != null) {
			configurationMBeanLifeCycle.stop();
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<?> getMBeanInterface() {
		return DynamicMBean.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<?> getServiceInterface() {
		return ConfigurationAdmin.class;
	}
}
