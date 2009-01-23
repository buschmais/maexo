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
package com.buschmais.maexo.test.common.mbeans;

import java.util.concurrent.atomic.AtomicLong;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;

public class Standard extends NotificationBroadcasterSupport implements
		StandardMBean, MBeanRegistration, MBeanRegistrationValidator {

	public static String NOTIFICATION_DESCRIPTION = "value of attribute changed";

	/** The attribute. */
	private String attribute;

	/** The sequence number. */
	private final AtomicLong sequenceNumber = new AtomicLong(0);

	/** The object name. */
	private ObjectName objectName;

	/** The value if registration is done. */
	private Boolean registrationDone;

	/**
	 * {@inheritDoc}
	 */
	public String getAttribute() {
		return this.attribute;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAttribute(String newAttribute) {
		String oldAttribute = this.attribute;
		this.attribute = newAttribute;
		// send a notification if the attribute changes
		super.sendNotification(new AttributeChangeNotification(this.objectName,
				this.sequenceNumber.incrementAndGet(), System
						.currentTimeMillis(), "attribute", "attribute changed",
				String.class.getName(), oldAttribute, newAttribute));
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean operation(boolean value) {
		return !value;
	}

	/**
	 * {@inheritDoc}
	 */
	public void postDeregister() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void postRegister(Boolean registrationDone) {
		this.registrationDone = registrationDone;
	}

	/**
	 * {@inheritDoc}
	 */
	public void preDeregister() throws Exception {
	}

	/**
	 * {@inheritDoc}
	 */
	public ObjectName preRegister(MBeanServer server, ObjectName name)
			throws Exception {
		this.objectName = name;
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean isRegistrationDone() {
		return registrationDone;
	}

	/**
	 * {@inheritDoc}
	 */
	public ObjectName getObjectName() {
		return objectName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MBeanNotificationInfo[] getNotificationInfo() {
		MBeanNotificationInfo mbeanNotificationInfo = new MBeanNotificationInfo(
				new String[] { AttributeChangeNotification.ATTRIBUTE_CHANGE },
				AttributeChangeNotification.class.getName(),
				"attribute changed");
		return new MBeanNotificationInfo[] { mbeanNotificationInfo };
	}
}
