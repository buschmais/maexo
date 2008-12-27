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
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.management.DynamicMBean;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.ObjectName;
import javax.management.openmbean.ArrayType;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

import com.buschmais.osgi.maexo.framework.commons.mbean.dynamic.DynamicMBeanSupport;
import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameHelper;

/**
 * Represents a registered service (wrapping a service reference)
 */
public class Service extends DynamicMBeanSupport implements DynamicMBean,
		ServiceMBean {

	/**
	 * the set of constants defined by the framework
	 */
	private static Set<String> FRAMEWORK_PROPERTIES = new HashSet<String>(
			Arrays.asList(new String[] { Constants.SERVICE_DESCRIPTION,
					Constants.SERVICE_ID, Constants.OBJECTCLASS,
					Constants.SERVICE_PID, Constants.SERVICE_RANKING,
					Constants.SERVICE_VENDOR }));

	/**
	 * the service reference to manage
	 */
	private ServiceReference serviceReference;

	/**
	 * the object name helper
	 */
	private ObjectNameHelper objectNameHelper;

	/**
	 * the composite type for the service properties
	 */
	private CompositeType propertiesRowType;

	/**
	 * the tabular type for the service properties
	 */
	private TabularType propertiesType;

	/**
	 * Constructs the service mbean
	 * 
	 * @param bundleContext
	 *            the bundle context
	 * @param serviceReference
	 *            the service reference
	 */
	public Service(BundleContext bundleContext,
			ServiceReference serviceReference) {
		this.serviceReference = serviceReference;
		this.objectNameHelper = new ObjectNameHelper(bundleContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.DynamicMBean#getMBeanInfo()
	 */
	public MBeanInfo getMBeanInfo() {
		try {
			String className = Service.class.getName();
			// attributes
			List<OpenMBeanAttributeInfoSupport> attributeList = new ArrayList<OpenMBeanAttributeInfoSupport>();
			// bundle
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					ServiceConstants.ATTRIBUTE_BUNDLE_NAME,
					ServiceConstants.ATTRIBUTE_BUNDLE_DESCRIPTION,
					SimpleType.OBJECTNAME, true, false, false));
			// description
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					ServiceConstants.ATTRIBUTE_DESCRIPTION_NAME,
					ServiceConstants.ATTRIBUTE_DESCRIPTION_DESCRIPTION,
					SimpleType.STRING, true, false, false));
			// id
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					ServiceConstants.ATTRIBUTE_ID_NAME,
					ServiceConstants.ATTRIBUTE_ID_DESCRIPTION, SimpleType.LONG,
					true, false, false));
			// object class
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					ServiceConstants.ATTRIBUTE_OBJECTCLASS_NAME,
					ServiceConstants.ATTRIBUTE_OBJECTCLASS_DESCRIPTION,
					new ArrayType(1, SimpleType.STRING), true, false, false));
			// pid
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					ServiceConstants.ATTRIBUTE_PID_NAME,
					ServiceConstants.ATTRIBUTE_PID_DESCRIPTION,
					SimpleType.STRING, true, false, false));
			// properties
			this.propertiesRowType = new CompositeType(
					ServiceConstants.COMPOSITETYPE_PROPERTY_ENTRY,
					ServiceConstants.COMPOSITETYPE_PROPERTY_ENTRY_DESCRIPTION,
					new String[] {
							ServiceConstants.COMPOSITETYPE_PROPERTY_NAME,
							ServiceConstants.COMPOSITETYPE_PROPERTY_VALUE },
					new String[] {
							ServiceConstants.COMPOSITETYPE_PROPERTY_NAME,
							ServiceConstants.COMPOSITETYPE_PROPERTY_VALUE },
					new OpenType[] { SimpleType.STRING, SimpleType.STRING });
			this.propertiesType = new TabularType(
					ServiceConstants.TABULARTYPE_PROPERTIES_NAME,
					ServiceConstants.TABULARTYPE_PROPERTIES_DESCRIPTION,
					this.propertiesRowType,
					new String[] { ServiceConstants.COMPOSITETYPE_PROPERTY_NAME });
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					ServiceConstants.ATTRIBUTE_PROPERTIES_NAME,
					ServiceConstants.ATTRIBUTE_PROPERTIES_DESCRIPTION,
					this.propertiesType, true, false, false));
			// ranking
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					ServiceConstants.ATTRIBUTE_RANKING_NAME,
					ServiceConstants.ATTRIBUTE_RANKING_DESCRIPTION,
					SimpleType.INTEGER, true, false, false));
			// using bundles
			attributeList
					.add(new OpenMBeanAttributeInfoSupport(
							ServiceConstants.ATTRIBUTE_USINGBUNDLES_NAME,
							ServiceConstants.ATTRIBUTE_USINGBUNDLES_DESCRIPTION,
							new ArrayType(1, SimpleType.OBJECTNAME), true,
							false, false));
			// vendor
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					ServiceConstants.ATTRIBUTE_VENDOR_NAME,
					ServiceConstants.ATTRIBUTE_VENDOR_DESCRIPTION,
					SimpleType.STRING, true, false, false));

			OpenMBeanAttributeInfoSupport[] mbeanAttributeInfos = attributeList
					.toArray(new OpenMBeanAttributeInfoSupport[0]);
			OpenMBeanOperationInfoSupport[] mbeanOperationInfos = new OpenMBeanOperationInfoSupport[] {};
			OpenMBeanConstructorInfoSupport[] mbeanConstructorInfos = new OpenMBeanConstructorInfoSupport[] {};
			MBeanNotificationInfo[] mbeanNotificationInfos = new MBeanNotificationInfo[] {};
			OpenMBeanInfoSupport mbeanInfo = new OpenMBeanInfoSupport(
					className, ServiceConstants.MBEAN_DESCRIPTION,
					mbeanAttributeInfos, mbeanConstructorInfos,
					mbeanOperationInfos, mbeanNotificationInfos);
			return mbeanInfo;
		} catch (OpenDataException e) {
			throw new RuntimeException("cannot construct mbean info", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Service#getBundle()
	 */
	public ObjectName getBundle() {
		return this.objectNameHelper.getObjectName(this.serviceReference
				.getBundle(), Bundle.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Service#getDescription()
	 */
	public String getDescription() {
		return (String) this.serviceReference
				.getProperty(Constants.SERVICE_DESCRIPTION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Service#getId()
	 */
	public Long getId() {
		return (Long) this.serviceReference.getProperty(Constants.SERVICE_ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Service#getObjectClass()
	 */
	public String[] getObjectClass() {
		return (String[]) this.serviceReference
				.getProperty(Constants.OBJECTCLASS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Service#getPid()
	 */
	public String getPid() {
		return (String) this.serviceReference
				.getProperty(Constants.SERVICE_PID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Service#getProperties()
	 */
	public TabularData getProperties() throws MBeanException {
		TabularDataSupport tabularProperties = new TabularDataSupport(
				this.propertiesType);
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
							this.propertiesRowType,
							new String[] {
									ServiceConstants.COMPOSITETYPE_PROPERTY_NAME,
									ServiceConstants.COMPOSITETYPE_PROPERTY_VALUE },
							new Object[] { key, stringRepresentation });
					tabularProperties.put(row);
				} catch (OpenDataException e) {
					throw new MBeanException(e);
				}
			}
		}
		return tabularProperties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Service#getRanking()
	 */
	public Integer getRanking() {
		return (Integer) this.serviceReference
				.getProperty(Constants.SERVICE_RANKING);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Service#getUsingBundles()
	 */
	public ObjectName[] getUsingBundles() {
		List<ObjectName> objectNames = new LinkedList<ObjectName>();
		Bundle[] usingBundles = this.serviceReference.getUsingBundles();
		if (usingBundles != null) {
			for (Bundle usingBundle : usingBundles) {
				objectNames.add(this.objectNameHelper.getObjectName(
						usingBundle, Bundle.class));
			}
		}
		return objectNames.toArray(new ObjectName[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Service#getVendor(
	 */
	public String getVendor() {
		return (String) this.serviceReference
				.getProperty(Constants.SERVICE_VENDOR);
	}
}
