MAEXO - Commons MBean sample
***********************************

This sample demonstrates how to use the API contained in the bundle maexo-framework.commons.mbean.
The following bundles are installed (see configuration/config.ini and conf/config.properties):

  * Commons MBean - The bundle maexo-framework.commons.mbean provides APIs which facilitate the
    implementation of MBeans. A sample is provided which demonstrates the use of these APIs. It
	is separated into the following packages:
      - com.buschmais.maexo.samples.commons.mbean
        Contains the activator for the bundle. It creates instances of the sample classes and
        registers services.
      - com.buschmais.maexo.samples.commons.mbean.lifecycle
	    Demonstrates how to couple the life cycle of an MBean to the life cycle of an OSGi service.
      - com.buschmais.maexo.samples.commons.mbean.objectname
	    Provides MBeans which reference each other using object names which are constructed using
		ObjectNameFactories.
      - com.buschmais.maexo.samples.commons.mbean.openmbean
	    Shows how the implementation of dynamic and open MBeans is supported by the provided APIs.

      maexo-framework.commons.mbean-${com.buschmais.maexo.version}.jar
	  maexo-samples.commons.mbean-${com.buschmais.maexo.version}.jar [activated]

  * Switch Board - Life cycle monitoring for MBeans, notification listeners, MBean servers and MBean
    server connections for transparent export of resources to the JMX infrastructure.

      maexo-framework.switchboard-${com.buschmais.maexo.version}.jar [activated]

  * Exporter Bundle for the Java platform MBean server - Looks up the platform MBean server of
    the Java Runtime Environment and exports it to the OSGi service registry.

      maexo-server.platform-${com.buschmais.maexo.version}.jar [activated]

  * SLF4J logging framework (including JDK 1.4 adapter and Java Commons Logging API)

      slf4j-api-${org.slf4j.version}.jar
      slf4j-jdk14-${org.slf4j.version}.jar

The container may be started using one of the provided shell scripts for Equinox or Felix.
