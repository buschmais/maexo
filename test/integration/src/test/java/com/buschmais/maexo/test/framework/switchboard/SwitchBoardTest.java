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
package com.buschmais.maexo.test.framework.switchboard;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.management.DynamicMBean;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.easymock.EasyMock;
import org.osgi.framework.ServiceRegistration;

import com.buschmais.maexo.test.Constants;
import com.buschmais.maexo.test.MaexoTests;
import com.buschmais.maexo.test.common.mbeans.StandardMBean;

/**
 * @see MaexoTests
 */
public class SwitchBoardTest extends MaexoTests {

	private static final String OBJECTNAME_TESTMBEAN = "com.buschmais.maexo:type=StandardMBean";

	private ObjectName objectName;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		this.objectName = new ObjectName(OBJECTNAME_TESTMBEAN);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onTearDown() throws Exception {
		super.onTearDown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String[] getTestBundlesNames() {
		return new String[] { Constants.ARTIFACT_SWITCHBOARD,
				Constants.ARTIFACT_EASYMOCK };
	}

	/**
	 * Registers an mbean server instance as OSGi service.
	 * 
	 * @param mbeanServer
	 *            the mbean server
	 * @return the service registration
	 */
	private ServiceRegistration registerMBeanServer(MBeanServer mbeanServer) {
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		// provide an agentId
		properties.put("agentId", "localhost");
		return super.bundleContext.registerService(MBeanServer.class.getName(),
				mbeanServer, properties);
	}

	/**
	 * First registers the mbean server and afterwards the mbean as services.
	 * 
	 * @param objectName
	 *            the object name
	 * @param mbean
	 *            the mbean
	 * @param mbeanInterface
	 *            the mbean interface
	 * @throws InstanceNotFoundException
	 * @throws MBeanRegistrationException
	 * @throws InstanceAlreadyExistsException
	 * @throws NotCompliantMBeanException
	 */
	private void test_registerMBeanOnExistingServer(ObjectName objectName,
			Object mbean, Class<?> mbeanInterface)
			throws InstanceNotFoundException, MBeanRegistrationException,
			InstanceAlreadyExistsException, NotCompliantMBeanException {
		// create mock for mbean server
		MBeanServer serverMock = EasyMock.createMock(MBeanServer.class);
		EasyMock.expect(serverMock.registerMBean(mbean, objectName)).andReturn(
				new ObjectInstance(objectName, mbean.getClass().getName()));
		serverMock.unregisterMBean(objectName);

		// do test
		EasyMock.replay(serverMock);
		// register mbean server
		ServiceRegistration serverServiceRegistration = this
				.registerMBeanServer(serverMock);
		// register/unregister mbean
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(ObjectName.class.getName(), objectName);
		ServiceRegistration mbeanServiceRegistration = this.bundleContext
				.registerService(mbeanInterface.getName(), mbean, properties);
		mbeanServiceRegistration.unregister();
		// verify mbean server
		EasyMock.verify(serverMock);
		serverServiceRegistration.unregister();
	}

	/**
	 * First registers the mbean and afterwards the mbean server as services.
	 * 
	 * @param objectName
	 *            the object name
	 * @param mbean
	 *            the mbean
	 * @param mbeanInterface
	 *            the mbean interface
	 * @throws InstanceNotFoundException
	 * @throws MBeanRegistrationException
	 * @throws InstanceAlreadyExistsException
	 * @throws NotCompliantMBeanException
	 */
	private void test_registerMBeanOnNewServer(ObjectName objectName,
			Object mbean, Class<?> mbeanInterface)
			throws InstanceNotFoundException, MBeanRegistrationException,
			InstanceAlreadyExistsException, NotCompliantMBeanException {
		// create mock for mbean server
		MBeanServer serverMock = EasyMock.createMock(MBeanServer.class);
		EasyMock.expect(serverMock.registerMBean(mbean, objectName)).andReturn(
				new ObjectInstance(objectName, mbean.getClass().getName()));
		serverMock.unregisterMBean(objectName);

		// do test
		EasyMock.replay(serverMock);
		// register mbean
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(ObjectName.class.getName(), objectName);
		ServiceRegistration mbeanServiceRegistration = this.bundleContext
				.registerService(mbeanInterface.getName(), mbean, properties);
		// register/unregister mbean server
		ServiceRegistration serverServiceRegistration = this
				.registerMBeanServer(serverMock);
		serverServiceRegistration.unregister();
		// verify mbean server
		EasyMock.verify(serverMock);
		// unregister mbean
		mbeanServiceRegistration.unregister();
	}

	/**
	 * Test mbean server first registration using standard mbeans.
	 * 
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 * @throws InstanceNotFoundException
	 * @throws InstanceAlreadyExistsException
	 * @throws MBeanRegistrationException
	 * @throws NotCompliantMBeanException
	 */
	public void test_registerStandardMBeanOnExistingServer()
			throws MalformedObjectNameException, NullPointerException,
			InstanceNotFoundException, InstanceAlreadyExistsException,
			MBeanRegistrationException, NotCompliantMBeanException {
		// create a mbean
		StandardMBean mbean = EasyMock.createMock(StandardMBean.class);
		this.test_registerMBeanOnExistingServer(this.objectName, mbean,
				StandardMBean.class);
	}

	/**
	 * Test mbean server first registration using dynamic mbeans.
	 * 
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 * @throws InstanceNotFoundException
	 * @throws InstanceAlreadyExistsException
	 * @throws MBeanRegistrationException
	 * @throws NotCompliantMBeanException
	 */
	public void test_registerDynamicMBeanOnExistingServer()
			throws MalformedObjectNameException, NullPointerException,
			InstanceNotFoundException, InstanceAlreadyExistsException,
			MBeanRegistrationException, NotCompliantMBeanException {
		// create a mbean
		DynamicMBean mbean = EasyMock.createMock(DynamicMBean.class);
		this.test_registerMBeanOnExistingServer(this.objectName, mbean,
				DynamicMBean.class);
	}

	/**
	 * Test mbean first registration using standard mbeans.
	 * 
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 * @throws InstanceNotFoundException
	 * @throws InstanceAlreadyExistsException
	 * @throws MBeanRegistrationException
	 * @throws NotCompliantMBeanException
	 */
	public void test_registerStandardMBeanOnNewServer()
			throws MalformedObjectNameException, NullPointerException,
			InstanceAlreadyExistsException, MBeanRegistrationException,
			NotCompliantMBeanException, InstanceNotFoundException {
		// create a mbean
		StandardMBean mbean = EasyMock.createMock(StandardMBean.class);
		this.test_registerMBeanOnNewServer(this.objectName, mbean,
				StandardMBean.class);
	}

	/**
	 * Test mbean first registration using dynamic mbeans.
	 * 
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 * @throws InstanceNotFoundException
	 * @throws InstanceAlreadyExistsException
	 * @throws MBeanRegistrationException
	 * @throws NotCompliantMBeanException
	 */
	public void test_registerDynamicMBeanOnNewServer()
			throws MalformedObjectNameException, NullPointerException,
			InstanceAlreadyExistsException, MBeanRegistrationException,
			NotCompliantMBeanException, InstanceNotFoundException {
		// create a mbean
		DynamicMBean mbean = EasyMock.createMock(DynamicMBean.class);
		this.test_registerMBeanOnNewServer(this.objectName, mbean,
				DynamicMBean.class);
	}

	/**
	 * Registers an mbean server connection instance as OSGi service.
	 * 
	 * @param mbeanServer
	 *            the mbean server connection
	 * @return the service registration
	 */
	private ServiceRegistration registerMBeanServerConnection(
			MBeanServerConnection mbeanServerConnection) {
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		// provide an agentId
		properties.put("agentId", "localhost");
		return super.bundleContext.registerService(MBeanServerConnection.class
				.getName(), mbeanServerConnection, properties);
	}

	/**
	 * First registers the mbean server connection and afterwards registers the
	 * notification listener as services.
	 */
	public void test_addNotificationListenerOnExistingServerConnection()
			throws Exception {
		ObjectName objectName = new ObjectName(OBJECTNAME_TESTMBEAN);
		NotificationListener notificationListener = EasyMock
				.createMock(NotificationListener.class);
		NotificationFilter notificationFilter = EasyMock
				.createMock(NotificationFilter.class);
		Object handback = "handbackObject";
		// create mock for mbean server connection
		MBeanServerConnection serverConnectionMock = EasyMock
				.createMock(MBeanServerConnection.class);
		serverConnectionMock.addNotificationListener(objectName,
				notificationListener, notificationFilter, handback);
		serverConnectionMock.removeNotificationListener(objectName,
				notificationListener, notificationFilter, handback);

		// do test
		EasyMock.replay(serverConnectionMock);
		// register mbean server connection
		ServiceRegistration serverConnectionServiceRegistration = this
				.registerMBeanServerConnection(serverConnectionMock);
		// register/unregister notification listener
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(ObjectName.class.getName(), objectName);
		properties.put(NotificationFilter.class.getName(), notificationFilter);
		properties.put("handback", handback);
		ServiceRegistration notificationListenerServiceRegistration = this.bundleContext
				.registerService(NotificationListener.class.getName(),
						notificationListener, properties);
		notificationListenerServiceRegistration.unregister();
		// verify mbean server connection
		EasyMock.verify(serverConnectionMock);
		serverConnectionServiceRegistration.unregister();
	}

	/**
	 * First registers the notification listener and afterwards the mbean server
	 * connection as services.
	 */
	public void test_addNotificationListenerOnNewServerConnection()
			throws Exception {
		// create mock for mbean server connection
		MBeanServerConnection serverConnectionMock = EasyMock
				.createMock(MBeanServerConnection.class);
		ObjectName objectName = new ObjectName(OBJECTNAME_TESTMBEAN);
		NotificationListener notificationListener = EasyMock
				.createMock(NotificationListener.class);
		NotificationFilter notificationFilter = EasyMock
				.createMock(NotificationFilter.class);
		Object handback = "handbackObject";
		serverConnectionMock.addNotificationListener(objectName,
				notificationListener, notificationFilter, handback);
		serverConnectionMock.removeNotificationListener(objectName,
				notificationListener, notificationFilter, handback);

		// do test
		EasyMock.replay(serverConnectionMock);
		// register notification listener
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(ObjectName.class.getName(), objectName);
		properties.put(NotificationFilter.class.getName(), notificationFilter);
		properties.put("handback", handback);
		ServiceRegistration notificationListenerServiceRegistration = this.bundleContext
				.registerService(NotificationListener.class.getName(),
						notificationListener, properties);
		// register/unregister mbean server connection
		ServiceRegistration serverServiceRegistration = this
				.registerMBeanServerConnection(serverConnectionMock);
		serverServiceRegistration.unregister();
		// verify mbean server connection
		EasyMock.verify(serverConnectionMock);
		// unregister notification listener
		notificationListenerServiceRegistration.unregister();
	}
}