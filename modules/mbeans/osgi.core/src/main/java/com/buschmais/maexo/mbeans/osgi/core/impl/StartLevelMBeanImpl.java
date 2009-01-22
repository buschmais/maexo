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
package com.buschmais.maexo.mbeans.osgi.core.impl;

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
import org.osgi.service.startlevel.StartLevel;

import com.buschmais.maexo.framework.commons.mbean.dynamic.DynamicMBeanSupport;
import com.buschmais.maexo.mbeans.osgi.core.BundleMBeanConstants;
import com.buschmais.maexo.mbeans.osgi.core.StartLevelMBeanConstants;
import com.buschmais.maexo.mbeans.osgi.core.StartLevelMBean;

/**
 * Represents the OSGi start level service.
 */
public final class StartLevelMBeanImpl extends DynamicMBeanSupport implements
		StartLevelMBean, DynamicMBean {

	/** The bundle context. */
	private final BundleContext bundleContext;

	/** The start level service to manage. */
	private final StartLevel startLevel;

	/**
	 * Constructor.
	 * 
	 * @param bundleContext
	 *            the bundle context
	 * @param startLevel
	 *            the start level service
	 */
	public StartLevelMBeanImpl(BundleContext bundleContext,
			StartLevel startLevel) {
		this.bundleContext = bundleContext;
		this.startLevel = startLevel;
	}

	/**
	 * {@inheritDoc}
	 */
	public MBeanInfo getMBeanInfo() {
		String className = StartLevelMBeanImpl.class.getName();
		// attributes
		OpenMBeanAttributeInfoSupport[] mbeanAttributeInfos = new OpenMBeanAttributeInfoSupport[] {
				StartLevelMBeanConstants.STARTLEVEL,
				StartLevelMBeanConstants.INITIALBUNDLE_STARTLEVEL };

		// operations
		OpenMBeanOperationInfoSupport[] mbeanOperationInfos = new OpenMBeanOperationInfoSupport[] {
				StartLevelMBeanConstants.GETBUNDLESTARTLEVEL_BY_OBJECTNAME,
				StartLevelMBeanConstants.SETBUNDLESTARTLEVEL_BY_OBJECTNAME,
				StartLevelMBeanConstants.GETBUNDLESTARTLEVEL_BY_ID,
				StartLevelMBeanConstants.SETBUNDLESTARTLEVEL_BY_ID,
				StartLevelMBeanConstants.ISBUNDLEPERSISTENTLYSTARTED_BY_OBJECTNAME,
				StartLevelMBeanConstants.ISBUNDLEPERSISTENTLYSTARTED_BY_ID };

		// constructors
		OpenMBeanConstructorInfoSupport[] mbeanConstructorInfos = new OpenMBeanConstructorInfoSupport[] {};
		// notifications
		MBeanNotificationInfo[] mbeanNotificationInfos = new MBeanNotificationInfo[] {};
		// mbean info
		OpenMBeanInfoSupport mbeanInfo = new OpenMBeanInfoSupport(className,
				BundleMBeanConstants.MBEAN_DESCRIPTION, mbeanAttributeInfos,
				mbeanConstructorInfos, mbeanOperationInfos,
				mbeanNotificationInfos);
		return mbeanInfo;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getBundleStartLevel(ObjectName objectName) {
		Long id = (Long) getAttribute(objectName, BundleMBeanConstants.ID.getName());
		return this.getBundleStartLevel(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getBundleStartLevel(Long id) {
		Bundle bundle = this.bundleContext.getBundle(id.longValue());
		if (null == bundle) {
			throw new IllegalArgumentException(String.format(
					"cannot get bundle for id %s", id));
		}
		return Integer.valueOf(this.startLevel.getBundleStartLevel(bundle));
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getInitialBundleStartLevel() {
		return Integer.valueOf(this.startLevel.getInitialBundleStartLevel());
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getStartLevel() {
		return Integer.valueOf(this.startLevel.getStartLevel());
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean isBundlePersistentlyStarted(ObjectName objectName) {
		Long id = (Long) getAttribute(objectName, BundleMBeanConstants.ID.getName());
		return this.isBundlePersistentlyStarted(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean isBundlePersistentlyStarted(Long id) {
		Bundle bundle = this.bundleContext.getBundle(id.longValue());
		if (null == bundle) {
			throw new IllegalArgumentException(String.format(
					"cannot get bundle for id %s", id));
		}
		return Boolean.valueOf(this.startLevel
				.isBundlePersistentlyStarted(bundle));
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBundleStartLevel(ObjectName objectName, Integer startLevel) {
		Long id = (Long) getAttribute(objectName, BundleMBeanConstants.ID.getName());
		this.setBundleStartLevel(id, startLevel);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBundleStartLevel(Long id, Integer startLevel) {
		Bundle bundle = this.bundleContext.getBundle(id.longValue());
		if (null == bundle) {
			throw new IllegalArgumentException(String.format(
					"cannot get bundle for id %s", id));
		}
		this.startLevel.setBundleStartLevel(bundle, startLevel.intValue());
	}

	/**
	 * {@inheritDoc}
	 */
	public void setInitialBundleStartLevel(Integer startLevel) {
		this.startLevel.setInitialBundleStartLevel(startLevel.intValue());
	}

	/**
	 * {@inheritDoc}
	 */
	public void setStartLevel(Integer startLevel) {
		this.startLevel.setStartLevel(startLevel.intValue());
	}
}
