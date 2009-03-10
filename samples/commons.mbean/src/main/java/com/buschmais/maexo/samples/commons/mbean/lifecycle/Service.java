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
 * Defines the sample service which will be published in the OSGi service
 * registry and is going to be managed by a {@link ServiceMBean}.
 */
public interface Service {

	/**
	 * Logs the provided message.
	 *
	 * @param message
	 *            The message.
	 */
	void hello(String message);
}
