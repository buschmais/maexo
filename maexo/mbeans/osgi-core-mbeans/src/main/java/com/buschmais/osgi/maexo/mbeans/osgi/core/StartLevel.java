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
package com.buschmais.osgi.maexo.mbeans.osgi.core;

import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.buschmais.osgi.maexo.framework.commons.mbean.dynamic.DynamicMBeanSupport;

/**
 * Represents the OSGi start level service.
 */
public final class StartLevel extends DynamicMBeanSupport implements
		StartLevelMBean, DynamicMBean {

	/**
	 * The bundle context.
	 */
	private BundleContext bundleContext;

	/**
	 * The start level service to manage.
	 */
	private org.osgi.service.startlevel.StartLevel startLevel;

	/**
	 * Constructor.
	 * 
	 * @param bundleContext
	 *            the bundle context
	 * @param startLevel
	 *            the start level service
	 */
	public StartLevel(BundleContext bundleContext,
			org.osgi.service.startlevel.StartLevel startLevel) {
		this.bundleContext = bundleContext;
		this.startLevel = startLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.DynamicMBean#getMBeanInfo()
	 */
	public MBeanInfo getMBeanInfo() {
		String className = StartLevel.class.getName();
		// attributes
		OpenMBeanAttributeInfoSupport[] mbeanAttributeInfos = new OpenMBeanAttributeInfoSupport[] {
				StartLevelConstants.STARTLEVEL,
				StartLevelConstants.INITIALBUNDLE_STARTLEVEL };

		// operations
		OpenMBeanOperationInfoSupport[] mbeanOperationInfos = new OpenMBeanOperationInfoSupport[] {
				StartLevelConstants.GETBUNDLESTARTLEVEL_BY_OBJECTNAME,
				StartLevelConstants.SETBUNDLESTARTLEVEL_BY_OBJECTNAME,
				StartLevelConstants.GETBUNDLESTARTLEVEL_BY_ID,
				StartLevelConstants.SETBUNDLESTARTLEVEL_BY_ID,
				StartLevelConstants.ISBUNDLEPERSISTENTLYSTARTED_BY_OBJECTNAME,
				StartLevelConstants.ISBUNDLEPERSISTENTLYSTARTED_BY_ID
		};

		// constructors
		OpenMBeanConstructorInfoSupport[] mbeanConstructorInfos = new OpenMBeanConstructorInfoSupport[] {};
		// notifications
		MBeanNotificationInfo[] mbeanNotificationInfos = new MBeanNotificationInfo[] {};
		// mbean info
		OpenMBeanInfoSupport mbeanInfo = new OpenMBeanInfoSupport(className,
				BundleConstants.MBEAN_DESCRIPTION, mbeanAttributeInfos,
				mbeanConstructorInfos, mbeanOperationInfos,
				mbeanNotificationInfos);
		return mbeanInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#getBundleStartLevel
	 * (javax.management.ObjectName)
	 */
	public Integer getBundleStartLevel(ObjectName objectName)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException {
		Long id = (Long) super.getMbeanServer().getAttribute(objectName,
				BundleConstants.ID.getName());
		return this.getBundleStartLevel(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#getBundleStartLevel
	 * (java.lang.Long)
	 */
	public Integer getBundleStartLevel(Long id) {
		Bundle bundle = this.bundleContext.getBundle(id.longValue());
		return Integer.valueOf(this.startLevel.getBundleStartLevel(bundle));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#
	 * getInitialBundleStartLevel()
	 */
	public Integer getInitialBundleStartLevel() {
		return Integer.valueOf(this.startLevel.getInitialBundleStartLevel());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#getStartLevel()
	 */
	public Integer getStartLevel() {
		return Integer.valueOf(this.startLevel.getStartLevel());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#
	 * isBundlePersistentlyStarted(javax.management.ObjectName)
	 */
	public Boolean isBundlePersistentlyStarted(ObjectName objectName)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException {
		Long id = (Long) super.getMbeanServer().getAttribute(objectName,
				BundleConstants.ID.getName());
		return this.isBundlePersistentlyStarted(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#
	 * isBundlePersistentlyStarted(Long)
	 */
	public Boolean isBundlePersistentlyStarted(Long id) {
		Bundle bundle = this.bundleContext.getBundle(id.longValue());
		return Boolean.valueOf(this.startLevel
				.isBundlePersistentlyStarted(bundle));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#setBundleStartLevel
	 * (javax.management.ObjectName, Integer)
	 */
	public void setBundleStartLevel(ObjectName objectName, Integer startLevel)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException {
		Long id = (Long) super.getMbeanServer().getAttribute(objectName,
				BundleConstants.ID.getName());
		this.setBundleStartLevel(id, startLevel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#setBundleStartLevel
	 * (Long, Integer)
	 */
	public void setBundleStartLevel(Long id, Integer startLevel) {
		Bundle bundle = this.bundleContext.getBundle(id.longValue());
		this.startLevel.setBundleStartLevel(bundle, startLevel.intValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#
	 * setInitialBundleStartLevel(Integer)
	 */
	public void setInitialBundleStartLevel(Integer startLevel) {
		this.startLevel.setInitialBundleStartLevel(startLevel.intValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#setStartLevel
	 * (Integer)
	 */
	public void setStartLevel(Integer startLevel) {
		this.startLevel.setStartLevel(startLevel.intValue());
	}
}
