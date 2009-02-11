MAEXO - Spring DM sample
************************

This sample demonstrates how to integrate MAEXO with Spring Dynamic Modules (Spring DM). This
framework provides an extender bundle which checks active bundles in the container for the
existence of Spring application context descriptors (default directory is META-INF/spring) and
evaluates them. For more information on the Spring DM framework please refer to the project web
site http://www.springframework.org/osgi.
The bundles in this sample create instances of MBeans, notification listeners and MBean servers
using the Spring DM approach and export them to the OSGi service registry. The life cycle of
these services is monitored by the MAEXO switch board which transparently performs registration
and unregistration of the MBeans and notification listeners on the MBean server.
The following bundles are installed (see configuration/config.ini):

  * Spring MBean - The bundle exports an instance of a standard MBean using Spring DM.
	
	  maexo-samples.spring.mbean-${com.buschmais.maexo.version}.jar [activated]

  * Spring Notification - The bundle exports an instance of a notification listener which is
    registered to the MBeanServerDelegate (JMImplementation:type=MBeanServerDelegate). It listens
    on notifications regarding the life cycle of MBeans and simply logs information messages.
    Furthermore an optional notification filter and handback object are defined in the Spring
    application context descriptor.
	
	  maexo-samples.spring.notification-${com.buschmais.maexo.version}.jar [activated]

  * Spring MBean Server - The bundle exports an instance of the platform MBean server which is
    looked up using the MBeanServerFactoryBean provided by the Spring Framework.

	  maexo-samples.spring.server-${com.buschmais.maexo.version}.jar [activated]

  * Switch Board - Life cycle monitoring for MBeans, notification listeners, MBean servers and MBean
    server connections for transparent export of resources to the JMX infrastructure.
    
      maexo-framework.switchboard-${com.buschmais.maexo.version}.jar [activated]

  * Spring Framework and its dependencies - The most interesting part is the Spring DM Extender which
    will look for active bundles in the container and evaluate application context descriptors.
      
	  org.springframework.aop-${org.springframework.version}.jar
      org.springframework.beans-${org.springframework.version}.jar
      org.springframework.context-${org.springframework.version}.jar
      org.springframework.core-${org.springframework.version}.jar
      org.springframework.osgi.core-${org.springframework.osgi.version}.jar
      org.springframework.osgi.extender-${org.springframework.osgi.version}.jar [activated]
      org.springframework.osgi.io-${org.springframework.osgi.version}.jar
      com.springsource.org.aopalliance-${org.aopalliance.version}.jar 
	  
  * SLF4J logging framework (including JDK 1.4 adapter and Java Commons Logging API)
  
      slf4j-api-${org.slf4j.version}.jar
      slf4j-jdk14-${org.slf4j.version}.jar
      jcl-over-slf4j-${org.slf4j.version}.jar

The container may be started using equinox.cmd or equinox.sh.