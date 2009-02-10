MAEXO - Management Extensions for OSGi
**************************************
http://code.google.com/p/maexo

Copyright 2008-2009 buschmais GbR

1. Introduction
---------------
The MAEXO project enables JMX based management of OSGi containers and applications. It offers an
easy to use programming model for dealing with JMX resources within an OSGi application.

As a showcase, the framework is used to export basic OSGi services and resources to JMX.

The project aims at portability, minimal dependencies and user-friendliness.

2. Files and Directories
------------------------
- dist/
    Contains the bundles of MAEXO.

		* maexo-framework.switchboard-${maexo.version}.jar
		    If activated this bundle monitors the life cycle of services representing MBean
		    servers, MBean server connections, MBeans and NotificationListeners do provide 
		    transparent registration and unregistration.
		   
		* maexo-framework.commons.mbean-${maexo.version}.jar
		    Provides common classes for other bundles to support MBean implementations in an OSGi 
		    environment.
		      
		* maexo-framework.commons.mbean-${maexo.version}.jar-javadoc
		    Contains the javadoc for the bundle maexo-framework.commons.mbean-${maexo.version}.jar.
		
		* maexo-mbeans.osgi.core-${maexo.version}.jar
		    This bundle registers MBeans to the OSGi service registry which represent resources
		    within the container, e.g. Bundles and Services.
		    
		* maexo-mbeans.osgi.core-${maexo.version}.jar-javadoc
		    Contains the javadoc for the bundle maexo-framework.commons.mbean-${maexo.version}.jar.
		
		* maexo-server.factory-${maexo.version}.jar
		    Registers all MBean servers which are available from the MBeanServerFactory as OSGi 
		    services.
		
		* maexo-server.platform-${maexo.version}.jar
		    Registers the platform MBean server provided by the JRE as OSGi service.
		    
		* maexo-adaptor.mx4j.http-${maexo.version}.jar
		    Activator bundle for the MX4j HTTP adaptor. It may be configured using bundle
		    properties (see provided javadoc).
		  
		* maexo-adaptor.mx4j.http-${maexo.version}.jar-javadoc
		    Contains the javadoc for the bundle maexo-adaptor.mx4j.http-${maexo.version}.jar.
		  
- lib/
	Third party libraries which are required by MAEXO or related bundles.
		* slf4j-api-${slf4j.version}.jar
		    Contains the API of the SLF4J framework which is used for logging by the MAEXO bundles.
		     
		* slf4j-jdk14-${slf4j.version}.jar
		    The JDK14LoggerAdapter of the SLF4j framework.
		    
		* jcl-over-slf4j-${slf4j.version}.jar
		    Provides an SLF4J implementation of the Jakarta Commons Logging (JCL) API. It is only
		    required if the MX4J HTTP adaptor is going to be used.
		    
		* maexo-mx4j.tools.osgi-${mx4j.tools.version}.jar
		    An OSGi-ified version of the MX4J tools (containing the HTTP adaptor).         
         
- doc/api/
    Contains the public API documention of MAEXO as javadoc. For explanations on the programming 
    model see http://code.google.com/p/maexo.
    
- samples/
    * basic
        Demonstrates a basic OSGi container setup for MAEXO using Eclipse Equinox.
    * ds
        Integration of MAEXO with OSGi Declarative Services.
    * spring
        Integration of MAEXO with Spring Dynamic Modules (Spring DM).

3. Links
--------
- MAEXO: http://code.google.com/p/maexo (provides documentation, source code and an issue tracker)
- OSGi Alliance: http://www.osgi.org
- Spring DM: http://www.springframework.org/osgi
- SLF4J: http://www.slf4j.org
- buschmais GbR: http://www.buschmais.com
