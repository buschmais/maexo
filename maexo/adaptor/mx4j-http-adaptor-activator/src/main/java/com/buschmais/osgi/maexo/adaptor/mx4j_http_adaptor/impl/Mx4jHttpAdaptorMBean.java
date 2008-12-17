package com.buschmais.osgi.maexo.adaptor.mx4j_http_adaptor.impl;

import mx4j.tools.adaptor.http.HttpAdaptorMBean;

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
