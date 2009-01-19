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
package com.buschmais.maexo.mbeans.osgi.core.impl.lifecyle;

import javax.management.DynamicMBean;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.buschmais.maexo.framework.commons.mbean.lifecycle.DefaultServiceMBeanLifeCycleSupport;
import com.buschmais.maexo.mbeans.osgi.core.impl.StartLevelMBeanImpl;

/**
 * This class implements a service event listener which tracks the life cycle of
 * the start level admin service.
 */
public final class StartLevelMBeanLifeCycle extends
		DefaultServiceMBeanLifeCycleSupport {

	/**
	 * Constructor.
	 * 
	 * @param bundleContext
	 *            the bundle context of the exporting bundle
	 */
	public StartLevelMBeanLifeCycle(BundleContext bundleContext) {
		super(bundleContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object getMBean(ServiceReference serviceReference, Object service) {
		return new StartLevelMBeanImpl(super.getBundleContext(),
				(org.osgi.service.startlevel.StartLevel) service);
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
		return org.osgi.service.startlevel.StartLevel.class;
	}
}
