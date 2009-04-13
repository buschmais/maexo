MAEXO - Declarative Services sample
***********************************

This sample demonstrates how to integrate MAEXO with Declarative Services. The bundles in this
sample create instances of MBeans and notification listeners which are exported using declarative
services to the OSGi service registry (see the descriptors in the OSGI-INF/-directories). The life
cycle of these services is monitored by the MAEXO switch board which transparently performs
registration and unregistration of the MBeans and notification listeners on the MBean server.
The following bundles are installed (see configuration/config.ini and conf/config.properties):

  * Declarative Services MBean - The bundle exports an instance of a standard MBean using Declarative
    Services.
	
	  maexo-samples.ds.mbean-${com.buschmais.maexo.version}.jar [activated]

  * Declarative Services Notification - The bundle exports an instance of a notification listener
    which is registered to the MBeanServerDelegate (JMImplementation:type=MBeanServerDelegate). It
    listens on notifications regarding the life cycle of MBeans and simply logs information messages.
	
	  maexo-samples.ds.notification-${com.buschmais.maexo.version}.jar [activated]

  * Switch Board - Life cycle monitoring for MBeans, notification listeners, MBean servers and MBean
    server connections for transparent export of resources to the JMX infrastructure.
    
      maexo-framework.switchboard-${com.buschmais.maexo.version}.jar [activated]
	  
  * Exporter Bundle for the Java platform MBean server - Looks up the platform MBean server of
    the Java Runtime Environment and exports it to the OSGi service registry. 
    
      maexo-server.platform-${com.buschmais.maexo.version}.jar [activated]
  
  * Apache Felix Service Component Runtime (SCR) - The implemenation of the OSGi Declarative
    Services specification from the Apache Felix project (see http://felix.apache.org).
	
	  org.apache.felix.scr-${org.apache.felix.scr.version}.jar [activated]
	
  * SLF4J logging framework (including JDK 1.4 adapter and Java Commons Logging API)
  
      slf4j-api-${org.slf4j.version}.jar
      slf4j-jdk14-${org.slf4j.version}.jar

The container may be started using one of the provided shell scripts for Equinox or Felix.
