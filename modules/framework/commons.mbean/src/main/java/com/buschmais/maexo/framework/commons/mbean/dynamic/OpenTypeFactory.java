/*
 * Copyright 2008 buschmais GbR
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
package com.buschmais.maexo.framework.commons.mbean.dynamic;

import javax.management.openmbean.ArrayType;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.TabularType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides factory methods to create JMX OpenType types.
 * 
 * @see OpenType
 */
public final class OpenTypeFactory {

	/**
	 * Private Constructor.
	 */
	private OpenTypeFactory() {
	}
	
	private static final Logger logger = LoggerFactory
			.getLogger(OpenTypeFactory.class);

	/**
	 * Creates a JMX composite type.
	 * <p>
	 * This method is optimistic about the correctness of its parameters. No
	 * exception whatsoever will be thrown.
	 * 
	 * @param typeName
	 *            The name given to the composite type this instance represents;
	 *            cannot be a null or empty string.
	 * @param description
	 *            The human readable description of the composite type this
	 *            instance represents; cannot be a null or empty string.
	 * @param itemNames
	 *            The names of the items contained in the composite data values
	 *            described by this <code>CompositeType</code> instance; cannot
	 *            be null and should contain at least one element; no element
	 *            can be a null or empty string. Note that the order in which
	 *            the item names are given is not important to differentiate a
	 *            <code>CompositeType</code> instance from another; the item
	 *            names are internally stored sorted in ascending alphanumeric
	 *            order.
	 * @param itemDescriptions
	 *            The descriptions, in the same order as <var>itemNames</var>,
	 *            of the items contained in the composite data values described
	 *            by this <code>CompositeType</code> instance; should be of the
	 *            same size as <var>itemNames</var>; no element can be a null or
	 *            empty string.
	 * @param itemTypes
	 *            The open type instances, in the same order as
	 *            <var>itemNames</var>, describing the items contained in the
	 *            composite data values described by this
	 *            <code>CompositeType</code> instance; should be of the same
	 *            size as <var>itemNames</var>; no element can be null.
	 * @return a composite type built by
	 *         {@link CompositeType#CompositeType(String, String, String[], String[], OpenType[])}
	 */
	public static CompositeType createCompositeType(String typeName,
			String description, String[] itemNames, String[] itemDescriptions,
			OpenType[] itemTypes) {
		try {
			return new CompositeType(typeName, description, itemNames,
					itemDescriptions, itemTypes);
		} catch (Exception e) {
			logger.error("error creating composite type", e);
		}
		return null;
	}

	/**
	 * Creates a JMX tabular type.
	 * <p>
	 * This method is optimistic about the correctness of its parameters. No
	 * exception whatsoever will be thrown.
	 * 
	 * @param typeName
	 *            The name given to the tabular type this instance represents;
	 *            cannot be a null or empty string.
	 * @param description
	 *            The human readable description of the tabular type this
	 *            instance represents; cannot be a null or empty string.
	 * @param rowType
	 *            The type of the row elements of tabular data values described
	 *            by this tabular type instance; cannot be null.
	 * @param indexNames
	 *            The names of the items the values of which are used to
	 *            uniquely index each row element in the tabular data values
	 *            described by this tabular type instance; cannot be null or
	 *            empty. Each element should be an item name defined in
	 *            <var>rowType</var> (no null or empty string allowed). It is
	 *            important to note that the <b>order</b> of the item names in
	 *            <var>indexNames</var> is used by the methods
	 *            {@link TabularData#get(java.lang.Object[]) <code>get</code>}
	 *            and {@link TabularData#remove(java.lang.Object[])
	 *            <code>remove</code>} of class <code>TabularData</code> to
	 *            match their array of values parameter to items.
	 * @return a tabular type built by
	 *         {@link TabularType#TabularType(String, String, CompositeType, String[])}
	 */
	public static TabularType createTabularType(String typeName,
			String description, CompositeType rowType, String[] indexNames) {
		try {
			return new TabularType(typeName, description, rowType, indexNames);
		} catch (Exception e) {
			logger.error("error creating tabular type", e);
		}
		return null;
	}

	/**
	 * Creates a JMX array type.
	 * <p>
	 * This method is optimistic about the correctness of its parameters. No
	 * exception whatsoever will be thrown.
	 * 
	 * @param dimension
	 *            the dimension of arrays described by this <tt>ArrayType</tt>
	 *            instance; must be greater than or equal to 1.
	 * @param elementType
	 *            the <i>open type</i> of element values contained in the arrays
	 *            described by this <tt>ArrayType</tt> instance; must be an
	 *            instance of either <tt>SimpleType</tt>, <tt>CompositeType</tt>
	 *            or <tt>TabularType</tt>.
	 * @return an array type built by {@link ArrayType#ArrayType(int, OpenType)}
	 */
	public static ArrayType createArrayType(int dimension, OpenType elementType) {
		try {
			return new ArrayType(dimension, elementType);
		} catch (Exception e) {
			logger.error("error creating array type", e);
		}
		return null;
	}

}
