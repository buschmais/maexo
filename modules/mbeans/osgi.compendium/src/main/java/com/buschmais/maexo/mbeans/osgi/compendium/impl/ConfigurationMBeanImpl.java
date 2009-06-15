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
package com.buschmais.maexo.mbeans.osgi.compendium.impl;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;

import org.osgi.service.cm.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.maexo.framework.commons.mbean.dynamic.DynamicMBeanSupport;
import com.buschmais.maexo.mbeans.osgi.compendium.ConfigurationMBean;
import com.buschmais.maexo.mbeans.osgi.compendium.ConfigurationMBeanConstants;

/**
 * Implementation of a {@link ConfigurationMBean}.
 */
public final class ConfigurationMBeanImpl extends DynamicMBeanSupport implements
		ConfigurationMBean {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ConfigurationMBeanImpl.class);

	/**
	 * The configuration object to manage.
	 */
	private Configuration configuration;

	/**
	 * The persistent id of the configuration to expose.
	 */
	private String pid;

	/**
	 * The current dictionary.
	 */
	private Dictionary<String, Object> dictionary = null;

	/**
	 * Constructor.
	 *
	 * @param configuration
	 *            The configuration object to manage.
	 * @param pid
	 *            The persistent id of the configuration to expose.
	 */
	public ConfigurationMBeanImpl(Configuration configuration, String pid) {
		this.configuration = configuration;
		this.pid = pid;
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete() {
		logger.debug("deleting configuration for pid '{}'", this.getPid());
		try {
			this.configuration.delete();
		} catch (IOException e) {
			throw new RuntimeException(e.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void discard() {
		this.dictionary = null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean[] getArrayOfBoolean(String name) {
		return (Boolean[]) this.getDictionary().get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Byte[] getArrayOfByte(String name) {
		return (Byte[]) this.getDictionary().get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Character[] getArrayOfCharacter(String name) {
		return (Character[]) this.getDictionary().get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Double[] getArrayOfDouble(String name) {
		return (Double[]) this.getDictionary().get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Float[] getArrayOfFloat(String name) {
		return (Float[]) this.getDictionary().get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer[] getArrayOfInteger(String name) {
		return (Integer[]) this.getDictionary().get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Long[] getArrayOfLong(String name) {
		return (Long[]) this.getDictionary().get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean[] getArrayOfPrimitiveBoolean(String name) {
		Boolean[] result = null;
		boolean[] value = (boolean[]) this.getDictionary().get(name);
		if (value != null) {
			result = new Boolean[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = Boolean.valueOf(value[i]);
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public Byte[] getArrayOfPrimitiveByte(String name) {
		Byte[] result = null;
		byte[] value = (byte[]) this.getDictionary().get(name);
		if (value != null) {
			result = new Byte[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = Byte.valueOf(value[i]);
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public Double[] getArrayOfPrimitiveDouble(String name) {
		Double[] result = null;
		double[] value = (double[]) this.getDictionary().get(name);
		if (value != null) {
			result = new Double[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = Double.valueOf(value[i]);
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public Float[] getArrayOfPrimitiveFloat(String name) {
		Float[] result = null;
		float[] value = (float[]) this.getDictionary().get(name);
		if (value != null) {
			result = new Float[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = Float.valueOf(value[i]);
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer[] getArrayOfPrimitiveInteger(String name) {
		Integer[] result = null;
		int[] value = (int[]) this.getDictionary().get(name);
		if (value != null) {
			result = new Integer[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = Integer.valueOf(value[i]);
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public Long[] getArrayOfPrimitiveLong(String name) {
		Long[] result = null;
		long[] value = (long[]) this.getDictionary().get(name);
		if (value != null) {
			result = new Long[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = Long.valueOf(value[i]);
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public Short[] getArrayOfPrimitiveShort(String name) {
		Short[] result = null;
		short[] value = (short[]) this.getDictionary().get(name);
		if (value != null) {
			result = new Short[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = Short.valueOf(value[i]);
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public Short[] getArrayOfShort(String name) {
		return (Short[]) this.getDictionary().get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getArrayOfString(String name) {
		return (String[]) this.getDictionary().get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean getBoolean(String name) {
		return (Boolean) this.getDictionary().get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getBundleLocation() {
		return this.configuration.getBundleLocation();
	}

	/**
	 * {@inheritDoc}
	 */
	public Byte getByte(String name) {
		return (Byte) this.getDictionary().get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Character getCharacter(String name) {
		return (Character) this.getDictionary().get(name);
	}

	@SuppressWarnings("unchecked")
	private synchronized Dictionary<String, Object> getDictionary() {
		if (this.dictionary == null) {
			this.dictionary = this.configuration.getProperties();
			if (this.dictionary == null) {
				logger.debug("creating new dictionary for pid '{}'", this
						.getPid());
				this.dictionary = new Hashtable<String, Object>();
			}
		}
		return this.dictionary;
	}

	/**
	 * {@inheritDoc}
	 */
	public Double getDouble(String name) {
		return (Double) this.getDictionary().get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getFactoryPid() {
		return this.configuration.getFactoryPid();
	}

	/**
	 * {@inheritDoc}
	 */
	public Float getFloat(String name) {
		return (Float) this.getDictionary().get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getInteger(String name) {
		return (Integer) this.getDictionary().get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Long getLong(String name) {
		return (Long) this.getDictionary().get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public MBeanInfo getMBeanInfo() {
		String className = ConfigurationAdminMBeanImpl.class.getName();
		// attributes
		OpenMBeanAttributeInfoSupport[] mbeanAttributeInfos = new OpenMBeanAttributeInfoSupport[] {
				ConfigurationMBeanConstants.BUNDLELOCATION,
				ConfigurationMBeanConstants.FACTORYPID,
				ConfigurationMBeanConstants.PID,
				ConfigurationMBeanConstants.PROPERTIES };
		// operations
		OpenMBeanOperationInfoSupport[] mbeanOperationInfos = new OpenMBeanOperationInfoSupport[] {
				ConfigurationMBeanConstants.DELETE,
				ConfigurationMBeanConstants.DISCARD,
				ConfigurationMBeanConstants.REMOVE,
				ConfigurationMBeanConstants.REMOVEALL,
				ConfigurationMBeanConstants.SETBOOLEAN,
				ConfigurationMBeanConstants.GETBOOLEAN,
				ConfigurationMBeanConstants.SETARRAYOFBOOLEAN,
				ConfigurationMBeanConstants.GETARRAYOFBOOLEAN,
				ConfigurationMBeanConstants.SETARRAYOFPRIMITIVEBOOLEAN,
				ConfigurationMBeanConstants.GETARRAYOFPRIMITIVEBOOLEAN,
				ConfigurationMBeanConstants.SETBYTE,
				ConfigurationMBeanConstants.GETBYTE,
				ConfigurationMBeanConstants.SETARRAYOFBYTE,
				ConfigurationMBeanConstants.GETARRAYOFBYTE,
				ConfigurationMBeanConstants.SETARRAYOFPRIMITIVEBYTE,
				ConfigurationMBeanConstants.GETARRAYOFPRIMITIVEBYTE,
				ConfigurationMBeanConstants.SETCHARACTER,
				ConfigurationMBeanConstants.GETCHARACTER,
				ConfigurationMBeanConstants.SETARRAYOFCHARACTER,
				ConfigurationMBeanConstants.GETARRAYOFCHARACTER,
				ConfigurationMBeanConstants.SETPARRAYOFPRIMITIVECHARACTER,
				ConfigurationMBeanConstants.GETPARRAYOFPRIMITIVECHARACTER,
				ConfigurationMBeanConstants.SETDOUBLE,
				ConfigurationMBeanConstants.GETDOUBLE,
				ConfigurationMBeanConstants.SETARRAYOFDOUBLE,
				ConfigurationMBeanConstants.GETARRAYOFDOUBLE,
				ConfigurationMBeanConstants.SETARRAYOFPRIMITIVEDOUBLE,
				ConfigurationMBeanConstants.GETARRAYOFPRIMITIVEDOUBLE,
				ConfigurationMBeanConstants.SETFLOAT,
				ConfigurationMBeanConstants.GETFLOAT,
				ConfigurationMBeanConstants.SETARRAYOFFLOAT,
				ConfigurationMBeanConstants.GETARRAYOFFLOAT,
				ConfigurationMBeanConstants.SETARRAYOFPRIMITIVEFLOAT,
				ConfigurationMBeanConstants.GETARRAYOFPRIMITIVEFLOAT,
				ConfigurationMBeanConstants.SETINTEGER,
				ConfigurationMBeanConstants.GETINTEGER,
				ConfigurationMBeanConstants.SETARRAYOFINTEGER,
				ConfigurationMBeanConstants.GETARRAYOFINTEGER,
				ConfigurationMBeanConstants.SETARRAYOFPRIMITIVEINTEGER,
				ConfigurationMBeanConstants.GETARRAYOFPRIMITIVEINTEGER,
				ConfigurationMBeanConstants.SETLONG,
				ConfigurationMBeanConstants.GETLONG,
				ConfigurationMBeanConstants.SETARRAYOFLONG,
				ConfigurationMBeanConstants.GETARRAYOFLONG,
				ConfigurationMBeanConstants.SETARRAYOFPRIMITIVELONG,
				ConfigurationMBeanConstants.GETARRAYOFPRIMITIVELONG,
				ConfigurationMBeanConstants.SETSHORT,
				ConfigurationMBeanConstants.GETSHORT,
				ConfigurationMBeanConstants.SETARRAYOFSHORT,
				ConfigurationMBeanConstants.GETARRAYOFSHORT,
				ConfigurationMBeanConstants.SETARRAYOFPRIMITIVESHORT,
				ConfigurationMBeanConstants.GETARRAYOFPRIMITIVESHORT,
				ConfigurationMBeanConstants.SETSTRING,
				ConfigurationMBeanConstants.GETSTRING,
				ConfigurationMBeanConstants.SETARRAYOFSTRING,
				ConfigurationMBeanConstants.GETARRAYOFSTRING,
				ConfigurationMBeanConstants.UPDATE };
		// constructors
		OpenMBeanConstructorInfoSupport[] mbeanConstructorInfos = new OpenMBeanConstructorInfoSupport[] {};
		// notifications
		MBeanNotificationInfo[] mbeanNotificationInfos = new MBeanNotificationInfo[] {};
		// MBean info
		OpenMBeanInfoSupport mbeanInfo = new OpenMBeanInfoSupport(className,
				ConfigurationMBeanConstants.MBEAN_DESCRIPTION,
				mbeanAttributeInfos, mbeanConstructorInfos,
				mbeanOperationInfos, mbeanNotificationInfos);
		return mbeanInfo;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPid() {
		return this.configuration.getPid();
	}

	/**
	 * {@inheritDoc}
	 */
	public TabularData getProperties() {
		TabularData properties = new TabularDataSupport(
				ConfigurationMBeanConstants.PROPERTIES_TYPE);
		Dictionary<String, Object> dictionary = this.getDictionary();
		Enumeration<String> keys = dictionary.keys();
		while (keys.hasMoreElements()) {
			String name = keys.nextElement();
			Object value = dictionary.get(name);
			String valueAsString = null;
			String valueType = null;
			if (value != null) {
				if (value.getClass().isArray()) {
					// array values
					valueType = String.format("%s[]", value.getClass()
							.getComponentType());
					List<String> valueList = new LinkedList<String>();
					for (int i = 0; i < Array.getLength(value); i++) {
						Object entry = Array.get(value, i);
						valueList.add(entry != null ? entry.toString() : null);
					}
					valueAsString = valueList.toString();
				} else {
					// non-array values
					valueType = value.getClass().getName();
					valueAsString = value.toString();
				}
			}
			try {
				CompositeDataSupport property = new CompositeDataSupport(
						ConfigurationMBeanConstants.PROPERTY_TYPE,
						ConfigurationMBeanConstants.PROPERTY_ITEMS
								.toArray(new String[0]), new Object[] { name,
								valueType, valueAsString });
				properties.put(property);
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
		return properties;
	}

	/**
	 * {@inheritDoc}
	 */
	public Short getShort(String name) {
		return (Short) this.getDictionary().get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getString(String name) {
		return (String) this.getDictionary().get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public void remove(String name) {
		this.dictionary.remove(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeAll() {
		this.dictionary = new Hashtable<String, Object>();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setArrayOfBoolean(String name, Boolean[] value) {
		this.getDictionary().put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setArrayOfByte(String name, Byte[] value) {
		this.getDictionary().put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setArrayOfCharacter(String name, Character[] value) {
		this.getDictionary().put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setArrayOfDouble(String name, Double[] value) {
		this.getDictionary().put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setArrayOfFloat(String name, Float[] value) {
		this.getDictionary().put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer[] setArrayOfInteger(String name) {
		return (Integer[]) this.getDictionary().get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setArrayOfInteger(String name, Integer[] value) {
		this.getDictionary().put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setArrayOfLong(String name, Long[] value) {
		this.getDictionary().put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setArrayOfPrimitiveBoolean(String name, Boolean[] value) {
		boolean[] result = null;
		if (value != null) {
			result = new boolean[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = value[i].booleanValue();
			}
		}
		this.getDictionary().put(name, result);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setArrayOfPrimitiveByte(String name, Byte[] value) {
		byte[] result = null;
		if (value != null) {
			result = new byte[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = value[i].byteValue();
			}
		}
		this.getDictionary().put(name, result);
	}

	/**
	 * {@inheritDoc}
	 */
	public Character[] setArrayOfPrimitiveCharacter(String name) {
		return (Character[]) this.getDictionary().get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setArrayOfPrimitiveCharacter(String name, Character[] value) {
		char[] result = null;
		if (value != null) {
			result = new char[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = value[i].charValue();
			}
		}
		this.getDictionary().put(name, result);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setArrayOfPrimitiveDouble(String name, Double[] value) {
		double[] result = null;
		if (value != null) {
			result = new double[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = value[i].doubleValue();
			}
		}
		this.getDictionary().put(name, result);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setArrayOfPrimitiveFloat(String name, Float[] value) {
		float[] result = null;
		if (value != null) {
			result = new float[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = value[i].floatValue();
			}
		}
		this.getDictionary().put(name, result);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setArrayOfPrimitiveInteger(String name, Integer[] value) {
		int[] result = null;
		if (value != null) {
			result = new int[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = value[i].intValue();
			}
		}
		this.getDictionary().put(name, result);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setArrayOfPrimitiveLong(String name, Long[] value) {
		long[] result = null;
		if (value != null) {
			result = new long[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = value[i].longValue();
			}
		}
		this.getDictionary().put(name, result);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setArrayOfPrimitiveShort(String name, Short[] value) {
		short[] result = null;
		if (value != null) {
			result = new short[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = value[i].shortValue();
			}
		}
		this.getDictionary().put(name, result);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setArrayOfShort(String name, Short[] value) {
		this.getDictionary().put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setArrayOfString(String name, String[] value) {
		this.getDictionary().put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBoolean(String name, Boolean value) {
		this.getDictionary().put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBundleLocation(String bundleLocation) {
		this.configuration.setBundleLocation(bundleLocation);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setByte(String name, Byte value) {
		this.getDictionary().put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCharacter(String name, Character value) {
		this.getDictionary().put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDouble(String name, Double value) {
		this.getDictionary().put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setFloat(String name, Float value) {
		this.getDictionary().put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setInteger(String name, Integer value) {
		this.getDictionary().put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLong(String name, Long value) {
		this.getDictionary().put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShort(String name, Short value) {
		this.getDictionary().put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setString(String name, String value) {
		this.getDictionary().put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void update() {
		logger.debug(
				"updating configuration for pid '{}' with dictionary '{}'",
				this.pid, this.dictionary);
		try {
			this.configuration.update(this.dictionary);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Character[] getArrayOfPrimitiveCharacter(String name) {
		Character[] result = null;
		char[] value = (char[]) this.getDictionary().get(name);
		if (value != null) {
			result = new Character[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = Character.valueOf(value[i]);
			}
		}
		return result;
	}
}
