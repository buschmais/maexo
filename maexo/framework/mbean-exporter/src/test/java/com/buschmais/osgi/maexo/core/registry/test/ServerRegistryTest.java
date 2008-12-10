package com.buschmais.osgi.maexo.core.registry.test;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.management.DynamicMBean;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.easymock.EasyMock;
import org.osgi.framework.ServiceRegistration;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;

/**
 * @see AbstractConfigurableBundleCreatorTests
 */
public class ServerRegistryTest extends AbstractConfigurableBundleCreatorTests {

	private static final String OBJECTNAME_TESTMBEAN = "com.buschmais.osgi.maexo:type=TestMBean";

	private ObjectName objectName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.test.AbstractSingleSpringContextTests#onSetUp()
	 */
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		this.objectName = new ObjectName(OBJECTNAME_TESTMBEAN);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.test.AbstractSingleSpringContextTests#onTearDown()
	 */
	@Override
	protected void onTearDown() throws Exception {
		super.onTearDown();
	}

	protected String[] getTestBundlesNames() {
		return new String[] {
				"com.buschmais.osgi.maexo.core, maexo-server-registry, 1.0.0-SNAPSHOT",
				"org.easymock, com.springsource.org.easymock, 2.3.0" };
	}

	/**
	 * First registers the mbean server and afterwards the mbean as services
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
		ServiceRegistration serverServiceRegistration = super.bundleContext
				.registerService(MBeanServer.class.getName(), serverMock, null);
		// register/unregister mbean
		Dictionary<String, ObjectName> properties = new Hashtable<String, ObjectName>();
		properties.put(ObjectName.class.getName(), objectName);
		ServiceRegistration mbeanServiceRegistration = this.bundleContext
				.registerService(mbeanInterface.getName(), mbean, properties);
		mbeanServiceRegistration.unregister();
		// verify mbean server
		EasyMock.verify(serverMock);
		serverServiceRegistration.unregister();
	}

	/**
	 * First registers the mbean and afterwards the mbean server as services
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
		Dictionary<String, ObjectName> properties = new Hashtable<String, ObjectName>();
		properties.put(ObjectName.class.getName(), objectName);
		ServiceRegistration mbeanServiceRegistration = this.bundleContext
				.registerService(mbeanInterface.getName(), mbean, properties);
		// register/unregister mbean server
		ServiceRegistration serverServiceRegistration = super.bundleContext
				.registerService(MBeanServer.class.getName(), serverMock, null);
		serverServiceRegistration.unregister();
		// verify mbean server
		EasyMock.verify(serverMock);
		// unregister mbean
		mbeanServiceRegistration.unregister();
	}

	/**
	 * test mbean server first registration using classic mbeans
	 * 
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 * @throws InstanceNotFoundException
	 * @throws InstanceAlreadyExistsException
	 * @throws MBeanRegistrationException
	 * @throws NotCompliantMBeanException
	 */
	public void test_registerClassicMBeanOnExistingServer()
			throws MalformedObjectNameException, NullPointerException,
			InstanceNotFoundException, InstanceAlreadyExistsException,
			MBeanRegistrationException, NotCompliantMBeanException {
		// create a mbean
		TestMBean mbean = (TestMBean) EasyMock.createMock(TestMBean.class);
		this.test_registerMBeanOnExistingServer(this.objectName, mbean,
				TestMBean.class);
	}

	/**
	 * test mbean server first registration using dynamic mbeans
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
		DynamicMBean mbean = (DynamicMBean) EasyMock
				.createMock(DynamicMBean.class);
		this.test_registerMBeanOnExistingServer(this.objectName, mbean,
				DynamicMBean.class);
	}

	/**
	 * test mbean first registration using classic mbeans
	 * 
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 * @throws InstanceNotFoundException
	 * @throws InstanceAlreadyExistsException
	 * @throws MBeanRegistrationException
	 * @throws NotCompliantMBeanException
	 */
	public void test_registerClassicMBeanOnNewServer()
			throws MalformedObjectNameException, NullPointerException,
			InstanceAlreadyExistsException, MBeanRegistrationException,
			NotCompliantMBeanException, InstanceNotFoundException {
		// create a mbean
		TestMBean mbean = (TestMBean) EasyMock.createMock(TestMBean.class);
		this.test_registerMBeanOnNewServer(this.objectName, mbean,
				TestMBean.class);
	}

	/**
	 * test mbean first registration using dynamic mbeans
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
		DynamicMBean mbean = (DynamicMBean) EasyMock
				.createMock(DynamicMBean.class);
		this.test_registerMBeanOnNewServer(this.objectName, mbean,
				DynamicMBean.class);
	}
}