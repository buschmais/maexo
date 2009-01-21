package com.buschmais.maexo.mbeans.osgi.core.impl.lifecyle;

import javax.management.ObjectName;

import org.osgi.framework.BundleContext;

import com.buschmais.maexo.framework.commons.mbean.lifecycle.MBeanLifecycleSupport;
import com.buschmais.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;
import com.buschmais.maexo.mbeans.osgi.core.FrameworkMBean;
import com.buschmais.maexo.mbeans.osgi.core.impl.FrameworkMBeanImpl;

/**
 * This class manages the life cycle of the BundleContexts MBean.
 */
public final class FrameworkMBeanLifeCycle extends MBeanLifecycleSupport {

	private ObjectName frameworkMBeanObjectName;

	/**
	 * Constructor.
	 * 
	 * @param bundleContext
	 *            the bundle context of the exporting bundle
	 */
	public FrameworkMBeanLifeCycle(BundleContext bundleContext) {
		super(bundleContext);
	}

	/**
	 * Registers BundleContext as MBean.
	 */
	public void start() {
		final BundleContext bundleContext = getBundleContext();
		final FrameworkMBean frameworkMBean = new FrameworkMBeanImpl(
				bundleContext);
		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				bundleContext);
		frameworkMBeanObjectName = objectNameFactoryHelper.getObjectName(
				bundleContext, BundleContext.class);
		registerMBeanService(FrameworkMBean.class, frameworkMBeanObjectName,
				frameworkMBean);
	}

	/**
	 * Unregisters registered BundleContext MBean.
	 */
	public void stop() {
		unregisterMBeanService(frameworkMBeanObjectName);
	}


}
