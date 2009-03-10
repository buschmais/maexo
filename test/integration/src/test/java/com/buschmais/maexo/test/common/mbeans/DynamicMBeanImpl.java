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
package com.buschmais.maexo.test.common.mbeans;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;

import com.buschmais.maexo.framework.commons.mbean.dynamic.DynamicMBeanSupport;

/**
 * Implementation
 */
public class DynamicMBeanImpl extends DynamicMBeanSupport implements
		DynamicMBean {

	private String stringAttribute;

	private int intAttribute;

	private Integer integerAttribute;

	/**
	 * {@inheritDoc}
	 */
	public MBeanInfo getMBeanInfo() {
		String className = this.getClass().getName();
		MBeanAttributeInfo stringAttributeInfo = new MBeanAttributeInfo(
				"stringAttribute", String.class.getName(), "stringAttribute",
				true, true, false);
		MBeanAttributeInfo intAttributeInfo = new MBeanAttributeInfo(
				"intAttribute", int.class.getName(), "intAttribute", true,
				true, false);
		MBeanAttributeInfo integerAttributeInfo = new MBeanAttributeInfo(
				"integerAttribute", Integer.class.getName(),
				"integerAttribute", true, true, false);
		MBeanOperationInfo operationInfo = new MBeanOperationInfo("operation",
				"An operation.", new MBeanParameterInfo[] {
						new MBeanParameterInfo("stringAttribute", String.class
								.getName(), "stringAttribute"),
						new MBeanParameterInfo("intAttribute", int.class
								.getName(), "intAttribute"),
						new MBeanParameterInfo("integerAttribute",
								Integer.class.getName(), "integerAttribute") },
				null, MBeanOperationInfo.ACTION_INFO);
		return new MBeanInfo(className, "A dynamic MBean.",
				new MBeanAttributeInfo[] { stringAttributeInfo,
						intAttributeInfo, integerAttributeInfo, },
				new MBeanConstructorInfo[] {},
				new MBeanOperationInfo[] { operationInfo },
				new MBeanNotificationInfo[] {});
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.buschmais.maexo.test.common.mbeans.OpenMBean#getStringAttribute()
	 */
	public String getStringAttribute() {
		return stringAttribute;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.buschmais.maexo.test.common.mbeans.OpenMBean#setStringAttribute(java
	 * .lang.String)
	 */
	public void setStringAttribute(String stringAttribute) {
		this.stringAttribute = stringAttribute;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.buschmais.maexo.test.common.mbeans.OpenMBean#getIntAttribute()
	 */
	public int getIntAttribute() {
		return intAttribute;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.buschmais.maexo.test.common.mbeans.OpenMBean#setIntAttribute(int)
	 */
	public void setIntAttribute(int intAttribute) {
		this.intAttribute = intAttribute;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.buschmais.maexo.test.common.mbeans.OpenMBean#getIntegerAttribute()
	 */
	public Integer getIntegerAttribute() {
		return integerAttribute;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.buschmais.maexo.test.common.mbeans.OpenMBean#setIntegerAttribute(
	 * java.lang.Integer)
	 */
	public void setIntegerAttribute(Integer integerAttribute) {
		this.integerAttribute = integerAttribute;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.buschmais.maexo.test.common.mbeans.OpenMBean#operation(java.lang.
	 * String, int, java.lang.Integer)
	 */
	public void operation(String stringAttribute, int intAttribute,
			Integer integerAttribute) {
		this.stringAttribute = stringAttribute;
		this.intAttribute = intAttribute;
		this.integerAttribute = integerAttribute;
	}
}
