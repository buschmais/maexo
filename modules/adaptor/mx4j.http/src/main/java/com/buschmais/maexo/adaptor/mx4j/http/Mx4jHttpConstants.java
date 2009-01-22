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
package com.buschmais.maexo.adaptor.mx4j.http;

/**
 * This class declares several global constants.
 */
public final class Mx4jHttpConstants {

	/**
	 * Private Constructor.
	 */
	private Mx4jHttpConstants() {

	}
	
	/** The http adapter object name. */
	public static final String HTTP_ADAPTOR_OBJECTNAME = "com.buschmais.maexo:type=Adaptor,name=Mx4jHttpAdaptor";

	/** The xslt processor object name. */
	public static final String XSLT_PROCESSOR_OBJECTNAME = "com.buschmais.maexo:type=Adaptor,name=Mx4jXsltProcessor";

	/** The host property. */
	public static final String HOST_PROPERTY = "com.buschmais.maexo.adaptor.mx4j.http.host";

	/** The host default value. */
	public static final String HOST_DEFAULT_VALUE = "localhost";

	/** The port property. */
	public static final String PORT_PROPERTY = "com.buschmais.maexo.adaptor.mx4j.http.port";

	/** The port default value. */
	public static final String PORT_DEFAULT_VALUE = "8081";

	/** The authentication property. */
	public static final String AUTHENTICATION_PROPERTY = "com.buschmais.maexo.adaptor.mx4j.http.authentication";

	/** The authentication default value. */
	public static final String AUTHENTICATION_DEFAULT_VALUE = "none";

	/** The user property. */
	public static final String USER_PROPERTY = "com.buschmais.maexo.adaptor.mx4j.http.user";

	/** The user default value. */
	public static final String USER_DEFAULT_VALUE = "admin";

	/** The password property. */
	public static final String PASSWORD_PROPERTY = "com.buschmais.maexo.adaptor.mx4j.http.password";

	/** The password default value. */
	public static final String PASSWORD_DEFAULT_VALUE = "maexo";

	/** The stylesheet property. */
	public static final String STYLESHEET_PROPERTY = "com.buschmais.maexo.adaptor.mx4j.http.stylesheet";

	/** The styleshet default value. */
	public static final String STYLESHEET_DEFAULT_VALUE = "/mx4j/tools/adaptor/http/xsl";

	/** The start property. */
	public static final String START_PROPERTY = "com.buschmais.maexo.adaptor.mx4j.http.start";

	/** The start default value. */
	public static final String START_DEFAULT_VALUE = "true";

}
