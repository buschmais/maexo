/**
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
package com.buschmais.osgi.maexo.adaptor.mx4j_http_adaptor;

public class Constants {

	public static final String HTTP_ADAPTOR_OBJECTNAME = "com.buschmais.osgi.maexo:type=Adaptor,name=Mx4jHttpAdaptor";
	public static final String XSLT_PROCESSOR_OBJECTNAME = "com.buschmais.osgi.maexo:type=Adaptor,name=Mx4jXsltProcessor";

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

}
