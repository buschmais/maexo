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
package com.buschmais.maexo.test;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.management.MBeanServerDelegateMBean;
import javax.management.MalformedObjectNameException;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.osgi.framework.ServiceRegistration;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;

/**
 * @see AbstractConfigurableBundleCreatorTests
 */
public class MaexoTests extends AbstractConfigurableBundleCreatorTests {

	/** Object name of the {@link MBeanServerDelegateMBean}. */
	protected static final String MBEANSERVERDELEGATE_OBJECTNAME = "JMImplementation:type=MBeanServerDelegate";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String[] getTestFrameworkBundlesNames() {
		return Constants.TEST_FRAMEWORK_BUNDLES_NAMES;
	}

	/**
	 * Registers a notification listener which listens to events.
	 *
	 * @return The listener.
	 * @throws MalformedObjectNameException
	 *             If notification listeners object name is incorrect.
	 */
	protected ServiceRegistration registerNotificationListener(
			NotificationListener listener, ObjectName objectName)
			throws MalformedObjectNameException {
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(ObjectName.class.getName(), objectName);
		ServiceRegistration notificationListenerServiceRegistration = this.bundleContext
				.registerService(NotificationListener.class.getName(), listener,
						properties);
		return notificationListenerServiceRegistration;
	}

}
