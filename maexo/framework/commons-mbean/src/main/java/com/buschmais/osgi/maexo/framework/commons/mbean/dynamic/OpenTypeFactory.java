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
package com.buschmais.osgi.maexo.framework.commons.mbean.dynamic;

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
public class OpenTypeFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(OpenTypeFactory.class);

	/**
	 * Creates a JMX composite type.
	 * <p>
	 * This method is optimistic about the correctness of its parameters. No
	 * exception whatsoever will be thrown.
	 * 
	 * @param typeName
	 * @param description
	 * @param itemNames
	 * @param itemDescriptions
	 * @param itemTypes
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
	 * @param description
	 * @param rowType
	 * @param indexNames
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
	 * @param elementType
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
