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
package com.buschmais.osgi.maexo.mbeans.osgi.core;

import java.util.ArrayList;
import java.util.List;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
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

/**
 * MBean implementation which represents the OSGi start level service
 */
public class StartLevel extends DynamicMBeanSupport implements
		MBeanRegistration, StartLevelMBean, DynamicMBean {

	private BundleContext bundleContext;

	/**
	 * the mbean server where this mbean is registered
	 */
	private MBeanServer mbeanServer;

	/**
	 * the start level service to manage
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
	 * @see javax.management.DynamicMBean#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String attribute)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException {
		if (StartLevelConstants.ATTRIBUTE_STARTLEVEL_NAME.equals(attribute)) {
			return Integer.valueOf(this.getStartLevel());
		} else if (StartLevelConstants.ATTRIBUTE_INITIALBUNDLESTARTLEVEL_NAME
				.equals(attribute)) {
			return Integer.valueOf(this.getInitialBundleStartLevel());
		}
		throw new AttributeNotFoundException("unknown attribute " + attribute);
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
	 * @see javax.management.DynamicMBean#invoke(java.lang.String,
	 * java.lang.Object[], java.lang.String[])
	 */
	public Object invoke(String actionName, Object[] params, String[] signature)
			throws MBeanException, ReflectionException {
		if (StartLevelConstants.OPERATION_GETBUNDLESTARTLEVEL_NAME
				.equals(actionName)
				&& signature.length == 1) {
			// getBundleStartLevel
			if (ObjectName.class.getName().equals(signature[0])) {
				ObjectName objectName = (ObjectName) params[0];
				try {
					return Integer
							.valueOf(this.getBundleStartLevel(objectName));
				} catch (Exception e) {
					throw new MBeanException(e);
				}
			} else if (Long.class.getName().equals(signature[0])) {
				long id = ((Long) params[0]).longValue();
				return Integer.valueOf(this.getBundleStartLevel(id));
			}
		} else if (StartLevelConstants.OPERATION_SETBUNDLESTARTLEVEL_NAME
				.equals(actionName)
				&& signature.length == 2) {
			// setBundleStartLevel
			if (Integer.class.getName().equals(signature[1])) {
				int startLevel = ((Integer) params[1]).intValue();
				if (ObjectName.class.getName().equals(signature[0])) {
					ObjectName objectName = (ObjectName) params[0];
					try {
						this.setBundleStartLevel(objectName, startLevel);
						return null;
					} catch (Exception e) {
						throw new MBeanException(e);
					}
				} else if (Integer.class.getName().equals(signature[0])) {
					long id = ((Long) params[0]).longValue();
					this.setBundleStartLevel(id, startLevel);
					return null;
				}
			}
		}
		if (StartLevelConstants.OPERATION_ISBUNDLEPERSISTENTLYSTARTED_NAME
				.equals(actionName)
				&& signature.length == 1) {
			// isBundlePersistentlyStarted
			if (ObjectName.class.getName().equals(signature[0])) {
				ObjectName objectName = (ObjectName) params[0];
				try {
					return Boolean.valueOf(this
							.isBundlePersistentlyStarted(objectName));
				} catch (Exception e) {
					throw new MBeanException(e);
				}
			} else if (Long.class.getName().equals(signature[0])) {
				long id = ((Long) params[0]).longValue();
				return Boolean.valueOf(this.isBundlePersistentlyStarted(id));
			}
		}
		throw new ReflectionException(new NoSuchMethodException(actionName
				+ "/" + signature));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.management.DynamicMBean#setAttribute(javax.management.Attribute)
	 */
	public void setAttribute(Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException {
		String name = attribute.getName();
		Object value = attribute.getValue();
		if (value != null && value instanceof Integer) {
			int startLevel = ((Integer) value).intValue();
			if (StartLevelConstants.ATTRIBUTE_STARTLEVEL_NAME.equals(name)) {
				this.setStartLevel(startLevel);
			} else if (StartLevelConstants.ATTRIBUTE_INITIALBUNDLESTARTLEVEL_NAME
					.equals(name)) {
				this.setInitialBundleStartLevel(startLevel);
			} else {
				throw new AttributeNotFoundException("unknown attribute "
						+ attribute);
			}
		} else {
			throw new InvalidAttributeValueException(
					"expected a positive integer value for attribute " + name);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#getBundleStartLevel
	 * (javax.management.ObjectName)
	 */
	public int getBundleStartLevel(ObjectName objectName)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException {
		Long id = (Long) this.mbeanServer.getAttribute(objectName,
				BundleConstants.ATTRIBUTE_ID_NAME);
		return this.getBundleStartLevel(id.longValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#getBundleStartLevel
	 * (long)
	 */
	public int getBundleStartLevel(long id) {
		Bundle bundle = this.bundleContext.getBundle(id);
		return this.startLevel.getBundleStartLevel(bundle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#
	 * getInitialBundleStartLevel()
	 */
	public int getInitialBundleStartLevel() {
		return this.startLevel.getInitialBundleStartLevel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#getStartLevel()
	 */
	public int getStartLevel() {
		return this.startLevel.getStartLevel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#
	 * isBundlePersistentlyStarted(javax.management.ObjectName)
	 */
	public boolean isBundlePersistentlyStarted(ObjectName objectName)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException {
		Long id = (Long) this.mbeanServer.getAttribute(objectName,
				BundleConstants.ATTRIBUTE_ID_NAME);
		return this.isBundlePersistentlyStarted(id.longValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#
	 * isBundlePersistentlyStarted(long)
	 */
	public boolean isBundlePersistentlyStarted(long id) {
		Bundle bundle = this.bundleContext.getBundle(id);
		return this.startLevel.isBundlePersistentlyStarted(bundle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#setBundleStartLevel
	 * (javax.management.ObjectName, int)
	 */
	public void setBundleStartLevel(ObjectName objectName, int startLevel)
			throws AttributeNotFoundException, InstanceNotFoundException,
			MBeanException, ReflectionException {
		Long id = (Long) this.mbeanServer.getAttribute(objectName,
				BundleConstants.ATTRIBUTE_ID_NAME);
		this.setBundleStartLevel(id.longValue(), startLevel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#setBundleStartLevel
	 * (long, int)
	 */
	public void setBundleStartLevel(long id, int startLevel) {
		Bundle bundle = this.bundleContext.getBundle(id);
		this.startLevel.setBundleStartLevel(bundle, startLevel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#
	 * setInitialBundleStartLevel(int)
	 */
	public void setInitialBundleStartLevel(int startLevel) {
		this.startLevel.setInitialBundleStartLevel(startLevel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.StartLevelMBean#setStartLevel
	 * (int)
	 */
	public void setStartLevel(int startLevel) {
		this.startLevel.setStartLevel(startLevel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.MBeanRegistration#postDeregister()
	 */
	public void postDeregister() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.MBeanRegistration#postRegister(java.lang.Boolean)
	 */
	public void postRegister(Boolean registrationDone) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.MBeanRegistration#preDeregister()
	 */
	public void preDeregister() throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.management.MBeanRegistration#preRegister(javax.management.MBeanServer
	 * , javax.management.ObjectName)
	 */
	public ObjectName preRegister(MBeanServer server, ObjectName name)
			throws Exception {
		this.mbeanServer = server;
		return name;
	}

}
