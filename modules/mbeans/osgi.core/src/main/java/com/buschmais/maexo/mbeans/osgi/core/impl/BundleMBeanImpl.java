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

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.management.DynamicMBean;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;

import com.buschmais.maexo.framework.commons.mbean.dynamic.DynamicMBeanSupport;
import com.buschmais.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;
import com.buschmais.maexo.mbeans.osgi.core.BundleConstants;
import com.buschmais.maexo.mbeans.osgi.core.BundleMBean;

/**
 * Represents an OSGi bundle.
 */
public final class BundleMBeanImpl extends DynamicMBeanSupport implements DynamicMBean,
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
	 * The bundle to manage.
	 */
	private final org.osgi.framework.Bundle bundle;

	private final ObjectNameFactoryHelper objectNameFactoryHelper;

	/**
	 * Constructs the managed bean.
	 * 
	 * @param bundleContext
	 *            the bundleContext
	 * @param bundle
	 *            the bundle to manage
	 */
	public BundleMBeanImpl(BundleContext bundleContext, org.osgi.framework.Bundle bundle) {
		this.bundle = bundle;
		this.objectNameFactoryHelper = new ObjectNameFactoryHelper(
				bundleContext);
	}

	/**
	 * {@inheritDoc}
	 */
	public MBeanInfo getMBeanInfo() {
		String className = BundleMBeanImpl.class.getName();
		// attributes
		OpenMBeanAttributeInfoSupport[] mbeanAttributeInfos = new OpenMBeanAttributeInfoSupport[] {
				BundleConstants.ID, BundleConstants.STATE,
				BundleConstants.STATENAME, BundleConstants.HEADER,
				BundleConstants.LASTMODIFIED,
				BundleConstants.LASTMODIFIEDASDATE, BundleConstants.LOCATION,
				BundleConstants.REGISTEREDSERVICES,
				BundleConstants.SERVICESINUSE };

		// operations
		OpenMBeanOperationInfoSupport[] mbeanOperationInfos = new OpenMBeanOperationInfoSupport[] {
				BundleConstants.START, BundleConstants.STOP,
				BundleConstants.UPDATE, BundleConstants.UPDATEFROMURL,
				BundleConstants.UPDATEFROMBYTEARRAY,
				BundleConstants.UNINSTALL };
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

	/**
	 * {@inheritDoc}
	 */
	public Long getBundleId() {
		return Long.valueOf(this.bundle.getBundleId());
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getState() {
		return Integer.valueOf(this.bundle.getState());
	}

	/**
	 * {@inheritDoc}
	 */
	public String getStateAsName() {
		return bundleStates.get(Integer.valueOf(this.bundle.getState()));
	}

	/**
	 * Returns an array of object names representing the services which were
	 * registered by this bundle.
	 * 
	 * @return the array of object names
	 */
	public ObjectName[] getRegisteredServices() {
		List<ObjectName> registeredServicesList = new LinkedList<ObjectName>();
		ServiceReference[] registeredServices = this.bundle
				.getRegisteredServices();
		if (registeredServices != null) {
			for (ServiceReference registeredService : registeredServices) {
				registeredServicesList.add(this.objectNameFactoryHelper
						.getObjectName(registeredService,
								ServiceReference.class));
			}
		}
		return registeredServicesList.toArray(new ObjectName[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public TabularData getHeaders() {
		TabularDataSupport tabularHeaders = new TabularDataSupport(
				BundleConstants.HEADERS_TYPE);
		Dictionary<String, String> headers = this.bundle.getHeaders();
		Enumeration<String> keys = headers.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = headers.get(key);
			try {
				CompositeDataSupport header = new CompositeDataSupport(
						BundleConstants.HEADER_TYPE, BundleConstants.HEADER_ITEMS
								.toArray(new String[0]), new Object[] { key,
								value });
				tabularHeaders.put(header);
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}

		}
		return tabularHeaders;
	}

	/**
	 * {@inheritDoc}
	 */
	public void start() {
		try {
			this.bundle.start();
		} catch (BundleException e) {
			throw new RuntimeException(e.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void stop() {
		try {
			this.bundle.stop();
		} catch (BundleException e) {
			throw new RuntimeException(e.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void uninstall() {
		try {
			this.bundle.uninstall();
		} catch (BundleException e) {
			throw new RuntimeException(e.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void update() {
		try {
			this.bundle.update();
		} catch (BundleException e) {
			throw new RuntimeException(e.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void update(String url) {
		try {
			URL updateUrl = new URL(url);
			this.bundle.update(updateUrl.openStream());
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void update(byte[] in) {
		ByteArrayInputStream stream = new ByteArrayInputStream(in);
		try {
			this.bundle.update(stream);
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Long getLastModified() {
		return Long.valueOf(this.bundle.getLastModified());
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getLastModifiedAsDate() {
		return new Date(this.bundle.getLastModified());
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLocation() {
		return this.bundle.getLocation();
	}

	/**
	 * {@inheritDoc}
	 */
	public ObjectName[] getServicesInUse() {
		ServiceReference[] servicesInUse = this.bundle.getServicesInUse();
		if (servicesInUse == null) {
			return null;
		}
		ObjectName[] objectNames = new ObjectName[servicesInUse.length];
		for (int i = 0; i < servicesInUse.length; i++) {
			objectNames[i] = this.objectNameFactoryHelper.getObjectName(
					servicesInUse[i], ServiceReference.class);
		}
		return objectNames;
	}
}
