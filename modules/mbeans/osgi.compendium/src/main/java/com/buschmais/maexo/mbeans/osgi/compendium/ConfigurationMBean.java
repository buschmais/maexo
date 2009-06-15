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

import javax.management.openmbean.TabularData;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 * Management interface for a configuration object provided by a
 * {@link ConfigurationAdmin} service.
 *
 * @see {@link Configuration}
 */
public interface ConfigurationMBean {

	/**
	 * Delete this Configuration object.
	 *
	 * @see {@link Configuration#delete()}
	 */
	void delete();

	/**
	 * Discard changes of this Configuration object.
	 */
	void discard();

	/**
	 * Returns a Boolean array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Boolean[] getArrayOfBoolean(String name);

	/**
	 * Returns a Byte array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Byte[] getArrayOfByte(String name);

	/**
	 * Returns a Character array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Character[] getArrayOfCharacter(String name);

	/**
	 * Returns a Double array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Double[] getArrayOfDouble(String name);

	/**
	 * Returns a Float array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Float[] getArrayOfFloat(String name);

	/**
	 * Returns an Integer array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Integer[] getArrayOfInteger(String name);

	/**
	 * Returns a Long array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Long[] getArrayOfLong(String name);

	/**
	 * Returns a boolean array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */

	public Boolean[] getArrayOfPrimitiveBoolean(String name);

	/**
	 * Returns a byte array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Byte[] getArrayOfPrimitiveByte(String name);

	/**
	 * Returns a char array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Character[] getArrayOfPrimitiveCharacter(String name);

	/**
	 * Returns a double array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Double[] getArrayOfPrimitiveDouble(String name);

	/**
	 * Returns a float array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Float[] getArrayOfPrimitiveFloat(String name);

	/**
	 * Returns an int array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Integer[] getArrayOfPrimitiveInteger(String name);

	/**
	 * Returns a long property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Long[] getArrayOfPrimitiveLong(String name);

	/**
	 * Returns a short array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Short[] getArrayOfPrimitiveShort(String name);

	/**
	 * Returns a Short array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Short[] getArrayOfShort(String name);

	/**
	 * Returns a String array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public String[] getArrayOfString(String name);

	/**
	 * Returns a Boolean property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Boolean getBoolean(String name);

	/**
	 * Get the bundle location.
	 *
	 * @see {@link Configuration#getBundleLocation()}
	 *
	 * @return location to which this configuration is bound, or null.
	 */
	String getBundleLocation();

	/**
	 * Returns a Byte property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Byte getByte(String name);

	/**
	 * Returns a Character property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Character getCharacter(String name);

	/**
	 * Returns a Double property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Double getDouble(String name);

	/**
	 * For a factory configuration return the PID of the corresponding Managed
	 * Service Factory, else return null.
	 *
	 * @see {@link Configuration#getFactoryPid()}
	 *
	 * @return Factory PID or null.
	 */
	String getFactoryPid();

	/**
	 * Returns a Float property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Float getFloat(String name);

	/**
	 * Returns an Integer property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Integer getInteger(String name);

	/**
	 * Returns a Long property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Long getLong(String name);

	/**
	 * Get the PID for this Configuration object.
	 *
	 * @see {@link Configuration#getPid()}
	 *
	 * @return The PID for this Configuration object.
	 */
	String getPid();

	/**
	 * Return the properties and the current values (as string representations)
	 * of this Configuration object.
	 *
	 * @see {@link Configuration#getProperties()}
	 *
	 * @return A private copy of the properties for the caller or null.
	 */
	TabularData getProperties();

	/**
	 * Returns a Short property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Short getShort(String name);

	/**
	 * Returns a String property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public String getString(String name);

	/**
	 * Removes a property from the configuration.
	 *
	 * @param name
	 *            The name of the property.
	 */
	public void remove(String name);

	/**
	 * Removes all properties from the configuration.
	 */
	public void removeAll();

	/**
	 * Sets a Boolean array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setArrayOfBoolean(String name, Boolean[] value);

	/**
	 * Sets a Byte array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setArrayOfByte(String name, Byte[] value);

	/**
	 * Sets a Character array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setArrayOfCharacter(String name, Character[] value);

	/**
	 * Sets a Double array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setArrayOfDouble(String name, Double[] value);

	/**
	 * Sets a Float array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setArrayOfFloat(String name, Float[] value);

	/**
	 * Returns an Integer array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public Integer[] setArrayOfInteger(String name);

	/**
	 * Sets a Long array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setArrayOfLong(String name, Long[] value);

	/**
	 * Sets a boolean array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setArrayOfPrimitiveBoolean(String name, Boolean[] value);

	/**
	 * Sets a byte array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setArrayOfPrimitiveByte(String name, Byte[] value);

	/**
	 * Returns a char array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public Character[] setArrayOfPrimitiveCharacter(String name);

	/**
	 * Sets a char array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setArrayOfPrimitiveCharacter(String name, Character[] value);

	/**
	 * Sets a double array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setArrayOfPrimitiveDouble(String name, Double[] value);

	/**
	 * Sets a float array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setArrayOfPrimitiveFloat(String name, Float[] value);

	/**
	 * Sets an int array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setArrayOfPrimitiveInteger(String name, Integer[] value);

	/**
	 * Sets a long property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setArrayOfPrimitiveLong(String name, Long[] value);

	/**
	 * Sets a short array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setArrayOfPrimitiveShort(String name, Short[] value);

	/**
	 * Sets a Short array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setArrayOfShort(String name, Short[] value);

	/**
	 * Sets a String array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setArrayOfString(String name, String[] value);

	/**
	 * Sets a Boolean property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setBoolean(String name, Boolean value);

	/**
	 * Bind this Configuration object to the specified bundle location.
	 *
	 * @see {@link Configuration#setBundleLocation(String)}
	 *
	 * @param bundleLocation
	 *            A bundle location or null.
	 */
	void setBundleLocation(String bundleLocation);

	/**
	 * Sets a Byte property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setByte(String name, Byte value);

	/**
	 * Sets a Character property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setCharacter(String name, Character value);

	/**
	 * Sets a Double property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setDouble(String name, Double value);

	/**
	 * Sets a Float property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setFloat(String name, Float value);

	/**
	 * Sets an Integer property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setInteger(String name, Integer value);

	/**
	 * Sets a Long property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setLong(String name, Long value);

	/**
	 * Sets a Short property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setShort(String name, Short value);

	/**
	 * Sets a String property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	public void setString(String name, String value);

	/**
	 * Update the properties of this Configuration object.
	 *
	 * @see {@link Configuration#update()}
	 */
	void update();
}
