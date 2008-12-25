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

import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import javax.management.openmbean.OpenMBeanParameterInfo;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;

import com.buschmais.osgi.maexo.framework.commons.mbean.dynamic.DynamicMBeanSupport;
import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameHelper;

/**
 * Represents a bundle
 */
public class Bundle extends DynamicMBeanSupport implements DynamicMBean,
		BundleMBean {

	// translation map for bundle states
	private static Map<Integer, String> bundleStates;

	static {
		bundleStates = new HashMap<Integer, String>();
		bundleStates.put(Integer.valueOf(org.osgi.framework.Bundle.ACTIVE),
				"ACTIVE");
		bundleStates.put(Integer.valueOf(org.osgi.framework.Bundle.INSTALLED),
				"INSTALLED");
		bundleStates.put(Integer.valueOf(org.osgi.framework.Bundle.RESOLVED),
				"RESOLVED");
		bundleStates.put(Integer.valueOf(org.osgi.framework.Bundle.STARTING),
				"STARTING");
		bundleStates.put(Integer.valueOf(org.osgi.framework.Bundle.STOPPING),
				"STOPPING");
		bundleStates.put(
				Integer.valueOf(org.osgi.framework.Bundle.UNINSTALLED),
				"UNINSTALLED");
	}

	/**
	 * the bundle to manage
	 */
	private org.osgi.framework.Bundle bundle;

	private ObjectNameHelper objectNameHelper;

	private CompositeType headersRowType;

	private TabularType headersType;

	/**
	 * Constructs the managed bean
	 * 
	 * @param bundle
	 *            the bundle to manage
	 */
	public Bundle(BundleContext bundleContext, org.osgi.framework.Bundle bundle) {
		this.bundle = bundle;
		this.objectNameHelper = new ObjectNameHelper(bundleContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.DynamicMBean#getMBeanInfo()
	 */
	public MBeanInfo getMBeanInfo() {
		try {
			String className = Bundle.class.getName();
			// attributes
			List<OpenMBeanAttributeInfoSupport> attributeList = new ArrayList<OpenMBeanAttributeInfoSupport>();
			// id
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					BundleConstants.ATTRIBUTE_ID_NAME,
					BundleConstants.ATTRIBUTE_ID_DESCRIPTION,
					SimpleType.INTEGER, true, false, false));
			// state
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					BundleConstants.ATTRIBUTE_STATE_NAME,
					BundleConstants.ATTRIBUTE_STATE_DESCRIPTION,
					SimpleType.INTEGER, true, false, false));
			// state as name
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					BundleConstants.ATTRIBUTE_STATENAME_NAME,
					BundleConstants.ATTRIBUTE_STATENAME_DESCRIPTION,
					SimpleType.STRING, true, false, false));
			// header
			this.headersRowType = new CompositeType(
					BundleConstants.COMPOSITETYPE_HEADERS_ENTRY,
					BundleConstants.COMPOSITETYPE_HEADERS_ENTRY_DESCRIPTION,
					new String[] { BundleConstants.COMPOSITETYPE_HEADERS_NAME,
							BundleConstants.COMPOSITETYPE_HEADERS_VALUE },
					new String[] { BundleConstants.COMPOSITETYPE_HEADERS_NAME,
							BundleConstants.COMPOSITETYPE_HEADERS_VALUE },
					new OpenType[] { SimpleType.STRING, SimpleType.STRING });
			this.headersType = new TabularType(
					BundleConstants.TABULARTYPE_HEADERS_NAME,
					BundleConstants.TABULARTYPE_HEADERS_DESCRIPTION,
					this.headersRowType,
					new String[] { BundleConstants.COMPOSITETYPE_HEADERS_NAME });
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					BundleConstants.ATTRIBUTE_HEADERS_NAME,
					BundleConstants.ATTRIBUTE_HEADERS_DESCRIPTION,
					this.headersType, true, false, false));
			// registered services
			attributeList
					.add(new OpenMBeanAttributeInfoSupport(
							BundleConstants.ATTRIBUTE_REGISTEREDSERVICES_NAME,
							BundleConstants.ATTRIBUTE_REGISTEREDSERVICES_DESCRIPTION,
							new ArrayType(1, SimpleType.OBJECTNAME), true,
							false, false));
			OpenMBeanAttributeInfoSupport[] mbeanAttributeInfos = attributeList
					.toArray(new OpenMBeanAttributeInfoSupport[attributeList
							.size()]);

			// operations
			List<OpenMBeanOperationInfoSupport> operationList = new ArrayList<OpenMBeanOperationInfoSupport>();
			// start
			operationList.add(new OpenMBeanOperationInfoSupport(
					BundleConstants.OPERATION_START_NAME,
					BundleConstants.OPERATION_START_NAME,
					new OpenMBeanParameterInfoSupport[] {}, SimpleType.VOID,
					OpenMBeanOperationInfoSupport.ACTION_INFO));
			// stop
			operationList.add(new OpenMBeanOperationInfoSupport(
					BundleConstants.OPERATION_STOP_NAME,
					BundleConstants.OPERATION_STOP_NAME,
					new OpenMBeanParameterInfoSupport[] {}, SimpleType.VOID,
					OpenMBeanOperationInfoSupport.ACTION_INFO));
			// update
			operationList.add(new OpenMBeanOperationInfoSupport(
					BundleConstants.OPERATION_UPDATE_NAME,
					BundleConstants.OPERATION_UPDATE_NAME,
					new OpenMBeanParameterInfoSupport[] {}, SimpleType.VOID,
					OpenMBeanOperationInfoSupport.ACTION_INFO));
			// update from url
			operationList
					.add(new OpenMBeanOperationInfoSupport(
							BundleConstants.OPERATION_UPDATEFROMURL_NAME,
							BundleConstants.OPERATION_UPDATEFROMURL_NAME,
							new OpenMBeanParameterInfo[] { new OpenMBeanParameterInfoSupport(
									BundleConstants.OPERATION_UPDATEFROMURL_URL_PARAMETER,
									BundleConstants.OPERATION_UPDATEFROMURL_URL_DESCRIPTION,
									SimpleType.STRING) }, SimpleType.VOID,
							OpenMBeanOperationInfoSupport.ACTION_INFO));
			OpenMBeanOperationInfoSupport[] mbeanOperationInfos = operationList
					.toArray(new OpenMBeanOperationInfoSupport[0]);
			// constructors
			OpenMBeanConstructorInfoSupport[] mbeanConstructorInfos = new OpenMBeanConstructorInfoSupport[] {};
			// notifications
			MBeanNotificationInfo[] mbeanNotificationInfos = new MBeanNotificationInfo[] {};
			// mbean info
			OpenMBeanInfoSupport mbeanInfo = new OpenMBeanInfoSupport(
					className, BundleConstants.MBEAN_DESCRIPTION,
					mbeanAttributeInfos, mbeanConstructorInfos,
					mbeanOperationInfos, mbeanNotificationInfos);
			return mbeanInfo;
		} catch (OpenDataException e) {
			throw new RuntimeException("cannot construct mbean info", e);
		}
	}

	public Long getId() {
		return Long.valueOf(this.bundle.getBundleId());
	}

	public Integer getState() {
		return Integer.valueOf(this.bundle.getState());
	}

	public String getStateAsName() {
		return bundleStates.get(Integer.valueOf(this.bundle.getState()));
	}

	/**
	 * Returns an array of object names representing the services which were
	 * registered by this bundle
	 * 
	 * @return the array of object names
	 */
	public ObjectName[] getRegisteredServices() {
		List<ObjectName> registeredServicesList = new LinkedList<ObjectName>();
		ServiceReference[] registeredServices = this.bundle
				.getRegisteredServices();
		if (registeredServices != null) {
			for (ServiceReference registeredService : registeredServices) {
				registeredServicesList.add(this.objectNameHelper.getObjectName(
						registeredService, ServiceReference.class));
			}
		}
		return registeredServicesList.toArray(new ObjectName[0]);
	}

	/**
	 * Returns the bundle headers as tabular data
	 * 
	 * @return the bundle headers
	 * @throws MBeanException
	 * @throws OpenDataException
	 */
	@SuppressWarnings("unchecked")
	public TabularData getHeaders() throws MBeanException {
		TabularDataSupport tabularHeaders = new TabularDataSupport(
				this.headersType);
		Dictionary<String, String> headers = this.bundle.getHeaders();
		Enumeration<String> keys = headers.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = headers.get(key);
			try {
				CompositeDataSupport row = new CompositeDataSupport(
						this.headersRowType, new String[] {
								BundleConstants.COMPOSITETYPE_HEADERS_NAME,
								BundleConstants.COMPOSITETYPE_HEADERS_VALUE },
						new Object[] { key, value });
				tabularHeaders.put(row);
			} catch (Exception e) {
				throw new MBeanException(e);
			}

		}
		return tabularHeaders;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Bundle#start()
	 */
	public void start() throws MBeanException {
		try {
			this.bundle.start();
		} catch (BundleException e) {
			throw new MBeanException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Bundle#stop()
	 */
	public void stop() throws MBeanException {
		try {
			this.bundle.stop();
		} catch (BundleException e) {
			throw new MBeanException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Bundle#uninstall()
	 */
	public void uninstall() throws MBeanException {
		try {
			this.bundle.uninstall();
		} catch (BundleException e) {
			throw new MBeanException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Bundle#update()
	 */
	public void update() throws MBeanException {
		try {
			this.bundle.update();
		} catch (BundleException e) {
			throw new MBeanException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.mbeans.osgi.core.Bundle#update(java.lang.String)
	 */
	public void update(String url) throws MBeanException {
		try {
			URL updateUrl = new URL(url);
			this.bundle.update(updateUrl.openStream());
		} catch (Exception e) {
			throw new MBeanException(e);
		}
	}
}
