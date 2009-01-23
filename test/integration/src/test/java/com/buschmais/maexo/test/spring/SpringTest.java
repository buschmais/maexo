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
	private static final String BEAN_STANDARDMBEAN = "standardMBean";
	private static final String BEAN_NOTIFICATIONLISTENER = "standardMBeanNotificationListener";

	private static final String OBJECTNAME = "com.buschmais.maexo:type=standardMBean";

	private static final String NOTIFICATION_HANDBACK = "handbackObject";

	private MBeanServer mbeanServer;

	private Standard standardMBean;

	private StandardMBeanNotificationListener notificationListener;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String[] getTestBundlesNames() {
		return new String[] { Constants.ARTIFACT_SWITCHBOARD };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String[] getConfigLocations() {
		return SPRING_CONFIG_LOCATIONS;
	}

	/**
	 * {@inheritDoc}
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSetUp() {
		this.mbeanServer = (MBeanServer) super.getApplicationContext().getBean(
				BEAN_MBEANSERVER);
		assertNotNull(this.mbeanServer);
		this.standardMBean = (Standard) super.getApplicationContext().getBean(
				BEAN_STANDARDMBEAN);
		assertNotNull(this.standardMBean);
		this.notificationListener = (StandardMBeanNotificationListener) super
				.getApplicationContext().getBean(BEAN_NOTIFICATIONLISTENER);
	}

	/**
	 * Test if MBean registration is done.
	 * 
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 */
	public void test_mbeanRegistrationDone()
			throws MalformedObjectNameException, NullPointerException {
		assertNotNull(this.standardMBean.isRegistrationDone());
		assertTrue(this.standardMBean.isRegistrationDone().booleanValue());
		assertEquals(new ObjectName(OBJECTNAME), this.standardMBean
				.getObjectName());
	}

	/**
	 * Test notification listener.
	 * 
	 * @throws InterruptedException
	 */
	public void test_notificationListener() throws InterruptedException {
		String newValue = "a new value";
		this.standardMBean.setAttribute(newValue);
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