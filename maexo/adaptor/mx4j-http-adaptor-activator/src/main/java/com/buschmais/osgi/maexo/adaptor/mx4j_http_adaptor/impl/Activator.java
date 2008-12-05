package com.buschmais.osgi.maexo.adaptor.mx4j_http_adaptor.impl;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.management.ObjectName;

import mx4j.tools.adaptor.http.HttpAdaptor;
import mx4j.tools.adaptor.http.HttpAdaptorMBean;
import mx4j.tools.adaptor.http.XSLTProcessor;
import mx4j.tools.adaptor.http.XSLTProcessorMBean;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Activator for the MX4J HTTP adaptor
 */
public class Activator implements BundleActivator {

	public static final String HTTP_ADAPTOR_OBJECTNAME = "com.buschmais.osgi.maexo:type=adaptor,name=mx4j_http_adaptor";

	public static final String HOST_PROPERTY = "com.buschmais.osgi.maexo.adaptor.mx4j_http_adaptor.host";
	public static final String HOST_DEFAULT_VALUE = "localhost";

	public static final String PORT_PROPERTY = "com.buschmais.osgi.maexo.adaptor.mx4j_http_adaptor.port";
	public static final String PORT_DEFAULT_VALUE = "8081";

	public static final String AUTHENTICATION_PROPERTY = "com.buschmais.osgi.maexo.adaptor.mx4j_http_adaptor.authentication";
	public static final String AUTHENTICATION_DEFAULT_VALUE = "none";

	public static final String USER_PROPERTY = "com.buschmais.osgi.maexo.adaptor.mx4j_http_adaptor.user";
	public static final String USER_DEFAULT_VALUE = "admin";

	public static final String PASSWORD_PROPERTY = "com.buschmais.osgi.maexo.adaptor.mx4j_http_adaptor.password";
	public static final String PASSWORD_DEFAULT_VALUE = "maexo";

	public static final String STYLESHEET_PROPERTY = "com.buschmais.osgi.maexo.adaptor.mx4j_http_adaptor.stylesheet";
	public static final String STYLESHEET_DEFAULT_VALUE = "/mx4j/tools/adaptor/http/xsl";

	public static final String START_PROPERTY = "com.buschmais.osgi.maexo.adaptor.mx4j_http_adaptor.start";
	public static final String START_DEFAULT_VALUE = "true";

	private static final Logger logger = LoggerFactory
			.getLogger(Activator.class);

	private static final Map<String, String> CONFIGURATION_PROPERTIES = new HashMap<String, String>();

	static {
		CONFIGURATION_PROPERTIES.put(HOST_PROPERTY, HOST_DEFAULT_VALUE);
		CONFIGURATION_PROPERTIES.put(PORT_PROPERTY, PORT_DEFAULT_VALUE);
		CONFIGURATION_PROPERTIES.put(AUTHENTICATION_PROPERTY,
				AUTHENTICATION_DEFAULT_VALUE);
		CONFIGURATION_PROPERTIES.put(USER_PROPERTY, USER_DEFAULT_VALUE);
		CONFIGURATION_PROPERTIES.put(PASSWORD_PROPERTY, PASSWORD_DEFAULT_VALUE);
		CONFIGURATION_PROPERTIES.put(STYLESHEET_PROPERTY,
				STYLESHEET_DEFAULT_VALUE);
		CONFIGURATION_PROPERTIES.put(START_PROPERTY, START_DEFAULT_VALUE);
	}

	private HttpAdaptorMBean httpAdaptorMBean;

	private ServiceRegistration serviceRegistration;

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
			logger.info("creating MX4J HTTP adaptor with properties "
					+ properties);
		}
		// create instance of the http adaptor mbean and apply configuration
		this.httpAdaptorMBean = new HttpAdaptor();
		this.httpAdaptorMBean.setHost(properties.getProperty(HOST_PROPERTY));
		this.httpAdaptorMBean.setPort(Integer.parseInt(properties
				.getProperty(PORT_PROPERTY)));
		this.httpAdaptorMBean.setAuthenticationMethod(properties
				.getProperty(AUTHENTICATION_PROPERTY));
		this.httpAdaptorMBean.addAuthorization(properties
				.getProperty(USER_PROPERTY), properties
				.getProperty(PASSWORD_PROPERTY));
		// create instance of the xsl processor
		XSLTProcessorMBean xsltProcessorMBean = new XSLTProcessor();
		xsltProcessorMBean.setPathInJar(properties
				.getProperty(STYLESHEET_PROPERTY));
		this.httpAdaptorMBean.setProcessor(xsltProcessorMBean);
		// create object name
		Dictionary serviceProperties = new Hashtable();
		ObjectName objectName = new ObjectName(HTTP_ADAPTOR_OBJECTNAME);
		serviceProperties.put("objectName", objectName);
		// register mbean
		this.serviceRegistration = bundleContext.registerService(
				HttpAdaptorMBean.class.getName(), this.httpAdaptorMBean,
				serviceProperties);
		// start http adaptor
		if (Boolean.parseBoolean(properties.getProperty(START_PROPERTY))) {
			if (logger.isInfoEnabled()) {
				logger.info("starting MX4J HTTP adaptor");
			}
			this.httpAdaptorMBean.start();
		}
	}

	/**
	 * Reads the configuration properties from the bundle context and applies
	 * default values if necessary
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
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
		}
		if (this.httpAdaptorMBean != null && this.httpAdaptorMBean.isActive()) {
			if (logger.isInfoEnabled()) {
				logger.info("stopping MX4J HTTP adaptor");
			}
			this.httpAdaptorMBean.stop();
		}
	}

}
