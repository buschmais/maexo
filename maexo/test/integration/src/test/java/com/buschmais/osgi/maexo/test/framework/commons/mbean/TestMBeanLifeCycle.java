/**
 * 
 */
package com.buschmais.osgi.maexo.test.framework.commons.mbean;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.osgi.framework.BundleContext;

import com.buschmais.osgi.maexo.framework.commons.mbean.lifecycle.MBeanLifecycleSupport;
import com.buschmais.osgi.maexo.test.common.mbeans.Standard;
import com.buschmais.osgi.maexo.test.common.mbeans.StandardMBean;

/**
 * This class derives from {@link MBeanLifecycleSupport} for test purposes.
 */
public class TestMBeanLifeCycle extends MBeanLifecycleSupport {

	private ObjectName objectName;

	/**
	 * Constructor.
	 * 
	 * @param bundleContext
	 *            The bundle context.
	 * @throws NullPointerException
	 * @throws MalformedObjectNameException
	 */
	public TestMBeanLifeCycle(BundleContext bundleContext, String objectName)
			throws MalformedObjectNameException, NullPointerException {
		super(bundleContext);
		this.objectName = new ObjectName(objectName);
	}

	/**
	 * Creates an MBean instance and uses the super class to publish the MBean
	 * to the OSGi service registry.
	 */
	public void registeredResource() {
		StandardMBean mbean = new Standard();
		super.registerMBeanService(StandardMBean.class, this.objectName, mbean);
	}

	/**
	 * Removes an MBean instance from the OSGi service registry using the
	 * appropriate super class method.
	 */
	public void unregisteredResource() {
		super.unregisterMBeanService(objectName);
	}

}
