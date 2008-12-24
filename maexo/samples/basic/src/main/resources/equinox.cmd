@echo off

rem Copyright 2008 buschmais GbR
rem  
rem Licensed under the Apache License, Version 2.0 (the "License");
rem you may not use this file except in compliance with the License.
rem You may obtain a copy of the License at
rem 
rem      http://www.apache.org/licenses/LICENSE-2.0
rem 
rem Unless required by applicable law or agreed to in writing, software
rem distributed under the License is distributed on an "AS IS" BASIS,
rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
rem implied.
rem See the License for the specific language governing permissions and
rem limitations under the License

echo Starting Equinox, enter "close" to exit
java -Dcom.sun.management.jmxremote -Dlog4j.configuration=file:log4j.properties -jar org.eclipse.osgi-${equinox.version}.jar -console
