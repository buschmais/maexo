/*
 * Copyright 2009 buschmais GbR
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
package com.buschmais.maexo.samples.commons.mbean;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.management.DynamicMBean;
import javax.management.ObjectName;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;
import com.buschmais.maexo.samples.commons.mbean.lifecycle.ServiceMBeanLifeCycle;
import com.buschmais.maexo.samples.commons.mbean.lifecycle.ServicePublisher;
import com.buschmais.maexo.samples.commons.mbean.lifecycle.ServicePublisherMBean;
import com.buschmais.maexo.samples.commons.mbean.objectname.Address;
import com.buschmais.maexo.samples.commons.mbean.objectname.AddressMBean;
import com.buschmais.maexo.samples.commons.mbean.objectname.AddressObjectNameFactory;
import com.buschmais.maexo.samples.commons.mbean.objectname.Person;
import com.buschmais.maexo.samples.commons.mbean.objectname.PersonMBean;
import com.buschmais.maexo.samples.commons.mbean.objectname.PersonObjectNameFactory;
import com.buschmais.maexo.samples.commons.mbean.openmbean.OpenMBean;

/**
 * The bundle activator.
 * <p>
 * Registers
 */
public class Activator implements BundleActivator {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(Activator.class);

	/**
	 * The service registration of the service publisher MBean.
	 */
	private ServiceRegistration servicePublisherMBean;

	/**
	 * The service MBean life cycle instance.
	 */
	private ServiceMBeanLifeCycle serviceMBeanLifeCycle;

	/**
	 * The service registration of the open MBean.
	 */
	private ServiceRegistration openMBean;

	/**
	 * The service registration of the person MBean.
	 */
	private ServiceRegistration personMBean;

	/**
	 * The service registrations of the address MBeans.
	 */
	private List<ServiceRegistration> addressMBeans;

	/**
	 * {@inheritDoc}
	 */
	public void start(BundleContext bundleContext) throws Exception {
		// register services of MBean life cycle support sample
		logger.info("registering service publisher MBean.");
		Dictionary<String, Object> publisherProperties = new Hashtable<String, Object>();
		publisherProperties.put("objectName",
				"com.buschmais.maexo.sample:type=ServicePublisher");
		this.servicePublisherMBean = bundleContext.registerService(
				ServicePublisherMBean.class.getName(), new ServicePublisher(
						bundleContext), publisherProperties);

		logger.info("creating and starting ServiceMBeanLifeCycle instance.");
		this.serviceMBeanLifeCycle = new ServiceMBeanLifeCycle(bundleContext);
		this.serviceMBeanLifeCycle.start();

		// register services of Open MBeans sample
		logger.info("registering Open MBean.");
		Dictionary<String, Object> openMBeanProperties = new Hashtable<String, Object>();
		openMBeanProperties.put("objectName",
				"com.buschmais.maexo.sample:type=OpenMBean");
		this.openMBean = bundleContext.registerService(DynamicMBean.class
				.getName(), new OpenMBean(), openMBeanProperties);

		// register services of the object name factory sample
		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				bundleContext);
		objectNameFactoryHelper.registerObjectNameFactory(
				new PersonObjectNameFactory(), Person.class);
		objectNameFactoryHelper.registerObjectNameFactory(
				new AddressObjectNameFactory(), Address.class);

		// create a person
		Person person = new Person();
		person.setFirstName("Albert");
		person.setLastName("Einstein");
		// create and register the MBean for the person
		PersonMBean personMBean = new PersonMBean(person,
				objectNameFactoryHelper);
		Dictionary<String, Object> personMBeanProperties = new Hashtable<String, Object>();
		personMBeanProperties.put(ObjectName.class.getName(),
				objectNameFactoryHelper.getObjectName(person, Person.class));
		logger.info("registering PersonMBean.");
		this.personMBean = bundleContext.registerService(DynamicMBean.class
				.getName(), personMBean, personMBeanProperties);
		this.addressMBeans = new LinkedList<ServiceRegistration>();
		// create addresses and address MBean
		for (int i = 0; i < 10; i++) {
			Address address = new Address();
			address.setId(i);
			address.setPerson(person);
			person.getAdresses().add(address);
			AddressMBean addressMBean = new AddressMBean(address,
					objectNameFactoryHelper);
			Dictionary<String, Object> addressMBeanProperties = new Hashtable<String, Object>();
			addressMBeanProperties.put(ObjectName.class.getName(),
					objectNameFactoryHelper.getObjectName(address,
							Address.class));
			logger.info("registering AddressMBean {}.", Integer.valueOf(i));
			this.addressMBeans.add(bundleContext.registerService(
					DynamicMBean.class.getName(), addressMBean,
					addressMBeanProperties));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void stop(BundleContext arg0) throws Exception {
		logger.info("stopping ServiceMBeanLifeCycle instance.");
		this.serviceMBeanLifeCycle.stop();
		logger.info("unregistering service publisher MBean.");
		this.servicePublisherMBean.unregister();
		logger.info("unregistering Open MBean.");
		this.openMBean.unregister();
		logger.info("unregistering PersonMBean.");
		this.personMBean.unregister();
		logger.info("unregistering AddressMBeans.");
		for (ServiceRegistration addressMBean : this.addressMBeans) {
			addressMBean.unregister();
		}
	}

}
