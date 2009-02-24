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

/**
 * Defines the interface an MBean which will be used to control the life cycle
 * of the {@link Service}. The goal is to demonstrate the functionality of
 * the class {@link ServiceMBeanLifeCycle}.
 */
public interface ServicePublisherMBean {

	/**
	 * Registers a {@link Service} instance in the OSGi service registry.
	 */
	public void registerService();

	/**
	 * Unregisters a formerly registered instance of {@link Service} from
	 * the OSGi service registry.
	 */
	public void unregisterService();
}
