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
	 * @see {@link org.osgi.service.cm.Configuration#delete()}
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
	Boolean[] getArrayOfBoolean(String name);

	/**
	 * Returns a Byte array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Byte[] getArrayOfByte(String name);

	/**
	 * Returns a Character array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Character[] getArrayOfCharacter(String name);

	/**
	 * Returns a Double array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Double[] getArrayOfDouble(String name);

	/**
	 * Returns a Float array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Float[] getArrayOfFloat(String name);

	/**
	 * Returns an Integer array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Integer[] getArrayOfInteger(String name);

	/**
	 * Returns a Long array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Long[] getArrayOfLong(String name);

	/**
	 * Returns a boolean array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */

	Boolean[] getArrayOfPrimitiveBoolean(String name);

	/**
	 * Returns a byte array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Byte[] getArrayOfPrimitiveByte(String name);

	/**
	 * Returns a char array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Character[] getArrayOfPrimitiveCharacter(String name);

	/**
	 * Returns a double array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Double[] getArrayOfPrimitiveDouble(String name);

	/**
	 * Returns a float array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Float[] getArrayOfPrimitiveFloat(String name);

	/**
	 * Returns an int array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Integer[] getArrayOfPrimitiveInteger(String name);

	/**
	 * Returns a long property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Long[] getArrayOfPrimitiveLong(String name);

	/**
	 * Returns a short array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Short[] getArrayOfPrimitiveShort(String name);

	/**
	 * Returns a Short array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Short[] getArrayOfShort(String name);

	/**
	 * Returns a String array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	String[] getArrayOfString(String name);

	/**
	 * Returns a Boolean property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Boolean getBoolean(String name);

	/**
	 * Get the bundle location.
	 *
	 * @see {@link org.osgi.service.cm.Configuration#getBundleLocation()}
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
	Byte getByte(String name);

	/**
	 * Returns a Character property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Character getCharacter(String name);

	/**
	 * Returns a Double property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Double getDouble(String name);

	/**
	 * For a factory configuration return the PID of the corresponding Managed
	 * Service Factory, else return null.
	 *
	 * @see {@link org.osgi.service.cm.Configuration#getFactoryPid()}
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
	Float getFloat(String name);

	/**
	 * Returns an Integer property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Integer getInteger(String name);

	/**
	 * Returns a Long property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Long getLong(String name);

	/**
	 * Get the PID for this Configuration object.
	 *
	 * @see {@link org.osgi.service.cm.Configuration#getPid()}
	 *
	 * @return The PID for this Configuration object.
	 */
	String getPid();

	/**
	 * Return the properties and the current values (as string representations)
	 * of this Configuration object.
	 *
	 * @see {@link org.osgi.service.cm.Configuration#getProperties()}
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
	Short getShort(String name);

	/**
	 * Returns a String property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	String getString(String name);

	/**
	 * Removes a property from the configuration.
	 *
	 * @param name
	 *            The name of the property.
	 */
	void remove(String name);

	/**
	 * Removes all properties from the configuration.
	 */
	void removeAll();

	/**
	 * Sets a Boolean array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setArrayOfBoolean(String name, Boolean[] value);

	/**
	 * Sets a Byte array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setArrayOfByte(String name, Byte[] value);

	/**
	 * Sets a Character array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setArrayOfCharacter(String name, Character[] value);

	/**
	 * Sets a Double array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setArrayOfDouble(String name, Double[] value);

	/**
	 * Sets a Float array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setArrayOfFloat(String name, Float[] value);

	/**
	 * Returns an Integer array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return The value of the property.
	 */
	Integer[] setArrayOfInteger(String name);

	/**
	 * Sets a Long array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setArrayOfLong(String name, Long[] value);

	/**
	 * Sets a boolean array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setArrayOfPrimitiveBoolean(String name, Boolean[] value);

	/**
	 * Sets a byte array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setArrayOfPrimitiveByte(String name, Byte[] value);

	/**
	 * Returns a char array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @return value
	 *            The value of the property.
	 */
	Character[] setArrayOfPrimitiveCharacter(String name);

	/**
	 * Sets a char array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setArrayOfPrimitiveCharacter(String name, Character[] value);

	/**
	 * Sets a double array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setArrayOfPrimitiveDouble(String name, Double[] value);

	/**
	 * Sets a float array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setArrayOfPrimitiveFloat(String name, Float[] value);

	/**
	 * Sets an int array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setArrayOfPrimitiveInteger(String name, Integer[] value);

	/**
	 * Sets a long property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setArrayOfPrimitiveLong(String name, Long[] value);

	/**
	 * Sets a short array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setArrayOfPrimitiveShort(String name, Short[] value);

	/**
	 * Sets a Short array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setArrayOfShort(String name, Short[] value);

	/**
	 * Sets a String array property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setArrayOfString(String name, String[] value);

	/**
	 * Sets a Boolean property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setBoolean(String name, Boolean value);

	/**
	 * Bind this Configuration object to the specified bundle location.
	 *
	 * @see {@link org.osgi.service.cm.Configuration#setBundleLocation(String)}
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
	void setByte(String name, Byte value);

	/**
	 * Sets a Character property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setCharacter(String name, Character value);

	/**
	 * Sets a Double property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setDouble(String name, Double value);

	/**
	 * Sets a Float property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setFloat(String name, Float value);

	/**
	 * Sets an Integer property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setInteger(String name, Integer value);

	/**
	 * Sets a Long property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setLong(String name, Long value);

	/**
	 * Sets a Short property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setShort(String name, Short value);

	/**
	 * Sets a String property.
	 *
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 */
	void setString(String name, String value);

	/**
	 * Update the properties of this Configuration object.
	 *
	 * @see {@link org.osgi.service.cm.Configuration#update()}
	 */
	void update();
}
