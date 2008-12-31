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

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.management.ObjectName;

import mx4j.tools.adaptor.http.HttpAdaptorMBean;
import mx4j.tools.adaptor.http.XSLTProcessor;
import mx4j.tools.adaptor.http.XSLTProcessorMBean;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.osgi.maexo.adaptor.mx4j_http_adaptor.Constants;

/**
 * Activator for the MX4J HTTP adaptor.
 */
public class Activator implements BundleActivator {

	private static final Logger logger = LoggerFactory
			.getLogger(Activator.class);

	private static final Map<String, String> CONFIGURATION_PROPERTIES = new HashMap<String, String>();

	static {
		// initialize the default configuration
		CONFIGURATION_PROPERTIES.put(Constants.HOST_PROPERTY,
				Constants.HOST_DEFAULT_VALUE);
		CONFIGURATION_PROPERTIES.put(Constants.PORT_PROPERTY,
				Constants.PORT_DEFAULT_VALUE);
		CONFIGURATION_PROPERTIES.put(Constants.AUTHENTICATION_PROPERTY,
				Constants.AUTHENTICATION_DEFAULT_VALUE);
		CONFIGURATION_PROPERTIES.put(Constants.USER_PROPERTY,
				Constants.USER_DEFAULT_VALUE);
		CONFIGURATION_PROPERTIES.put(Constants.PASSWORD_PROPERTY,
				Constants.PASSWORD_DEFAULT_VALUE);
		CONFIGURATION_PROPERTIES.put(Constants.STYLESHEET_PROPERTY,
				Constants.STYLESHEET_DEFAULT_VALUE);
		CONFIGURATION_PROPERTIES.put(Constants.START_PROPERTY,
				Constants.START_DEFAULT_VALUE);
	}

	private ServiceRegistration adaptorServiceRegistration;

	private ServiceRegistration processorServiceRegistration;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@SuppressWarnings("unchecked")
	public void start(BundleContext bundleContext) throws Exception {
		// get configuration
		Properties properties = this.getProperties(bundleContext);
		if (logger.isInfoEnabled()) {
			logger.info("Registering MX4J HTTP adaptor");
			for (String key : CONFIGURATION_PROPERTIES.keySet()) {
				logger.info("\t" + key + ": " + properties.getProperty(key));
			}
		}
		// create instance of the http adaptor mbean and apply configuration
		Mx4jHttpAdaptorMBean httpAdaptorMBean = new Mx4jHttpAdaptor();
		httpAdaptorMBean.setStartOnRegistration(Boolean.parseBoolean(properties
				.getProperty(Constants.START_PROPERTY)));
		httpAdaptorMBean.setHost(properties
				.getProperty(Constants.HOST_PROPERTY));
		httpAdaptorMBean.setPort(Integer.parseInt(properties
				.getProperty(Constants.PORT_PROPERTY)));
		httpAdaptorMBean.setAuthenticationMethod(properties
				.getProperty(Constants.AUTHENTICATION_PROPERTY));
		httpAdaptorMBean.addAuthorization(properties
				.getProperty(Constants.USER_PROPERTY), properties
				.getProperty(Constants.PASSWORD_PROPERTY));
		// create instance of the xsl processor
		XSLTProcessorMBean xsltProcessorMBean = new XSLTProcessor();
		xsltProcessorMBean.setPathInJar(properties
				.getProperty(Constants.STYLESHEET_PROPERTY));
		httpAdaptorMBean.setProcessor(xsltProcessorMBean);
		// create adaptor object name
		Dictionary adaptorServiceProperties = new Hashtable();
		ObjectName adaptorObjectName = new ObjectName(
				Constants.HTTP_ADAPTOR_OBJECTNAME);
		adaptorServiceProperties.put(javax.management.ObjectName.class
				.getName(), adaptorObjectName);
		// create processor object name
		Dictionary processorServiceProperties = new Hashtable();
		ObjectName processorObjectName = new ObjectName(
				Constants.XSLT_PROCESSOR_OBJECTNAME);
		processorServiceProperties.put(javax.management.ObjectName.class
				.getName(), processorObjectName);
		// register adaptor mbean
		this.adaptorServiceRegistration = bundleContext.registerService(
				HttpAdaptorMBean.class.getName(), httpAdaptorMBean,
				adaptorServiceProperties);
		// register processor mbean
		this.processorServiceRegistration = bundleContext.registerService(
				XSLTProcessorMBean.class.getName(), xsltProcessorMBean,
				processorServiceProperties);
	}

	/**
	 * Reads the configuration properties from the bundle context and applies
	 * default values if necessary.
	 * 
	 * @param bundleContext
	 *            the bundle context
	 * @return the configuration properties
	 */
	private Properties getProperties(BundleContext bundleContext) {
		Properties configuration = new Properties();
		// iterate over all known configuration properties
		for (Entry<String, String> entry : CONFIGURATION_PROPERTIES.entrySet()) {
			String key = entry.getKey();
			// fetch value from bundle context
			String value = bundleContext.getProperty(key);
			if (value == null) {
				// use default value if nothing provided by the configuration
				value = entry.getValue();
			}
			configuration.setProperty(key, value);
		}
		return configuration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("Unregistering MX4J HTTP adaptor");
		}
		if (this.processorServiceRegistration != null) {
			this.processorServiceRegistration.unregister();
		}
		if (this.adaptorServiceRegistration != null) {
			this.adaptorServiceRegistration.unregister();
		}
	}

}
