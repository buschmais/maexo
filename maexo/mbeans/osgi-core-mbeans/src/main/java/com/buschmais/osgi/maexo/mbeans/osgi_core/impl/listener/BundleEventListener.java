package com.buschmais.osgi.maexo.mbeans.osgi_core.impl.listener;

import javax.management.DynamicMBean;
import javax.management.ObjectName;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import com.buschmais.osgi.maexo.mbeans.osgi_core.BundleMBean;

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
		Long id = new Long(bundle.getBundleId());
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
