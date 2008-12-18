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
package com.buschmais.osgi.maexo.mbeans.osgi.core.impl.listener;

import javax.management.DynamicMBean;
import javax.management.ObjectName;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import com.buschmais.osgi.maexo.mbeans.osgi.core.BundleMBean;

public class BundleEventListener extends EventListener implements
		BundleListener {

	public BundleEventListener(BundleContext bundleContext) {
		super(bundleContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleListener#bundleChanged(org.osgi.framework
	 * .BundleEvent)
	 */
	public synchronized void bundleChanged(BundleEvent bundleEvent) {
		Bundle bundle = bundleEvent.getBundle();
		Long id = Long.valueOf((bundle.getBundleId()));
		switch (bundleEvent.getType()) {
		case BundleEvent.INSTALLED: {
			BundleMBean bundleMBean = new BundleMBean(bundle, super
					.getObjectNameHelper());
			ObjectName objectName = super.getObjectNameHelper().getObjectName(
					bundle, Bundle.class);
			super.registerMBeanService(DynamicMBean.class, objectName, id,
					bundleMBean);
		}
			break;
		case BundleEvent.UNINSTALLED: {
			super.unregisterMBeanService(id);
		}
			break;
		}
	}
}
