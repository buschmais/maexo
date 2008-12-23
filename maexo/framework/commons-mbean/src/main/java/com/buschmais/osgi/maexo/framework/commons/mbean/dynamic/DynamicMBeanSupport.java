package com.buschmais.osgi.maexo.framework.commons.mbean.dynamic;

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
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides implementation of common needed functionality for dynamic
 * mbeans. The invocation of methods and getting/setting attributes is performed
 * via introspection of the mbeans class and using the metadata provided by the
 * mbean itself.
 */
public abstract class DynamicMBeanSupport implements DynamicMBean {

	private static final Logger logger = LoggerFactory
			.getLogger(DynamicMBeanSupport.class);

	private static final String GETTER_PREFIX = "get";
	private static final String SETTER_PREFIX = "set";

	/**
	 * This map holds the attribute names as keys and the types as values
	 */
	private Map<String, Class<?>> attributeTypes;

	/**
	 * Constructor
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.DynamicMBean#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String attribute)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.management.DynamicMBean#setAttribute(javax.management.Attribute)
	 */
	public void setAttribute(Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException {
		String attributeName = attribute.getName();
		Object value = attribute.getValue();
		if (logger.isDebugEnabled()) {
			logger.debug("setting value of attribute "
					+ this.getClass().getName() + ":" + attribute + " to "
					+ value);
		}
		String getterName = SETTER_PREFIX
				+ this.attributeToUpperCase(attributeName);
		Method method;
		try {
			method = this.getClass().getDeclaredMethod(getterName,
					new Class<?>[] { this.attributeTypes.get(attributeName) });
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.DynamicMBean#getAttributes(java.lang.String[])
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.management.DynamicMBean#setAttributes(javax.management.AttributeList
	 * )
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.DynamicMBean#invoke(java.lang.String,
	 * java.lang.Object[], java.lang.String[])
	 */
	public Object invoke(String actionName, Object[] params, String[] signature)
			throws MBeanException, ReflectionException {
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
	 * getter/setter introspection)
	 * 
	 * @param attribute
	 *            the attribute name
	 * @return the converted attribute name
	 */
	private String attributeToUpperCase(String attribute) {
		return attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
	}

}
