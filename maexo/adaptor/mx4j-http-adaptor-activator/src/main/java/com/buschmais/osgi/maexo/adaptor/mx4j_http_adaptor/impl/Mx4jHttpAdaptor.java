package com.buschmais.osgi.maexo.adaptor.mx4j_http_adaptor.impl;

import java.io.IOException;

import mx4j.tools.adaptor.http.HttpAdaptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mx4jHttpAdaptor extends HttpAdaptor implements
		Mx4jHttpAdaptorMBean {

	private static final Logger logger = LoggerFactory
			.getLogger(Mx4jHttpAdaptor.class);

	private boolean startOnRegistration;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.adaptor.mx4j_http_adaptor.impl.Mx4jHttpAdaptorMBean
	 * #isStartOnRegistration()
	 */
	public boolean isStartOnRegistration() {
		return startOnRegistration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgi.maexo.adaptor.mx4j_http_adaptor.impl.Mx4jHttpAdaptorMBean
	 * #setStartOnRegistration(boolean)
	 */
	public void setStartOnRegistration(boolean startOnRegistration) {
		this.startOnRegistration = startOnRegistration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx4j.tools.adaptor.http.HttpAdaptor#postRegister(java.lang.Boolean)
	 */
	@Override
	public void postRegister(Boolean registrationDone) {
		super.postRegister(registrationDone);
		if (!super.isActive() && this.startOnRegistration
				&& registrationDone != null && registrationDone.booleanValue()) {
			try {
				super.start();
			} catch (IOException e) {
				logger.error("cannot start MX4J HTTP adaptor", e);
			}
		}
	}

}
