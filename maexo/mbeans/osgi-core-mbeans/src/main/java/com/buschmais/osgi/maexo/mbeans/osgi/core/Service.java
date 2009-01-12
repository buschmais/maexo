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

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.management.DynamicMBean;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

import com.buschmais.osgi.maexo.framework.commons.mbean.dynamic.DynamicMBeanSupport;
import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;

/**
 * Represents a registered service (wrapping a service reference).
 */
public final class Service extends DynamicMBeanSupport implements DynamicMBean,
		ServiceMBean {

	/**
	 * The set of constants defined by the framework.
	 */
	private static final Set<String> FRAMEWORK_PROPERTIES = new HashSet<String>(
			Arrays.asList(new String[] { Constants.SERVICE_DESCRIPTION,
					Constants.SERVICE_ID, Constants.OBJECTCLASS,
					Constants.SERVICE_PID, Constants.SERVICE_RANKING,
					Constants.SERVICE_VENDOR }));

	/**
	 * The service reference to manage.
	 */
	private final ServiceReference serviceReference;

	/**
	 * The object name helper.
	 */
	private final ObjectNameFactoryHelper objectNameFactoryHelper;

	/**
	 * Constructs the service mbean.
	 * 
	 * @param bundleContext
	 *            the bundle context
	 * @param serviceReference
	 *            the service reference
	 */
	public Service(BundleContext bundleContext,
			ServiceReference serviceReference) {
		this.serviceReference = serviceReference;
		this.objectNameFactoryHelper = new ObjectNameFactoryHelper(bundleContext);
	}

	/**
	 * {@inheritDoc}
	 */
	public MBeanInfo getMBeanInfo() {
			String className = Service.class.getName();
		// attributes
		OpenMBeanAttributeInfoSupport[] mbeanAttributeInfos = new OpenMBeanAttributeInfoSupport[] {
				ServiceConstants.BUNDLE, ServiceConstants.DESCRIPTION,
				ServiceConstants.ID, ServiceConstants.OBJECTCLASS,
				ServiceConstants.PID, ServiceConstants.PROPERTYTYPE,
				ServiceConstants.RANKING, ServiceConstants.USING_BUNDLES,
				ServiceConstants.VENDOR };

		OpenMBeanOperationInfoSupport[] mbeanOperationInfos = new OpenMBeanOperationInfoSupport[] {};
		OpenMBeanConstructorInfoSupport[] mbeanConstructorInfos = new OpenMBeanConstructorInfoSupport[] {};
		MBeanNotificationInfo[] mbeanNotificationInfos = new MBeanNotificationInfo[] {};
		OpenMBeanInfoSupport mbeanInfo = new OpenMBeanInfoSupport(className,
				ServiceConstants.MBEAN_DESCRIPTION, mbeanAttributeInfos,
				mbeanConstructorInfos, mbeanOperationInfos,
				mbeanNotificationInfos);
		return mbeanInfo;
	}


	/**
	 * {@inheritDoc}
	 */
	public ObjectName getBundle() {
		return this.objectNameFactoryHelper.getObjectName(this.serviceReference
				.getBundle(), Bundle.class);
	}


	/**
	 * {@inheritDoc}
	 */
	public String getDescription() {
		return (String) this.serviceReference
				.getProperty(Constants.SERVICE_DESCRIPTION);
	}


	/**
	 * {@inheritDoc}
	 */
	public Long getId() {
		return (Long) this.serviceReference.getProperty(Constants.SERVICE_ID);
	}


	/**
	 * {@inheritDoc}
	 */
	public String[] getObjectClass() {
		return (String[]) this.serviceReference
				.getProperty(Constants.OBJECTCLASS);
	}


	/**
	 * {@inheritDoc}
	 */
	public String getPid() {
		return (String) this.serviceReference
				.getProperty(Constants.SERVICE_PID);
	}


	/**
	 * {@inheritDoc}
	 */
	public TabularData getProperties() {
		TabularDataSupport tabularProperties = new TabularDataSupport(
				ServiceConstants.PROPERTIES_TYPE);
		String[] keys = this.serviceReference.getPropertyKeys();
		for (String key : keys) {
			if (!FRAMEWORK_PROPERTIES.contains(key)) {
				Object value = this.serviceReference.getProperty(key);
				String stringRepresentation = null;
				if (value != null) {
					stringRepresentation = value.toString();
				}
				try {
					CompositeDataSupport row = new CompositeDataSupport(
							ServiceConstants.PROPERTIES_ROW_TYPE,
							ServiceConstants.ITEM_NAMES,
							new Object[] { key, stringRepresentation });
					tabularProperties.put(row);
				} catch (OpenDataException e) {
					throw new IllegalStateException(e);
				}
			}
		}
		return tabularProperties;
	}


	/**
	 * {@inheritDoc}
	 */
	public Integer getRanking() {
		return (Integer) this.serviceReference
				.getProperty(Constants.SERVICE_RANKING);
	}


	/**
	 * {@inheritDoc}
	 */
	public ObjectName[] getUsingBundles() {
		List<ObjectName> objectNames = new LinkedList<ObjectName>();
		Bundle[] usingBundles = this.serviceReference.getUsingBundles();
		if (usingBundles != null) {
			for (Bundle usingBundle : usingBundles) {
				objectNames.add(this.objectNameFactoryHelper.getObjectName(
						usingBundle, Bundle.class));
			}
		}
		return objectNames.toArray(new ObjectName[0]);
	}

 
	/**
	 * {@inheritDoc}
	 */
	public String getVendor() {
		return (String) this.serviceReference
				.getProperty(Constants.SERVICE_VENDOR);
	}
}
