/*
 * Copyright 2009 buschmais GbR
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
package com.buschmais.maexo.mbeans.osgi.compendium;

import java.util.Arrays;
import java.util.List;

import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularType;

import com.buschmais.maexo.framework.commons.mbean.dynamic.OpenTypeFactory;

/**
 * Class holding constants for {@link ConfigurationMBean}s.
 */
public final class ConfigurationMBeanConstants {

	/**
	 * Constructor.
	 */
	private ConfigurationMBeanConstants() {
	}

	/** MBean description. */
	public static final String MBEAN_DESCRIPTION = "Configuration MBean";

	/** MBean object name format. */
	public static final String OBJECTNAME_FORMAT = "com.buschmais.maexo:type=Configuration,pid=%s,id=%s";

	/**
	 * Constant: the name of a property.
	 */
	public static final String PROPERTY_ITEM_NAME = "name";

	/**
	 * Constant: the type of a property.
	 */
	public static final String PROPERTY_ITEM_TYPE = "type";

	/**
	 * Constant: the value of a property.
	 */
	public static final String PROPERTY_ITEM_VALUE = "value";

	/**
	 * Defines the elements of a property.
	 */
	public static final List<String> PROPERTY_ITEMS = Arrays
			.asList(new String[] { PROPERTY_ITEM_NAME, PROPERTY_ITEM_TYPE,
					PROPERTY_ITEM_VALUE });

	/** Composite type representing one configuration property. */
	public static final CompositeType PROPERTY_TYPE = OpenTypeFactory
			.createCompositeType("property", "configuration properties",
					PROPERTY_ITEMS.toArray(new String[0]), PROPERTY_ITEMS
							.toArray(new String[0]), new OpenType[] {
							SimpleType.STRING, SimpleType.STRING,
							SimpleType.STRING });

	/**
	 * Tabular type containing properties as composite type.
	 */
	public static final TabularType PROPERTIES_TYPE = OpenTypeFactory
			.createTabularType("properties",
					"TabularType representing configuration properties.",
					PROPERTY_TYPE, new String[] { PROPERTY_ITEM_NAME });

	/**
	 * MBean attribute info for
	 * {@link org.osgi.service.cm.Configuration#getBundleLocation()} and
	 * {@link org.osgi.service.cm.Configuration#setBundleLocation(String)}.
	 */
	public static final OpenMBeanAttributeInfoSupport BUNDLELOCATION = new OpenMBeanAttributeInfoSupport(
			"bundleLocation",
			"The location to which this configuration is bound, or null.",
			SimpleType.STRING, true, true, false);

	/**
	 * MBean attribute info for
	 * {@link org.osgi.service.cm.Configuration#getFactoryPid()}.
	 */
	public static final OpenMBeanAttributeInfoSupport FACTORYPID = new OpenMBeanAttributeInfoSupport(
			"factoryPid", "The factory PID or null.", SimpleType.STRING, true,
			false, false);

	/**
	 * MBean attribute info for
	 * {@link org.osgi.service.cm.Configuration#getPid()}.
	 */
	public static final OpenMBeanAttributeInfoSupport PID = new OpenMBeanAttributeInfoSupport(
			"pid", "The PID for this Configuration object.", SimpleType.STRING,
			true, false, false);

	/**
	 * MBean attribute info for
	 * {@link org.osgi.service.cm.Configuration#getProperties()}.
	 */
	public static final OpenMBeanAttributeInfoSupport PROPERTIES = new OpenMBeanAttributeInfoSupport(
			"properties",
			"A private copy of the properties for the caller or null.",
			PROPERTIES_TYPE, true, true, false);

	/**
	 * MBean operation info for operation
	 * {@link org.osgi.service.cm.Configuration#delete()}.
	 */
	public static final OpenMBeanOperationInfoSupport DELETE = new OpenMBeanOperationInfoSupport(
			"delete", "Delete this Configuration object.",
			new OpenMBeanParameterInfoSupport[] {}, SimpleType.VOID,
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation discard.
	 */
	public static final OpenMBeanOperationInfoSupport DISCARD = new OpenMBeanOperationInfoSupport(
			"discard", "Discard changes of this Configuration object.",
			new OpenMBeanParameterInfoSupport[] {}, SimpleType.VOID,
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link org.osgi.service.cm.Configuration#remove()}.
	 */
	public static final OpenMBeanOperationInfoSupport REMOVE = new OpenMBeanOperationInfoSupport(
			"remove",
			"Removes a property from the configuration.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link org.osgi.service.cm.Configuration#removeAll()}.
	 */
	public static final OpenMBeanOperationInfoSupport REMOVEALL = new OpenMBeanOperationInfoSupport(
			"removeAll", "Removes all properties from the configuration.",
			new OpenMBeanParameterInfoSupport[] {}, SimpleType.VOID,
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setBoolean(String, Boolean)}.
	 */
	public static final OpenMBeanOperationInfoSupport SETBOOLEAN = new OpenMBeanOperationInfoSupport(
			"setBoolean",
			"Sets a Boolean property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", SimpleType.BOOLEAN) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getBoolean(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETBOOLEAN = new OpenMBeanOperationInfoSupport(
			"getBoolean",
			"Returns a Boolean property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING), },
			SimpleType.BOOLEAN, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setArrayOfPrimitiveBoolean(String, boolean[])}.
	 */
	public static final OpenMBeanOperationInfoSupport SETARRAYOFPRIMITIVEBOOLEAN = new OpenMBeanOperationInfoSupport(
			"setArrayOfPrimitiveBoolean", "Sets a boolean array property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", OpenTypeFactory
									.createArrayType(1, SimpleType.BOOLEAN)) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getArrayOfPrimitiveBoolean(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETARRAYOFPRIMITIVEBOOLEAN = new OpenMBeanOperationInfoSupport(
			"getArrayOfPrimitiveBoolean",
			"Returns a boolean array property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING), },
			OpenTypeFactory.createArrayType(1, SimpleType.BOOLEAN),
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setArrayOfBoolean(String, Boolean[])}.
	 */
	public static final OpenMBeanOperationInfoSupport SETARRAYOFBOOLEAN = new OpenMBeanOperationInfoSupport(
			"setArrayOfBoolean", "Sets a Boolean array property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", OpenTypeFactory
									.createArrayType(1, SimpleType.BOOLEAN)) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getArrayOfBoolean(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETARRAYOFBOOLEAN = new OpenMBeanOperationInfoSupport(
			"getArrayOfBoolean",
			"Returns a Boolean array property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING), },
			OpenTypeFactory.createArrayType(1, SimpleType.BOOLEAN),
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getByte(String, Byte)}.
	 */
	public static final OpenMBeanOperationInfoSupport SETBYTE = new OpenMBeanOperationInfoSupport(
			"setByte", "Sets a Byte property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", SimpleType.BYTE) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getByte(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETBYTE = new OpenMBeanOperationInfoSupport(
			"getByte",
			"Returns a Byte property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING), },
			SimpleType.BYTE, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setArrayOfByte(String, Byte[])}.
	 */
	public static final OpenMBeanOperationInfoSupport SETARRAYOFPRIMITIVEBYTE = new OpenMBeanOperationInfoSupport(
			"setArrayOfPrimitiveByte", "Sets a byte array property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", OpenTypeFactory
									.createArrayType(1, SimpleType.BYTE)) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getArrayOfByte(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETARRAYOFPRIMITIVEBYTE = new OpenMBeanOperationInfoSupport(
			"getArrayOfPrimitiveByte",
			"Returns a byte array property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			OpenTypeFactory.createArrayType(1, SimpleType.BYTE),
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setArrayOfPrimitiveByte(String, byte[])}.
	 */
	public static final OpenMBeanOperationInfoSupport SETARRAYOFBYTE = new OpenMBeanOperationInfoSupport(
			"setArrayOfByte", "Sets a Byte array property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", OpenTypeFactory
									.createArrayType(1, SimpleType.BYTE)) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getArrayOfPrimitiveByte(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETARRAYOFBYTE = new OpenMBeanOperationInfoSupport(
			"getArrayOfByte",
			"Returns a Byte array property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			OpenTypeFactory.createArrayType(1, SimpleType.BYTE),
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setCharacter(String, Character)}.
	 */
	public static final OpenMBeanOperationInfoSupport SETCHARACTER = new OpenMBeanOperationInfoSupport(
			"setCharacter",
			"Sets a Character property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", SimpleType.CHARACTER) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getCharacter(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETCHARACTER = new OpenMBeanOperationInfoSupport(
			"getCharacter",
			"Returns a Character property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			SimpleType.CHARACTER, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setArrayOfPrimitiveCharacter(String, char[])}.
	 */
	public static final OpenMBeanOperationInfoSupport SETPARRAYOFPRIMITIVECHARACTER = new OpenMBeanOperationInfoSupport(
			"setArrayOfPrimitiveCharacter",
			"Sets a char array property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", OpenTypeFactory
									.createArrayType(1, SimpleType.CHARACTER)) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getArrayOfPrimitiveCharacter(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETPARRAYOFPRIMITIVECHARACTER = new OpenMBeanOperationInfoSupport(
			"getArrayOfPrimitiveCharacter",
			"Returns a char array property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			OpenTypeFactory.createArrayType(1, SimpleType.CHARACTER),
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setArrayOfCharacter(String, Character[])}.
	 */
	public static final OpenMBeanOperationInfoSupport SETARRAYOFCHARACTER = new OpenMBeanOperationInfoSupport(
			"setArrayOfCharacter",
			"Sets a Character array property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", OpenTypeFactory
									.createArrayType(1, SimpleType.CHARACTER)) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getArrayOfCharacter(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETARRAYOFCHARACTER = new OpenMBeanOperationInfoSupport(
			"getArrayOfCharacter",
			"Returns a Character array property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			OpenTypeFactory.createArrayType(1, SimpleType.CHARACTER),
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setDouble(String, Double)}.
	 */
	public static final OpenMBeanOperationInfoSupport SETDOUBLE = new OpenMBeanOperationInfoSupport(
			"setDouble", "Sets a Double property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", SimpleType.DOUBLE) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getDouble(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETDOUBLE = new OpenMBeanOperationInfoSupport(
			"getDouble",
			"Returns a Double property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			SimpleType.DOUBLE, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setArrayOfPrimitiveDouble(String, double[])}.
	 */
	public static final OpenMBeanOperationInfoSupport SETARRAYOFPRIMITIVEDOUBLE = new OpenMBeanOperationInfoSupport(
			"setArrayOfPrimitiveDouble", "Sets a double array property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", OpenTypeFactory
									.createArrayType(1, SimpleType.DOUBLE)) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getArrayOfPrimitiveDouble(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETARRAYOFPRIMITIVEDOUBLE = new OpenMBeanOperationInfoSupport(
			"getArrayOfPrimitiveDouble",
			"Returns a double array property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			OpenTypeFactory.createArrayType(1, SimpleType.DOUBLE),
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setArrayOfDouble(String, Double[])}.
	 */
	public static final OpenMBeanOperationInfoSupport SETARRAYOFDOUBLE = new OpenMBeanOperationInfoSupport(
			"setArrayOfDouble", "Sets a Double array property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", OpenTypeFactory
									.createArrayType(1, SimpleType.DOUBLE)) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getArrayOfDouble(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETARRAYOFDOUBLE = new OpenMBeanOperationInfoSupport(
			"getArrayOfDouble",
			"Returns a Double array property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			OpenTypeFactory.createArrayType(1, SimpleType.DOUBLE),
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setFloat(String, Float)}.
	 */
	public static final OpenMBeanOperationInfoSupport SETFLOAT = new OpenMBeanOperationInfoSupport(
			"setFloat", "Sets a Float property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", SimpleType.FLOAT) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getFloat(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETFLOAT = new OpenMBeanOperationInfoSupport(
			"getFloat",
			"Returns a Float property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			SimpleType.FLOAT, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setArrayOfPrimitiveFloat(String, float[])}.
	 */
	public static final OpenMBeanOperationInfoSupport SETARRAYOFPRIMITIVEFLOAT = new OpenMBeanOperationInfoSupport(
			"setArrayOfPrimitiveFloat", "Sets a float array property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", OpenTypeFactory
									.createArrayType(1, SimpleType.FLOAT)) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getArrayOfPrimitiveFloat(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETARRAYOFPRIMITIVEFLOAT = new OpenMBeanOperationInfoSupport(
			"getArrayOfPrimitiveFloat",
			"Returns a float array property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			OpenTypeFactory.createArrayType(1, SimpleType.FLOAT),
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setArrayOfFloat(String, Float[])}.
	 */
	public static final OpenMBeanOperationInfoSupport SETARRAYOFFLOAT = new OpenMBeanOperationInfoSupport(
			"setArrayOfFloat", "Sets a Float array property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", OpenTypeFactory
									.createArrayType(1, SimpleType.FLOAT)) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getArrayOfFloat(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETARRAYOFFLOAT = new OpenMBeanOperationInfoSupport(
			"getArrayOfFloat",
			"Returns a Float array property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			OpenTypeFactory.createArrayType(1, SimpleType.FLOAT),
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setInteger(String, Integer)}.
	 */
	public static final OpenMBeanOperationInfoSupport SETINTEGER = new OpenMBeanOperationInfoSupport(
			"setInteger",
			"Sets an Integer property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", SimpleType.INTEGER) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getInteger(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETINTEGER = new OpenMBeanOperationInfoSupport(
			"getInteger",
			"Returns an Integer property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			SimpleType.INTEGER, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setArrayOfPrimitiveInteger(String, int[])}.
	 */
	public static final OpenMBeanOperationInfoSupport SETARRAYOFPRIMITIVEINTEGER = new OpenMBeanOperationInfoSupport(
			"setArrayOfPrimitiveInteger", "Sets an int array property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", OpenTypeFactory
									.createArrayType(1, SimpleType.INTEGER)) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getArrayOfPrimitiveInteger(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETARRAYOFPRIMITIVEINTEGER = new OpenMBeanOperationInfoSupport(
			"getArrayOfPrimitiveInteger",
			"Returns an int array property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			OpenTypeFactory.createArrayType(1, SimpleType.INTEGER),
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setArrayOfInteger(String, Integer[])}.
	 */
	public static final OpenMBeanOperationInfoSupport SETARRAYOFINTEGER = new OpenMBeanOperationInfoSupport(
			"setArrayOfInteger", "Sets a Integer array property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", OpenTypeFactory
									.createArrayType(1, SimpleType.INTEGER)) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getArrayOfInteger(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETARRAYOFINTEGER = new OpenMBeanOperationInfoSupport(
			"getArrayOfInteger",
			"Returns a Integer array property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			OpenTypeFactory.createArrayType(1, SimpleType.INTEGER),
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setLong(String, Long)}.
	 */
	public static final OpenMBeanOperationInfoSupport SETLONG = new OpenMBeanOperationInfoSupport(
			"setLong", "Sets a Long property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", SimpleType.LONG) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getLong(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETLONG = new OpenMBeanOperationInfoSupport(
			"getLong",
			"Returns a Long property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			SimpleType.LONG, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setArrayOfPrimitiveLong(String, Long[])}.
	 */
	public static final OpenMBeanOperationInfoSupport SETARRAYOFPRIMITIVELONG = new OpenMBeanOperationInfoSupport(
			"setArrayOfPrimitiveLong", "Sets a long array property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", OpenTypeFactory
									.createArrayType(1, SimpleType.LONG)) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getArrayOfPrimitiveCharacter(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETARRAYOFPRIMITIVELONG = new OpenMBeanOperationInfoSupport(
			"getArrayOfPrimitiveLong",
			"Returns a long array property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			OpenTypeFactory.createArrayType(1, SimpleType.LONG),
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setArrayOfLong(String, Long[])}.
	 */
	public static final OpenMBeanOperationInfoSupport SETARRAYOFLONG = new OpenMBeanOperationInfoSupport(
			"setArrayOfLong", "Sets a Long array property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", OpenTypeFactory
									.createArrayType(1, SimpleType.LONG)) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getArrayOfLong(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETARRAYOFLONG = new OpenMBeanOperationInfoSupport(
			"getArrayOfLong",
			"Returns a Long array property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			OpenTypeFactory.createArrayType(1, SimpleType.LONG),
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setShort(String, Short)}.
	 */
	public static final OpenMBeanOperationInfoSupport SETSHORT = new OpenMBeanOperationInfoSupport(
			"setShort", "Sets a Short property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", SimpleType.SHORT) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getShort(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETSHORT = new OpenMBeanOperationInfoSupport(
			"getShort",
			"Returns a Short property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			SimpleType.SHORT, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setArrayOfPrimitiveShort(String, short[])}.
	 */
	public static final OpenMBeanOperationInfoSupport SETARRAYOFPRIMITIVESHORT = new OpenMBeanOperationInfoSupport(
			"setArrayOfPrimitiveShort", "Sets a short array property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", OpenTypeFactory
									.createArrayType(1, SimpleType.SHORT)) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getArrayOfPrimitiveShort(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETARRAYOFPRIMITIVESHORT = new OpenMBeanOperationInfoSupport(
			"getArrayOfPrimitiveShort",
			"Returns a short array property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			OpenTypeFactory.createArrayType(1, SimpleType.SHORT),
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setArrayOfShort(String, Short[])}.
	 */
	public static final OpenMBeanOperationInfoSupport SETARRAYOFSHORT = new OpenMBeanOperationInfoSupport(
			"setArrayOfShort", "Sets a Short array property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", OpenTypeFactory
									.createArrayType(1, SimpleType.SHORT)) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getArrayOfShort(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETARRAYOFSHORT = new OpenMBeanOperationInfoSupport(
			"getArrayOfShort",
			"Returns a Short array property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			OpenTypeFactory.createArrayType(1, SimpleType.SHORT),
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setString(String, String)}.
	 */
	public static final OpenMBeanOperationInfoSupport SETSTRING = new OpenMBeanOperationInfoSupport(
			"setString", "Sets a String property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", SimpleType.STRING) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getString(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETSTRING = new OpenMBeanOperationInfoSupport(
			"getString",
			"Returns a String property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			SimpleType.STRING, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#setArrayOfString(String, String[])}.
	 */
	public static final OpenMBeanOperationInfoSupport SETARRAYOFSTRING = new OpenMBeanOperationInfoSupport(
			"setArrayOfString", "Sets a string array property.",
			new OpenMBeanParameterInfoSupport[] {
					new OpenMBeanParameterInfoSupport("name",
							"The name of the property.", SimpleType.STRING),
					new OpenMBeanParameterInfoSupport("name",
							"The value of the property.", OpenTypeFactory
									.createArrayType(1, SimpleType.STRING)) },
			SimpleType.VOID, OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link ConfigurationMBean#getArrayOfString(String)}.
	 */
	public static final OpenMBeanOperationInfoSupport GETARRAYOFSTRING = new OpenMBeanOperationInfoSupport(
			"getArrayOfString",
			"Returns a string array property.",
			new OpenMBeanParameterInfoSupport[] { new OpenMBeanParameterInfoSupport(
					"name", "The name of the property.", SimpleType.STRING) },
			OpenTypeFactory.createArrayType(1, SimpleType.STRING),
			OpenMBeanOperationInfoSupport.ACTION);

	/**
	 * MBean operation info for operation
	 * {@link org.osgi.service.cm.Configuration#update()}.
	 */
	public static final OpenMBeanOperationInfoSupport UPDATE = new OpenMBeanOperationInfoSupport(
			"update", "Update the properties of this Configuration object.",
			new OpenMBeanParameterInfoSupport[] {}, SimpleType.VOID,
			OpenMBeanOperationInfoSupport.ACTION);
}
