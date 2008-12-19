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
package com.buschmais.osgi.maexo.test.framework.commons.mbean;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.easymock.EasyMock;
import org.osgi.framework.ServiceRegistration;

import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameFactory;
import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameHelper;
import com.buschmais.osgi.maexo.test.Constants;
import com.buschmais.osgi.maexo.test.MaexoTests;

/**
 * @see MaexoTests
 */
public class ObjectNameHelperTest extends MaexoTests {

	private static final String OBJECTNAME_RESOURCE_A = "com.buschmais.osgi.maexo:type=ResourceA";
	private static final String OBJECTNAME_RESOURCE_B = "com.buschmais.osgi.maexo:type=ResourceB";

	private ObjectNameHelper objectNameHelper;

	private ObjectNameFactory objectNameFactoryA;
	private ServiceRegistration serviceRegistrationA;
	private ObjectNameFactory objectNameFactoryB;
	private ServiceRegistration serviceRegistrationB;

	private ResourceInterfaceA resourceA;
	private ResourceInterfaceB resourceB;

	private ObjectName objectNameA;
	private ObjectName objectNameB;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.test.AbstractSingleSpringContextTests#onSetUp()
	 */
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		// create helper
		this.objectNameHelper = new ObjectNameHelper(super.bundleContext);
		// create and register factory mocks
		this.objectNameFactoryA = EasyMock.createMock(ObjectNameFactory.class);
		this.serviceRegistrationA = this.objectNameHelper
				.registerObjectNameFactory(this.objectNameFactoryA,
						ResourceInterfaceA.class);
		this.objectNameFactoryB = EasyMock.createMock(ObjectNameFactory.class);
		this.serviceRegistrationB = this.objectNameHelper
				.registerObjectNameFactory(this.objectNameFactoryB,
						ResourceInterfaceB.class);
		// create resource mocks
		this.resourceA = EasyMock.createMock(ResourceInterfaceA.class);
		this.resourceB = EasyMock.createMock(ResourceInterfaceB.class);
		this.objectNameA = new ObjectName(OBJECTNAME_RESOURCE_A);
		this.objectNameB = new ObjectName(OBJECTNAME_RESOURCE_B);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.test.AbstractSingleSpringContextTests#onTearDown()
	 */
	@Override
	protected void onTearDown() throws Exception {
		this.serviceRegistrationA.unregister();
		this.serviceRegistrationB.unregister();
		super.onTearDown();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.osgi.test.AbstractDependencyManagerTests#
	 * getTestBundlesNames()
	 */
	protected String[] getTestBundlesNames() {
		return new String[] { Constants.ARTIFACT_COMMONS_MBEAN,
				Constants.ARTIFACT_EASYMOCK };
	}

	public void test_getObjectNameFromInterface()
			throws MalformedObjectNameException, NullPointerException {
		// expect calls to object name factories
		EasyMock.expect(this.objectNameFactoryA.getObjectName(this.resourceA))
				.andReturn(this.objectNameA);
		EasyMock.expect(this.objectNameFactoryB.getObjectName(this.resourceB))
				.andReturn(this.objectNameB);
		// replay
		EasyMock.replay(this.objectNameFactoryA);
		EasyMock.replay(this.objectNameFactoryB);
		assertEquals(this.objectNameA, this.objectNameHelper.getObjectName(
				this.resourceA, ResourceInterfaceA.class));
		assertEquals(this.objectNameB, this.objectNameHelper.getObjectName(
				this.resourceB, ResourceInterfaceB.class));
		// verify
		EasyMock.verify(this.objectNameFactoryA);
		EasyMock.verify(this.objectNameFactoryB);
	}

	public void test_getObjectNameFromResource() {
		// expect calls to object name factories
		EasyMock.expect(this.objectNameFactoryA.getObjectName(this.resourceA))
				.andReturn(this.objectNameA);
		EasyMock.expect(this.objectNameFactoryB.getObjectName(this.resourceB))
				.andReturn(this.objectNameB);
		// replay
		EasyMock.replay(this.objectNameFactoryA);
		EasyMock.replay(this.objectNameFactoryB);
		assertEquals(this.objectNameA, this.objectNameHelper
				.getObjectName(this.resourceA));
		assertEquals(this.objectNameB, this.objectNameHelper
				.getObjectName(this.resourceB));
		// verify
		EasyMock.verify(this.objectNameFactoryA);
		EasyMock.verify(this.objectNameFactoryB);
	}
}