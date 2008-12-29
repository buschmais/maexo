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

import java.util.ArrayList;
import java.util.List;

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
import javax.management.openmbean.OpenMBeanParameterInfoSupport;
import javax.management.openmbean.SimpleType;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.buschmais.osgi.maexo.framework.commons.mbean.dynamic.DynamicMBeanSupport;

/**
 * MBean implementation which represents the OSGi start level service
 */
public final class StartLevel extends DynamicMBeanSupport implements
		StartLevelMBean, DynamicMBean {

	/**
	 * The bundle context
	 */
	private BundleContext bundleContext;

	/**
	 * The start level service to manage
	 */
	private org.osgi.service.startlevel.StartLevel startLevel;

	/**
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
		List<OpenMBeanAttributeInfoSupport> attributeList = new ArrayList<OpenMBeanAttributeInfoSupport>();
		// startLevel
		attributeList.add(new OpenMBeanAttributeInfoSupport(
				StartLevelConstants.ATTRIBUTE_STARTLEVEL_NAME,
				StartLevelConstants.ATTRIBUTE_STARTLEVEL_DESCRIPTION,
				SimpleType.INTEGER, true, true, false));
		// initialBundleStartLevel
		attributeList
				.add(new OpenMBeanAttributeInfoSupport(
						StartLevelConstants.ATTRIBUTE_INITIALBUNDLESTARTLEVEL_NAME,
						StartLevelConstants.ATTRIBUTE_INITIALBUNDLESTARTLEVEL_DESCRIPTION,
						SimpleType.INTEGER, true, true, false));
		OpenMBeanAttributeInfoSupport[] mbeanAttributeInfos = attributeList
				.toArray(new OpenMBeanAttributeInfoSupport[attributeList.size()]);

		// operations
		List<OpenMBeanOperationInfoSupport> operationList = new ArrayList<OpenMBeanOperationInfoSupport>();
		// int getBundleStartLevel(ObjectName)
		operationList
				.add(new OpenMBeanOperationInfoSupport(
						StartLevelConstants.OPERATION_GETBUNDLESTARTLEVEL_NAME,
						StartLevelConstants.OPERATION_GETBUNDLESTARTLEVEL_DESCRIPTION,
						new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
								StartLevelConstants.OPERATION_GETBUNDLESTARTLEVEL_OBJECTNAME_PARAMETER,
								StartLevelConstants.OPERATION_GETBUNDLESTARTLEVEL_OBJECTNAME_DESCRIPTION,
								SimpleType.OBJECTNAME) }, SimpleType.INTEGER,
						OpenMBeanOperationInfoSupport.INFO));
		// void setBundleStartLevel(ObjectName, int)
		operationList
				.add(new OpenMBeanOperationInfoSupport(
						StartLevelConstants.OPERATION_SETBUNDLESTARTLEVEL_NAME,
						StartLevelConstants.OPERATION_SETBUNDLESTARTLEVEL_DESCRIPTION,
						new OpenMBeanParameterInfoSupport[] {
								new OpenMBeanParameterInfoSupport(
										StartLevelConstants.OPERATION_SETBUNDLESTARTLEVEL_OBJECTNAME_PARAMETER,
										StartLevelConstants.OPERATION_SETBUNDLESTARTLEVEL_OBJECTNAME_DESCRIPTION,
										SimpleType.OBJECTNAME),
								new OpenMBeanParameterInfoSupport(
										StartLevelConstants.OPERATION_SETBUNDLESTARTLEVEL_STARTLEVEL_PARAMETER,
										StartLevelConstants.OPERATION_SETBUNDLESTARTLEVEL_STARTLEVEL_DESCRIPTION,
										SimpleType.INTEGER) }, SimpleType.VOID,
						OpenMBeanOperationInfoSupport.ACTION));
		// int getBundleStartLevel(long)
		operationList
				.add(new OpenMBeanOperationInfoSupport(
						StartLevelConstants.OPERATION_GETBUNDLESTARTLEVEL_NAME,
						StartLevelConstants.OPERATION_GETBUNDLESTARTLEVEL_DESCRIPTION,
						new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
								StartLevelConstants.OPERATION_GETBUNDLESTARTLEVEL_ID_PARAMETER,
								StartLevelConstants.OPERATION_GETBUNDLESTARTLEVEL_ID_DESCRIPTION,
								SimpleType.LONG) }, SimpleType.INTEGER,
						OpenMBeanOperationInfoSupport.INFO));
		// void setBundleStartLevel(long, int)
		operationList
				.add(new OpenMBeanOperationInfoSupport(
						StartLevelConstants.OPERATION_SETBUNDLESTARTLEVEL_NAME,
						StartLevelConstants.OPERATION_SETBUNDLESTARTLEVEL_DESCRIPTION,
						new OpenMBeanParameterInfoSupport[] {
								new OpenMBeanParameterInfoSupport(
										StartLevelConstants.OPERATION_SETBUNDLESTARTLEVEL_ID_PARAMETER,
										StartLevelConstants.OPERATION_SETBUNDLESTARTLEVEL_ID_DESCRIPTION,
										SimpleType.LONG),
								new OpenMBeanParameterInfoSupport(
										StartLevelConstants.OPERATION_SETBUNDLESTARTLEVEL_STARTLEVEL_PARAMETER,
										StartLevelConstants.OPERATION_SETBUNDLESTARTLEVEL_STARTLEVEL_DESCRIPTION,
										SimpleType.INTEGER) }, SimpleType.VOID,
						OpenMBeanOperationInfoSupport.ACTION));
		// boolean isBundlePersistentlyStarted(ObjectName)
		operationList
				.add(new OpenMBeanOperationInfoSupport(
						StartLevelConstants.OPERATION_ISBUNDLEPERSISTENTLYSTARTED_NAME,
						StartLevelConstants.OPERATION_ISBUNDLEPERSISTENTLYSTARTED_DESCRIPTION,
						new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
								StartLevelConstants.OPERATION_ISBUNDLEPERSISTENTLYSTARTED_OBJECTNAME_PARAMETER,
								StartLevelConstants.OPERATION_ISBUNDLEPERSISTENTLYSTARTED_OBJECTNAME_DESCRIPTION,
								SimpleType.OBJECTNAME) }, SimpleType.BOOLEAN,
						OpenMBeanOperationInfoSupport.INFO));
		// boolean isBundlePersistentlyStarted(long)
		operationList
				.add(new OpenMBeanOperationInfoSupport(
						StartLevelConstants.OPERATION_ISBUNDLEPERSISTENTLYSTARTED_NAME,
						StartLevelConstants.OPERATION_ISBUNDLEPERSISTENTLYSTARTED_DESCRIPTION,
						new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
								StartLevelConstants.OPERATION_ISBUNDLEPERSISTENTLYSTARTED_ID_PARAMETER,
								StartLevelConstants.OPERATION_ISBUNDLEPERSISTENTLYSTARTED_ID_DESCRIPTION,
								SimpleType.LONG) }, SimpleType.BOOLEAN,
						OpenMBeanOperationInfoSupport.INFO));
		OpenMBeanOperationInfoSupport[] mbeanOperationInfos = operationList
				.toArray(new OpenMBeanOperationInfoSupport[0]);
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
				BundleConstants.ATTRIBUTE_ID_NAME);
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
				BundleConstants.ATTRIBUTE_ID_NAME);
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
				BundleConstants.ATTRIBUTE_ID_NAME);
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
