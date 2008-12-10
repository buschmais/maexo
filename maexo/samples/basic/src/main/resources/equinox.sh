#!/bin/sh
echo Starting Equinox, enter "close" to exit
java -Dlog4j.configuration=file:log4j.properties -jar org.eclipse.osgi-${equinox.version}.jar -console
