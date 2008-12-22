package com.buschmais.osgi.maexo.mbeans.osgi.core;

import java.util.Iterator;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.DynamicMBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides implementation of common needed funtionality for dynamic
 * mbeans.
 */
public abstract class DynamicMBeanSupport implements DynamicMBean {

	private static final Logger logger = LoggerFactory
			.getLogger(DynamicMBeanSupport.class);

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
		for (Iterator<Attribute> iterator = attributes.iterator(); iterator
				.hasNext();) {
			Attribute attribute = iterator.next();
			try {
				this.setAttribute(attribute);
				attributeList.add(attribute);
			} catch (Exception e) {
				logger.warn("cannot get attribute " + attribute, e);
			}
		}
		return attributeList;
	}

}
