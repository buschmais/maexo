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
package com.buschmais.maexo.test.spring;

import java.util.jar.Manifest;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.ObjectName;

import com.buschmais.maexo.test.Constants;
import com.buschmais.maexo.test.MaexoTests;
import com.buschmais.maexo.test.common.mbeans.Standard;
import com.buschmais.maexo.test.common.mbeans.StandardMBeanNotificationListener;

/**
 * @see MaexoTests
 */
public class SpringTest extends MaexoTests {

	private static final String[] SPRING_CONFIG_LOCATIONS = new String[] {
			"/spring/mbeanserver-osgi.xml", "/spring/mbeans-osgi.xml" };

	private static final String IMPORT_PACKAGES = "org.springframework.jmx.support";

	private static final String BEAN_MBEANSERVER = "mbeanServer";
	private static final String BEAN_CLASSICMBEAN = "classicMBean";
	private static final String BEAN_NOTIFICATIONLISTENER = "classicMBeanNotificationListener";

	private static final String OBJECTNAME = "com.buschmais.osgi.maexo:type=classicMBean";

	private static final String NOTIFICATION_HANDBACK = "handbackObject";

	private MBeanServer mbeanServer;

	private Standard classicMBean;

	private StandardMBeanNotificationListener notificationListener;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.osgi.test.AbstractDependencyManagerTests#
	 * getTestBundlesNames()
	 */
	@Override
	protected String[] getTestBundlesNames() {
		return new String[] { Constants.ARTIFACT_SWITCHBOARD };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.test.AbstractSingleSpringContextTests#getConfigLocations
	 * ()
	 */
	@Override
	protected String[] getConfigLocations() {
		return SPRING_CONFIG_LOCATIONS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.osgi.test.AbstractOnTheFlyBundleCreatorTests#getManifest
	 * ()
	 */
	@Override
	protected Manifest getManifest() {
		// let the testing framework create/load the manifest
		Manifest mf = super.getManifest();
		StringBuffer imports = new StringBuffer(IMPORT_PACKAGES);
		String importPackages = mf.getMainAttributes().getValue(
				org.osgi.framework.Constants.IMPORT_PACKAGE);
		if (importPackages != null) {
			imports.append(',');
			imports.append(importPackages);
		}
		mf.getMainAttributes()
				.putValue(org.osgi.framework.Constants.IMPORT_PACKAGE,
						imports.toString());
		return mf;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.test.AbstractSingleSpringContextTests#onSetUp()
	 */
	@Override
	protected void onSetUp() {
		this.mbeanServer = (MBeanServer) super.getApplicationContext().getBean(
				BEAN_MBEANSERVER);
		assertNotNull(this.mbeanServer);
		this.classicMBean = (Standard) super.getApplicationContext().getBean(
				BEAN_CLASSICMBEAN);
		assertNotNull(this.classicMBean);
		this.notificationListener = (StandardMBeanNotificationListener) super
				.getApplicationContext().getBean(BEAN_NOTIFICATIONLISTENER);
	}

	public void test_mbeanRegistrationDone()
			throws MalformedObjectNameException, NullPointerException {
		assertNotNull(this.classicMBean.isRegistrationDone());
		assertTrue(this.classicMBean.isRegistrationDone().booleanValue());
		assertEquals(new ObjectName(OBJECTNAME), this.classicMBean
				.getObjectName());
	}

	public void test_notificationListener() throws InterruptedException {
		String newValue = "a new value";
		this.classicMBean.setAttribute(newValue);
		// notifications may be asynchronous, wait some time
		Thread.sleep(1000);
		Notification notification = this.notificationListener
				.getLastNotification();
		assertNotNull(notification);
		assertEquals(NOTIFICATION_HANDBACK, this.notificationListener
				.getLastHandback());
		if (notification instanceof AttributeChangeNotification) {
			AttributeChangeNotification attributeChangeNotification = (AttributeChangeNotification) notification;
			assertEquals(newValue, attributeChangeNotification.getNewValue());
		} else {
			super.fail("expected instance of "
					+ AttributeChangeNotification.class.getName()
					+ ", received " + notification.getClass().getName());
		}
	}
}