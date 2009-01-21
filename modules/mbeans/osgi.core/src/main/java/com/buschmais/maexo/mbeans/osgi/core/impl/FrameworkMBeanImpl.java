package com.buschmais.maexo.mbeans.osgi.core.impl;

import java.io.ByteArrayInputStream;

import javax.management.DynamicMBean;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.ObjectName;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;

import com.buschmais.maexo.framework.commons.mbean.dynamic.DynamicMBeanSupport;
import com.buschmais.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;
import com.buschmais.maexo.mbeans.osgi.core.FrameworkConstants;
import com.buschmais.maexo.mbeans.osgi.core.FrameworkMBean;
import com.buschmais.maexo.mbeans.osgi.core.PackageAdminConstants;

public final class FrameworkMBeanImpl extends DynamicMBeanSupport implements
		DynamicMBean, FrameworkMBean {

	/**
	 * The bundle context.
	 */
	private final BundleContext bundleContext;

	/**
	 * The object name helper.
	 */
	private final ObjectNameFactoryHelper objectNameFactoryHelper;

	/**
	 * Constructor.
	 * 
	 * @param bundleContext
	 *            The bundle context.
	 */
	public FrameworkMBeanImpl(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		this.objectNameFactoryHelper = new ObjectNameFactoryHelper(
				bundleContext);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getBootDelegation() {
		return bundleContext.getProperty(Constants.FRAMEWORK_BOOTDELEGATION);
	}

	/**
	 * {@inheritDoc}
	 */
	public ObjectName[] getBundles() {
		Bundle[] bundles = bundleContext.getBundles();
		if (bundles == null) {
			return null;
		}
		return this.objectNameFactoryHelper.getObjectNames(bundles,
				Bundle.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getExecutionEnvironment() {
		return this.bundleContext
				.getProperty(Constants.FRAMEWORK_EXECUTIONENVIRONMENT);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLanguage() {
		return this.bundleContext.getProperty(Constants.FRAMEWORK_LANGUAGE);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getOsName() {
		return this.bundleContext.getProperty(Constants.FRAMEWORK_OS_NAME);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getOsVersion() {
		return this.bundleContext.getProperty(Constants.FRAMEWORK_OS_VERSION);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getProcessor() {
		return this.bundleContext.getProperty(Constants.FRAMEWORK_PROCESSOR);
	}

	/**
	 * {@inheritDoc}
	 */
	public ObjectName[] getServices() {
		return this.getServices(null, null);
	}

	/**
	 * {@inheritDoc}
	 */
	public ObjectName[] getServices(String objectClass, String filter) {
		ServiceReference[] serviceReferences;
		try {
			serviceReferences = this.bundleContext.getServiceReferences(
					objectClass, filter);
		} catch (InvalidSyntaxException e) {
			return null;
		}
		return this.objectNameFactoryHelper.getObjectNames(serviceReferences,
				ServiceReference.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getSystemPackages() {
		return this.bundleContext
				.getProperty(Constants.FRAMEWORK_SYSTEMPACKAGES);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getVendor() {
		return this.bundleContext.getProperty(Constants.FRAMEWORK_VENDOR);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getVersion() {
		return this.bundleContext.getProperty(Constants.FRAMEWORK_VERSION);
	}

	/**
	 * {@inheritDoc}
	 */
	public ObjectName installBundle(String location) throws BundleException {
		Bundle bundle = this.bundleContext.installBundle(location);
		return this.objectNameFactoryHelper.getObjectName(bundle, Bundle.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public ObjectName installBundle(String location, byte[] input)
			throws BundleException {
		ByteArrayInputStream inStream = new ByteArrayInputStream(input);
		Bundle bundle = this.bundleContext.installBundle(location, inStream);
		return this.objectNameFactoryHelper.getObjectName(bundle, Bundle.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public MBeanInfo getMBeanInfo() {
		String className = PackageAdmin.class.getName();
		// attributes
		OpenMBeanAttributeInfoSupport[] mbeanAttributeInfos = new OpenMBeanAttributeInfoSupport[] {
				FrameworkConstants.BOOTDELEGATION, FrameworkConstants.BUNDLES,
				FrameworkConstants.EXECUTIONENVIRONMENT,
				FrameworkConstants.LANGUAGE, FrameworkConstants.OSNAME,
				FrameworkConstants.OSVERSION, FrameworkConstants.PROCESSOR,
				FrameworkConstants.SERVICES, FrameworkConstants.SYSTEMPACKAGES,
				FrameworkConstants.VENDOR, FrameworkConstants.VERSION };
		// operations
		OpenMBeanOperationInfoSupport[] mbeanOperationInfos = new OpenMBeanOperationInfoSupport[] {
				FrameworkConstants.GETSERVICES_BY_OBJECTCLASS,
				FrameworkConstants.INSTALLBUNDLE_BY_BYTEARRAY,
				FrameworkConstants.INSTALLBUNDLE_BY_LOCATION };

		// constructors
		OpenMBeanConstructorInfoSupport[] mbeanConstructorInfos = new OpenMBeanConstructorInfoSupport[] {};
		// notifications
		MBeanNotificationInfo[] mbeanNotificationInfos = new MBeanNotificationInfo[] {};
		// mbean info
		OpenMBeanInfoSupport mbeanInfo = new OpenMBeanInfoSupport(className,
				PackageAdminConstants.MBEAN_DESCRIPTION, mbeanAttributeInfos,
				mbeanConstructorInfos, mbeanOperationInfos,
				mbeanNotificationInfos);
		return mbeanInfo;
	}
}
