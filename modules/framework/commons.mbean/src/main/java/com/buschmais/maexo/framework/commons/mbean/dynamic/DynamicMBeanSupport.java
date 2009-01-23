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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides supporting functionality to facilitate the implementation
 * dynamic MBeans.
 * <p>
 * A dynamic MBean implementation can derive from this class and provide
 * getters/setters and methods like standard MBeans: 
 * 
 * <pre>
 *   public class MyDynamicMBean extends DynamicMBeanSupport { 
 *     private int myAttribute;
 *     public int getMyAttribute() { return this.myAttribute; }
 *     public void setMyAttribute() {this.myAttribute = myAttribute; }
 *     public void myOperation(int parameter) { ... }
 *     public MBeanInfo getMBeanInfo() { ... }
 *   }
 * </pre>
 * 
 * The invocation of these methods is performed via introspection of the MBean's
 * class and using the metadata provided by the method {@link #getMBeanInfo()}
 * which still must be implemented by the MBean itself.
 * <p>
 * Note: The names of the attributes provided by the MBean must start with a
 * lower case letter, e.g. <code>myAttribute</code>.
 */
public abstract class DynamicMBeanSupport implements DynamicMBean,
		MBeanRegistration {

	private static final Logger logger = LoggerFactory
			.getLogger(DynamicMBeanSupport.class);

	private static final String GETTER_PREFIX = "get";
	private static final String SETTER_PREFIX = "set";

	/**
	 * The mbean server where this mbean is registered. It may be used to lookup
	 * other mbeans by their object name.
	 */
	private MBeanServer mbeanServer;

	/**
	 * This map holds the attribute names as keys and the types as values.
	 */
	private final Map<String, Class<?>> attributeTypes;

	/**
	 * Constructor.
	 */
	protected DynamicMBeanSupport() {
		this.attributeTypes = new HashMap<String, Class<?>>();
		if (logger.isDebugEnabled()) {
			logger.debug("constructing dynamic mbean support for "
					+ this.toString());
		}
		for (MBeanAttributeInfo attributeInfo : this.getMBeanInfo()
				.getAttributes()) {
			String name = attributeInfo.getName();
			String type = attributeInfo.getType();
			Class<?> attributeType;
			try {
				// use the class loader of the instantiated (derived) class
				attributeType = Class.forName(type, true, this.getClass()
						.getClassLoader());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("unknown type "
						+ attributeInfo.getType() + " for attribute "
						+ attributeInfo.getName(), e);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("\t" + name + ":" + attributeType.getName());
			}
			this.attributeTypes.put(name, attributeType);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final Object getAttribute(String attribute)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException {
		if (logger.isDebugEnabled()) {
			logger.debug("getting value of attribute "
					+ this.getClass().getName() + ":" + attribute);
		}
		String getterName = GETTER_PREFIX
				+ this.attributeToUpperCase(attribute);
		Method method;
		try {
			method = this.getClass().getDeclaredMethod(getterName,
					new Class<?>[0]);
		} catch (SecurityException e) {
			throw new ReflectionException(e);
		} catch (NoSuchMethodException e) {
			throw new AttributeNotFoundException(attribute);
		}
		try {
			return method.invoke(this, new Object[0]);
		} catch (Exception e) {
			throw new MBeanException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final void setAttribute(Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException {
		String attributeName = attribute.getName();
		Object value = attribute.getValue();
		if (logger.isDebugEnabled()) {
			logger.debug("setting value of attribute "
					+ this.getClass().getName() + ":" + attribute + " to "
					+ value);
		}
		String setterName = SETTER_PREFIX
				+ this.attributeToUpperCase(attributeName);
		Method method;
		try {
			method = this.getClass().getDeclaredMethod(
					setterName,
					new Class<?>[] { this.attributeTypes.get(this
							.attributeToLowerCase(attributeName)) });
		} catch (SecurityException e) {
			throw new ReflectionException(e);
		} catch (NoSuchMethodException e) {
			throw new AttributeNotFoundException(attributeName);
		}
		try {
			method.invoke(this, new Object[] { value });
		} catch (Exception e) {
			throw new MBeanException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public final AttributeList getAttributes(String[] attributes) {
		AttributeList attributeList = new AttributeList();
		for (String attribute : attributes) {
			try {
				attributeList.add(this.getAttribute(attribute));
			} catch (Exception e) {
				logger.warn("cannot get attribute " + attribute, e);
			}
		}
		return attributeList;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public final AttributeList setAttributes(AttributeList attributes) {
		AttributeList attributeList = new AttributeList();
		for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
			Attribute attribute = (Attribute) iterator.next();
			try {
				this.setAttribute(attribute);
				attributeList.add(attribute);
			} catch (Exception e) {
				logger.warn("cannot get attribute " + attribute, e);
			}
		}
		return attributeList;
	}

	/**
	 * {@inheritDoc}
	 */
	public final Object invoke(String actionName, Object[] params,
			String[] signature) throws MBeanException, ReflectionException {
		Method method;
		if (logger.isDebugEnabled()) {
			logger.debug("invoking method " + this.getClass().getName() + ":"
					+ actionName + " using parameters "
					+ Arrays.toString(params));
		}
		try {
			Class<?>[] parameterClasses = new Class<?>[signature.length];
			for (int i = 0; i < signature.length; i++) {
				parameterClasses[i] = Class.forName(signature[i]);
			}
			method = this.getClass().getDeclaredMethod(actionName,
					parameterClasses);
		} catch (Exception e) {
			throw new ReflectionException(e, "cannot introspect class "
					+ this.getClass().getName() + " for method " + actionName);
		}
		try {
			return method.invoke(this, params);
		} catch (Exception e) {
			throw new MBeanException(e, "cannot invoke method " + actionName);
		}
	}

	/**
	 * Converts the first letter of an attribute to upper case (for
	 * getter/setter introspection).
	 * 
	 * @param attribute
	 *            the attribute name
	 * @return the converted attribute name
	 */
	private String attributeToUpperCase(String attribute) {
		return attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
	}

	/**
	 * Converts the first letter of an attribute to lower case (for
	 * getter/setter introspection).
	 * 
	 * @param attribute
	 *            the attribute name
	 * @return the converted attribute name
	 */
	private String attributeToLowerCase(String attribute) {
		return attribute.substring(0, 1).toLowerCase() + attribute.substring(1);
	}

	/**
	 * {@inheritDoc}
	 */
	public void postDeregister() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void postRegister(Boolean registrationDone) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void preDeregister() throws Exception {
	}

	/**
	 * {@inheritDoc}
	 */
	public final ObjectName preRegister(MBeanServer server, ObjectName name)
			throws Exception {
		this.mbeanServer = server;
		return name;
	}

	/**
	 * @return the mbeanServer
	 */
	protected final MBeanServer getMbeanServer() {
		return mbeanServer;
	}

	/**
	 * Gets the value of a specific attribute of a named MBean. The MBean is
	 * identified by its object name.
	 * 
	 * @param objectName
	 *            The object name of the MBean from which the attribute is to be
	 *            retrieved.
	 * @param attribute
	 *            A String specifying the name of the attribute to be retrieved.
	 * 
	 * @return The value of the retrieved attribute.
	 */
	protected final Object getAttribute(ObjectName objectName, String attribute) {
		try {
			return getMbeanServer().getAttribute(objectName, attribute);
		} catch (MBeanException e) {
			throw new RuntimeException(e.getTargetException().toString());
		} catch (JMException e) {
			throw new IllegalArgumentException(String.format(
					"cannot get attribute %s from mbean %s", attribute,
					objectName), e);
		}
	}
}
