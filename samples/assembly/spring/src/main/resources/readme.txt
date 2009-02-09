MAEXO - Spring sample
********************

This sample demonstrates how to integrate MAEXO with Spring DM. For information on this framwork
please refer to http://www.springframework.org/osgi.
The following bundles are installed (see configuration/config.ini):

  * Spring MBean - The bundle exports an instance of a standard MBean using Spring DM. Therefore
    it contains an application context descriptor in the directory META-INF/spring which is
	evaluated by the Spring DM extender bundle.
	
	  maexo-bundle.spring.mbean-${maexo.version}.jar [activated]

  * Spring MBean Server - The bundle exports an instance the platform MBean server which is looked
    up using the MBeanServerFactoryBean provided by the Spring Framework. The directory
	META-INF/spring contains the application context descriptor.

	  maexo-bundle.spring.server-${maexo.version}.jar [activated]

  * Switch Board - Life cycle monitoring for MBeans, notification listeners, MBean servers and MBean
    server connections for transparent export of resources to the JMX infrastructure.
    
      maexo-framework.switchboard-${maexo.version}.jar [activated]

  * Spring Framework and its dependencies - The most interesting part is the Spring DM Extender which
    will look for active bundles in the container and evaluate application context descriptors.
      
	  org.springframework.aop-${spring.maven.artifact.version}.jar
      org.springframework.beans-${spring.maven.artifact.version}.jar
      org.springframework.context-${spring.maven.artifact.version}.jar
      org.springframework.core-${spring.maven.artifact.version}.jar
      org.springframework.osgi.core-${spring.osgi.version}.jar
      org.springframework.osgi.extender-${spring.osgi.version}.jar [activated]
      org.springframework.osgi.io-${spring.osgi.version}.jar
      com.springsource.org.aopalliance-${aopalliance.version}.jar 
	  
  * SLF4J logging framework (including JDK 1.4 adapter and Java Commons Logging API)
  
      slf4j-api-${slf4j.version}.jar
      slf4j-jdk14-${slf4j.version}.jar
      jcl-over-slf4j-${slf4j.version}.jar

The container may be started using equinox.cmd or equinox.sh.