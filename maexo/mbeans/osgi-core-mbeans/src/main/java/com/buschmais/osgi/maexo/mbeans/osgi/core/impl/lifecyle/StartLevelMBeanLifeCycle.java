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
package com.buschmais.osgi.maexo.mbeans.osgi.core.impl.lifecyle;

import javax.management.DynamicMBean;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.buschmais.osgi.maexo.mbeans.osgi.core.StartLevel;

/**
 * This class implements a service event listener which tracks the lifecycle of
 * the start level admin service.
 */
public final class StartLevelMBeanLifeCycle extends
		ServiceMBeanLifeCycleSupport {

	public StartLevelMBeanLifeCycle(BundleContext bundleContext) {
		super(bundleContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.impl.lifecyle.
	 * ServiceMBeanLifeCycleSupport
	 * #isManageable(org.osgi.framework.ServiceReference)
	 */
	@Override
	public boolean isManageable(ServiceReference serviceReference) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.impl.lifecyle.
	 * ServiceMBeanLifeCycleSupport#getMBean(java.lang.Object)
	 */
	@Override
	public Object getMBean(Object service) {
		return new StartLevel(super.getBundleContext(),
				(org.osgi.service.startlevel.StartLevel) service);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.impl.lifecyle.
	 * ServiceMBeanLifeCycleSupport#getMBeanInterface()
	 */
	@Override
	public Class<?> getMBeanInterface() {
		return DynamicMBean.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.impl.lifecyle.
	 * ServiceMBeanLifeCycleSupport#getServiceInterface()
	 */
	@Override
	public Class<?> getServiceInterface() {
		return org.osgi.service.startlevel.StartLevel.class;
	}

}