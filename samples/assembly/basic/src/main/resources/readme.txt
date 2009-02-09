MAEXO - Basic sample
********************

This sample demonstrates how to setup a configuration to use MAEXO within an OSGi environment. The
following bundles are installed (see configuration/config.ini):

  * Switch Board - Life cycle monitoring for MBeans, notification listeners, MBean servers and MBean
    server connections for transparent export of resources to the JMX infrastructure.
    
	  maexo-framework.switchboard-${maexo.version}.jar [activated]
	
  * MBeans for the OSGi core framework - Registers MBeans representing core OSGi resources (e.g.
    Bundles) as OSGi Services
    
      maexo-mbeans.osgi.core-${maexo.version}.jar [activated]
      maexo-framework.commons.mbean-${maexo.version}.jar

  * Exporter Bundle for the Java platform MBean server - Looks up the platform MBean server of
    the Java Runtime Environment and exports it to the OSGi service registry. 
    
      maexo-server.platform-${maexo.version}.jar [activated]

  * MX4J HTTP Adaptor - Offers a HTTP based management console.
   
      maexo-adaptor.mx4j.http-${maexo.version}.jar [activated]
      maexo-mx4j.tools.osgi-${mx4j.tools.version}.jar
	
  * SLF4J logging framework (including JDK 1.4 adapter and Java Commons Logging API)
  
      slf4j-api-${slf4j.version}.jar
      slf4j-jdk14-${slf4j.version}.jar
      jcl-over-slf4j-${slf4j.version}.jar

The container may be started using equinox.cmd or equinox.sh. Now a management console can be
connected:

  * JDK 6.0 comes with VisualVM which can be found in the directory bin/ of the local Java
    installation (e.g. C:\Program Files\Java\Jdk1.5.0_17\bin). Alternatively it is available from
    the web site http://visualvm.dev.java.net.
    It offers a wide range of functionality to monitor, manage and profile running Java
    applications and allows installation of a plugin called VisualVM-MBeans (see Tools->Plugins).
    If the sample is running it will be listed in the applications view (left side) as 
    org.eclipse.osgi-${equinox.version}.jar. Double clicking the entry opens a tab where the MBeans
    view can be selected.
  * JDK 5.0 and JDK 6.0 provide a generic management console called JConsole. It can found in the
    directory bin/ of your Java installation and offers the same functionality as the VisualVM
    MBeans plugin. 
  * The MX4J HTTP adapator contained in the container setup provides a simple web interface which
    may be accessed from the url http://127.0.0.1:8081.
  * Another console using an RMI connection to service:jmx:rmi:///jndi/rmi://localhost:${jmx.remote.port}/jmxrmi
    (no SSL support, no authentication).
