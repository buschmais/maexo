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
import javax.management.ObjectName;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import com.buschmais.osgi.maexo.framework.commons.mbean.lifecycle.MBeanLifecycleSupport;
import com.buschmais.osgi.maexo.mbeans.osgi.core.Bundle;
import com.buschmais.osgi.maexo.mbeans.osgi.core.BundleMBean;

/**
 * This class implements a bundle event listener to manage the lifecycle of the
 * associated bundle mbeans.
 */
public final class BundleMBeanLifeCycle extends MBeanLifecycleSupport implements
		BundleListener {

	public BundleMBeanLifeCycle(BundleContext bundleContext) {
		super(bundleContext);
	}

	public void start() {
		// register all existing bundles as mbeans
		for (org.osgi.framework.Bundle bundle : super.getBundleContext()
				.getBundles()) {
			this.bundleChanged(new BundleEvent(BundleEvent.INSTALLED, bundle));
		}
		super.getBundleContext().addBundleListener(this);
	}

	public void stop() {
		// remove bundle listener
		super.getBundleContext().removeBundleListener(this);
		// unregister all registered bundle mbeans
		for (org.osgi.framework.Bundle bundle : super.getBundleContext()
				.getBundles()) {
			this
					.bundleChanged(new BundleEvent(BundleEvent.UNINSTALLED,
							bundle));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleListener#bundleChanged(org.osgi.framework
	 * .BundleEvent)
	 */
	public synchronized void bundleChanged(BundleEvent bundleEvent) {
		org.osgi.framework.Bundle bundle = bundleEvent.getBundle();
		ObjectName objectName = super.getObjectNameHelper().getObjectName(
				bundle, org.osgi.framework.Bundle.class);
		switch (bundleEvent.getType()) {
		case BundleEvent.INSTALLED: {
			BundleMBean bundleMBean = new Bundle(super.getBundleContext(),
					bundle);
			super.registerMBeanService(DynamicMBean.class, objectName,
					bundleMBean);
		}
			break;
		case BundleEvent.UNINSTALLED: {
			super.unregisterMBeanService(objectName);
		}
			break;
		}
	}
}
