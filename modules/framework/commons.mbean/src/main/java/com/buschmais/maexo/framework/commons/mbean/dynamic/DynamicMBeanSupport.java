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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.management.Attribute;
import javax.management.AttributeChangeNotification;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.maexo.framework.commons.mbean.MBeanSupport;

/**
 * This class extends {@link MBeanSupport} and provides supporting functionality
 * to facilitate the implementation of dynamic MBeans.
 * <p>
 * A dynamic MBean implementation may derive from this class and then provide
 * getters/setters and methods like standard MBeans:
 *
 * <pre>
 * public class MyDynamicMBean extends DynamicMBeanSupport {
 * 	private int myAttribute;
 *
 * 	public int getMyAttribute() {
 * 		return this.myAttribute;
 * 	}
 *
 * 	public void setMyAttribute() {
 * 		this.myAttribute = myAttribute;
 * 	}
 *
 * 	public void myOperation(int parameter) { ... }
 *
 * 	public MBeanInfo getMBeanInfo() { ... }
 * }
 * </pre>
 *
 * The invocation of these methods is performed via introspection of the MBean's
 * class and using the meta information provided by the method
 * {@link #getMBeanInfo()} which still must be implemented by the MBean itself.
 * <p>
 * Restrictions:
 * <ul>
 * <li>The names of the attributes provided by the MBean must start with a lower
 * case letter, e.g. <code>myAttribute</code>.</li>
 * <li>Native types and the corresponding wrapper types (e.g. int and
 * java.lang.Integer) are treated as semantically equivalent if used as
 * parameter types for setters and MBean operations. As an example there can no
 * distinction be made between two setter methods
 * <code>public void setMyAttribute(int)</code> and
 * <code>public void setMyAttribute(java.lang.Integer)</code>. Both represent
 * the same attribute (<code>myAttribute</code>) and only one method (without
 * any further rule) will be used if both exist.</li>
 * </ul>
 * <p>
 * This class furthermore provides the method
 * {@link #sendAttributeChangeNotification(MBeanAttributeInfo, Object, Object)}
 * which allows sending an {@link AttributeChangeNotification} based on the meta
 * data from an {@link MBeanAttributeInfo} instance.
 */
public abstract class DynamicMBeanSupport extends MBeanSupport implements
		DynamicMBean {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(DynamicMBeanSupport.class);

	private static final String GETTER_PREFIX = "get";
	private static final String SETTER_PREFIX = "set";

	/**
	 * This map holds the attribute names as keys and the types as values.
	 */
	private final Map<String, String> attributeTypes;

	private Map<MBeanMethodDescriptor, Method> mbeanMethods;

	/**
	 * The sequence number for sending notifications.
	 */
	private AtomicLong notificationSequenceNumber = new AtomicLong(0);

	/**
	 * Constructor.
	 */
	protected DynamicMBeanSupport() {
		logger.debug("constructing dynamic mbean support for {}" , this);
		this.introspectMBeanMethods();
		this.attributeTypes = new HashMap<String, String>();
		for (MBeanAttributeInfo attributeInfo : this.getMBeanInfo()
				.getAttributes()) {
			String name = attributeInfo.getName();
			String type = attributeInfo.getType();
			this.attributeTypes.put(name, type);
		}
		logger.debug("attribute types for {} are {}" , this, attributeTypes);
		
	}

	/**
	 * Introspects the methods provided by the MBean and stores them in a map.
	 * It will be used to lookup getters/setters and MBean operations by their
	 * signatures.
	 */
	private void introspectMBeanMethods() {
		this.mbeanMethods = new HashMap<MBeanMethodDescriptor, Method>();
		for (Method mbeanMethod : this.getClass().getMethods()) {
			List<String> parameterTypes = new LinkedList<String>();
			for (Class<?> parameterType : mbeanMethod.getParameterTypes()) {
				parameterTypes.add(parameterType.getName());
			}
			MBeanMethodDescriptor descriptor = new MBeanMethodDescriptor(
					mbeanMethod.getName(), parameterTypes
							.toArray(new String[0]));
			mbeanMethods.put(descriptor, mbeanMethod);
		}
		this.mbeanMethods = Collections.unmodifiableMap(this.mbeanMethods);
	}

	/**
	 * Looks up a method by its signature: name and parameter types.
	 *
	 * @param name
	 *            The name of the method.
	 * @param parameterTypes
	 *            The parameter types.
	 * @return The method or <code>null</code> if no corresponding method
	 *         exists.
	 */
	private Method findMethod(String name, String[] parameterTypes) {
		MBeanMethodDescriptor descriptor = new MBeanMethodDescriptor(name,
				parameterTypes);
		return this.mbeanMethods.get(descriptor);
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
	public final Object getAttribute(String attribute)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException {
		if (logger.isDebugEnabled()) {
			logger.debug("getting value of attribute "
					+ this.getClass().getName() + ":" + attribute);
		}
		String getterName = GETTER_PREFIX
				+ this.attributeToUpperCase(attribute);
		Method method = this.findMethod(getterName, new String[0]);
		if (method == null) {
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
		Method method = this.findMethod(setterName,
				new String[] { this.attributeTypes.get(this
						.attributeToLowerCase(attributeName)) });
		if (method == null) {
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
		method = this.findMethod(actionName, signature);
		if (method == null) {
			throw new ReflectionException(
					new NoSuchMethodException(
							String
									.format(
											"class %s does not contain the requested method %s(%s)",
											this.getClass().getName(),
											actionName, Arrays
													.toString(signature))));
		}
		try {
			return method.invoke(this, params);
		} catch (Exception e) {
			throw new MBeanException(e, "cannot invoke method " + actionName);
		}
	}

	/**
	 * Sends an {@link AttributeChangeNotification}.
	 *
	 * @param mbeanAttributeInfo
	 *            The {@link MBeanAttributeInfo} of the changed attribute.
	 * @param oldValue
	 *            The old value.
	 * @param newValue
	 *            The new value.
	 */
	protected final void sendAttributeChangeNotification(
			MBeanAttributeInfo mbeanAttributeInfo, Object oldValue,
			Object newValue) {
		this.sendNotification(new AttributeChangeNotification(this,
				this.notificationSequenceNumber.getAndIncrement(), System
						.currentTimeMillis(), mbeanAttributeInfo.getName(),
				mbeanAttributeInfo.getDescription(), mbeanAttributeInfo
						.getType(), oldValue, newValue));
	}

}
