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
package com.buschmais.osgi.maexo.test.common.mbeans;

import java.util.concurrent.atomic.AtomicLong;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;

public class Classic extends NotificationBroadcasterSupport implements
		ClassicMBean, MBeanRegistration, MBeanRegistrationValidator {

	public static String NOTIFICATION_DESCRIPTION = "value of attribute changed";

	private String attribute;

	private AtomicLong sequenceNumber = new AtomicLong(0);

	private ObjectName objectName;

	private Boolean registrationDone;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.test.common.mbeans.ClassicMBean#getAttribute()
	 */
	public String getAttribute() {
		return this.attribute;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.test.common.mbeans.ClassicMBean#setAttribute
	 * (java.lang.String)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.test.common.mbeans.ClassicMBean#operation(boolean
	 * )
	 */
	public boolean operation(boolean value) {
		return !value;
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
		this.registrationDone = registrationDone;
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
		this.objectName = name;
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.test.common.mbeans.MBeanRegistrationValidator
	 * #isRegistrationDone()
	 */
	public Boolean isRegistrationDone() {
		return registrationDone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.test.common.mbeans.MBeanRegistrationValidator
	 * #getObjectName()
	 */
	public ObjectName getObjectName() {
		return objectName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.management.NotificationBroadcasterSupport#getNotificationInfo()
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
