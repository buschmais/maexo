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
package com.buschmais.maexo.samples.commons.mbean.lifecycle;

import javax.management.DynamicMBean;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.openmbean.OpenMBeanAttributeInfo;
import javax.management.openmbean.OpenMBeanConstructorInfo;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfo;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.OpenMBeanParameterInfo;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;
import javax.management.openmbean.SimpleType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.maexo.framework.commons.mbean.dynamic.DynamicMBeanSupport;

/**
 * This class represents a {@link DynamicMBean} which manages a {@link Service}
 * instance.
 * <p>
 * It provides the operation {@link #hello(String)} which delegates to the
 * corresponding service method.
 */
public class ServiceMBean extends DynamicMBeanSupport {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ServiceMBean.class);

	/**
	 * The instance of the service which is managed by this MBean.
	 */
	private Service service;

	/**
	 * Constructs the MBean. It takes an instance of {@link Service} as
	 * parameter which will be managed by this MBean.
	 *
	 * @param service
	 *            The service to manage.
	 */
	public ServiceMBean(Service service) {
		this.service = service;
	}

	/**
	 * Returns meta data for this MBean. This method is an implementation of the
	 * method declared in {@link DynamicMBean#getMBeanInfo()}.
	 */
	public MBeanInfo getMBeanInfo() {
		String className = this.getClass().getName();
		OpenMBeanOperationInfo[] mbeanOperations = new OpenMBeanOperationInfo[] { new OpenMBeanOperationInfoSupport(
				"hello",
				"Sends a message to the service which is managed by this MBean.",
				new OpenMBeanParameterInfo[] { new OpenMBeanParameterInfoSupport(
						"message", "The message.", SimpleType.STRING) },
				SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION) };
		return new OpenMBeanInfoSupport(className,
				"An OpenMBean which represents a service.",
				new OpenMBeanAttributeInfo[] {},
				new OpenMBeanConstructorInfo[] {}, mbeanOperations,
				new MBeanNotificationInfo[] {});
	}

	/**
	 * This MBean operation will forward the message to the underlying service
	 * instance.
	 *
	 * @param message
	 *            The message.
	 */
	public void hello(String message) {
		logger.info("operation \"hello\" invoked on service MBean");
		this.service.hello(message + (" [invoked by MBean]"));
	}

}
