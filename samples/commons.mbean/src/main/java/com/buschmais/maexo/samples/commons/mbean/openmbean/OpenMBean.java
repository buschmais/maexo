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
package com.buschmais.maexo.samples.commons.mbean.openmbean;

import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenMBeanAttributeInfo;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanConstructorInfo;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfo;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.OpenMBeanParameterInfo;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;

import com.buschmais.maexo.framework.commons.mbean.dynamic.DynamicMBeanSupport;
import com.buschmais.maexo.framework.commons.mbean.dynamic.OpenTypeFactory;

/**
 * This class demonstrates how to implement OpenMBeans (which are a special type
 * of {@link DynamicMBean}s) using the functionality provided by
 * {@link DynamicMBeanSupport}.
 * <p>
 * The implementation provides an operation which will do a calculation on two
 * provided parameters <code>a</code> and <code>b</code> depending on the
 * current value of the attribute <code>operationMode</code>. All information is
 * logged into the attribute <code>operationLog</code> which may be reseted
 * using the operation <code>clear</code>.
 * <p>
 * The MBean only contains an implementation of the method
 * {@link DynamicMBean#getMBeanInfo()} to provide meta information. The other
 * methods required by the interface {@link DynamicMBean} are provided by
 * {@link DynamicMBeanSupport} and work using reflection on the getters/setters
 * and operations defined in this class.
 */
public class OpenMBean extends DynamicMBeanSupport {

	/**
	 * Defines the supported operations.
	 */
	enum OperationMode {
		/**
		 * Add.
		 */
		ADD,
		/**
		 * Sub.
		 */
		SUB,
		/**
		 * Mul.
		 */
		MUL,
		/**
		 * Div.
		 */
		DIV
	};

	/**
	 * The current operation mode.
	 */
	private OperationMode operationMode = OperationMode.ADD;

	/**
	 * The current id. It will be incremented on each executed operation.
	 */
	private int id = 0;

	/**
	 * The composite type which represents one entry in the operation log.
	 */
	private static final CompositeType OPERATIONLOGENTRY_TYPE = OpenTypeFactory
			.createCompositeType(
					"operationLogEntryType",
					"Represents the information about one executed operation.",
					new String[] { "id", "a", "b", "operationMode", "result" },
					new String[] { "Id", "A", "B", "Operation mode", "Result" },
					new OpenType[] { SimpleType.INTEGER, SimpleType.INTEGER,
							SimpleType.INTEGER, SimpleType.STRING,
							SimpleType.INTEGER });

	/**
	 * The tabular type which holds log entries.
	 */
	private static final TabularType OPERATIONLOG_TYPE = OpenTypeFactory
			.createTabularType("operationLogType",
					"Represents a log of operations", OPERATIONLOGENTRY_TYPE,
					new String[] { "id" });

	/**
	 * The operation log.
	 */
	private TabularData operationLog = new TabularDataSupport(OPERATIONLOG_TYPE);

	/**
	 * {@inheritDoc}
	 */
	public final MBeanInfo getMBeanInfo() {
		String className = this.getClass().getName();
		OpenMBeanAttributeInfo[] mbeanAttributes = new OpenMBeanAttributeInfo[] {
				new OpenMBeanAttributeInfoSupport(
						"operationMode",
						"The mode of the operation, supported values are ADD, SUB, MUL and DIV.",
						SimpleType.STRING, true, true, false),
				new OpenMBeanAttributeInfoSupport("operationLog",
						"The operation log.", OPERATIONLOG_TYPE, true, false,
						false) };
		OpenMBeanOperationInfo[] mbeanOperations = new OpenMBeanOperationInfo[] {
				new OpenMBeanOperationInfoSupport(
						"operation",
						"Executes the operation using the mode specified by the attribute operationMode.",
						new OpenMBeanParameterInfo[] {
								new OpenMBeanParameterInfoSupport("a",
										"parameter a", SimpleType.INTEGER),
								new OpenMBeanParameterInfoSupport("b",
										"parameter b", SimpleType.INTEGER) },
						SimpleType.INTEGER,
						OpenMBeanOperationInfoSupport.ACTION_INFO),
				new OpenMBeanOperationInfoSupport("clearOperationLog",
						"Clears the operation log.",
						new OpenMBeanParameterInfo[] {}, SimpleType.VOID,
						OpenMBeanOperationInfoSupport.ACTION) };
		return new OpenMBeanInfoSupport(className, "A sample OpenMBean.",
				mbeanAttributes, new OpenMBeanConstructorInfo[] {},
				mbeanOperations, new MBeanNotificationInfo[] {});
	}

	/**
	 * Returns the operation mode.
	 *
	 * @return The operation mode.
	 */
	public final String getOperationMode() {
		return this.operationMode.toString();
	}

	/**
	 * Sets the operation mode.
	 *
	 * @param operationMode
	 *            The operation mode to set, supported values are
	 *            <ul>
	 *            <li>ADD</li>
	 *            <li>SUB</li>
	 *            <li>MUL</li>
	 *            <li>DIV</li>
	 *            </ul>
	 */
	public final void setOperationMode(String operationMode) {
		this.operationMode = OperationMode.valueOf(operationMode);
	}

	/**
	 * Returns the operation log as tabular data.
	 *
	 * @return The operation log.
	 */
	public final TabularData getOperationLog() {
		return this.operationLog;
	}

	/**
	 * Executes the operation which is defined by the value of the attribute
	 * <code>operationMode</code>.
	 *
	 * @param a
	 *            The first parameter.
	 * @param b
	 *            The second parameter.
	 * @return The result of the operation.
	 */
	public final Integer operation(Integer a, Integer b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException(String.format(
					"The parameters must not be null: a=%s b=%s", a, b));
		}
		int result;
		switch (this.operationMode) {
		case ADD:
			result = a.intValue() + b.intValue();
			break;
		case SUB:
			result = a.intValue() - b.intValue();
			break;
		case MUL:
			result = a.intValue() * b.intValue();
			break;
		case DIV:
			result = a.intValue() / b.intValue();
			break;
		default:
			throw new IllegalStateException(String.format(
					"Unkown operation: %s", this.operationMode));
		}
		// log the operation
		this.id++;
		try {
			CompositeData operationInfo = new CompositeDataSupport(
					OPERATIONLOGENTRY_TYPE, new String[] { "id", "a", "b",
							"operationMode", "result" }, new Object[] {
							Integer.valueOf(this.id), a, b,
							this.operationMode.toString(),
							Integer.valueOf(result) });
			this.operationLog.put(operationInfo);
		} catch (OpenDataException e) {
			throw new IllegalStateException(e);
		}
		return Integer.valueOf(result);
	}

	/**
	 * Clears the operation log.
	 */
	public final void clearOperationLog() {
		this.operationLog.clear();
	}
}
