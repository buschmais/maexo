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
package com.buschmais.osgi.maexo.adaptor.mx4j_http_adaptor.impl;

import mx4j.tools.adaptor.http.HttpAdaptorMBean;

/**
 * Managment interface for the MX4J HTTP adaptor.
 */
public interface Mx4jHttpAdaptorMBean extends HttpAdaptorMBean {

	/**
	 * @return the startOnRegistration
	 */
	public boolean isStartOnRegistration();

	/**
	 * @param startOnRegistration
	 *            the startOnRegistration to set
	 */
	public void setStartOnRegistration(boolean startOnRegistration);

}
