package com.buschmais.osgi.maexo.mbeans.osgi.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.ArrayType;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;

import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.osgi.maexo.framework.commons.mbean.objectname.ObjectNameHelper;

/**
 * Represents a registered service (wrapping a service reference)
 */
public class ServiceMBean implements DynamicMBean, Service {

	private static final Logger logger = LoggerFactory
			.getLogger(ServiceMBean.class);

	/**
	 * the set of constants defined by the framework
	 */
	private static Set<String> FRAMEWORK_PROPERTIES = new HashSet<String>(
			Arrays.asList(new String[] { Constants.SERVICE_DESCRIPTION,
					Constants.SERVICE_ID, Constants.OBJECTCLASS,
					Constants.SERVICE_PID, Constants.SERVICE_RANKING,
					Constants.SERVICE_VENDOR }));

	/**
	 * the service reference to manage
	 */
	private ServiceReference serviceReference;

	/**
	 * the object name helper
	 */
	private ObjectNameHelper objectNameHelper;

	/**
	 * the composite type for the service properties
	 */
	private CompositeType propertiesRowType;

	/**
	 * the tabular type for the service properties
	 */
	private TabularType propertiesType;

	/**
	 * Constructs the service mbean
	 * 
	 * @param serviceReference
	 *            the service reference
	 * @param objectNameHelper
	 *            the object name helper
	 */
	public ServiceMBean(ServiceReference serviceReference,
			ObjectNameHelper objectNameHelper) {
		this.serviceReference = serviceReference;
		this.objectNameHelper = objectNameHelper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.DynamicMBean#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String attribute)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException {
		if (ATTRIBUTE_BUNDLE_NAME.equals(attribute)) {
			return this.getBundle();
		} else if (ATTRIBUTE_DESCRIPTION_NAME.equals(attribute)) {
			return this.getDescription();
		} else if (ATTRIBUTE_ID_NAME.equals(attribute)) {
			return this.getId();
		} else if (ATTRIBUTE_OBJECTCLASS_NAME.equals(attribute)) {
			return this.getObjectClass();
		} else if (ATTRIBUTE_PID_NAME.equals(attribute)) {
			return this.getPid();
		} else if (ATTRIBUTE_PROPERTIES_NAME.equals(attribute)) {
			return this.getProperties();
		} else if (ATTRIBUTE_RANKING_NAME.equals(attribute)) {
			return this.getRanking();
		} else if (ATTRIBUTE_USINGBUNDLES_NAME.equals(attribute)) {
			return this.getUsingBundles();
		} else if (ATTRIBUTE_VENDOR_NAME.equals(attribute)) {
			return this.getVendor();
		}
		throw new AttributeNotFoundException("unknown attribute" + attribute);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.DynamicMBean#getAttributes(java.lang.String[])
	 */
	@SuppressWarnings("unchecked")
	public AttributeList getAttributes(String[] attributes) {
		AttributeList attributeList = new AttributeList();
		for (String attribute : attributes) {
			try {
				attributeList.add(this.getAttribute(attribute));
			} catch (Exception e) {
				logger.warn("caught exception while getting attribute "
						+ attribute, e);
			}
		}
		return attributeList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.DynamicMBean#getMBeanInfo()
	 */
	public MBeanInfo getMBeanInfo() {
		try {
			String className = ServiceMBean.class.getName();
			// attributes
			List<OpenMBeanAttributeInfoSupport> attributeList = new ArrayList<OpenMBeanAttributeInfoSupport>();
			// bundle
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					ATTRIBUTE_BUNDLE_NAME, ATTRIBUTE_BUNDLE_DESCRIPTION,
					SimpleType.OBJECTNAME, true, false, false));
			// description
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					ATTRIBUTE_DESCRIPTION_NAME,
					ATTRIBUTE_DESCRIPTION_DESCRIPTION, SimpleType.STRING, true,
					false, false));
			// id
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					ATTRIBUTE_ID_NAME, ATTRIBUTE_ID_DESCRIPTION,
					SimpleType.LONG, true, false, false));
			// object class
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					ATTRIBUTE_OBJECTCLASS_NAME,
					ATTRIBUTE_OBJECTCLASS_DESCRIPTION, new ArrayType(1,
							SimpleType.LONG), true, false, false));
			// pid
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					ATTRIBUTE_PID_NAME, ATTRIBUTE_PID_DESCRIPTION,
					SimpleType.STRING, true, false, false));
			// properties
			this.propertiesRowType = new CompositeType(
					COMPOSITETYPE_PROPERTIES_ENTRY,
					COMPOSITETYPE_PROPERTIES_ENTRY_DESCRIPTION, new String[] {
							COMPOSITETYPE_PROPERTIES_NAME,
							COMPOSITETYPE_PROPERTIES_VALUE }, new String[] {
							COMPOSITETYPE_PROPERTIES_NAME,
							COMPOSITETYPE_PROPERTIES_VALUE }, new OpenType[] {
							SimpleType.STRING, SimpleType.STRING });
			this.propertiesType = new TabularType(TABULARTYPE_PROPERTIES_NAME,
					TABULARTYPE_PROPERTIES_DESCRIPTION, this.propertiesRowType,
					new String[] { COMPOSITETYPE_PROPERTIES_NAME });
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					ATTRIBUTE_PROPERTIES_NAME,
					ATTRIBUTE_PROPERTIES_DESCRIPTION, this.propertiesType,
					true, false, false));
			// ranking
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					ATTRIBUTE_RANKING_NAME, ATTRIBUTE_RANKING_DESCRIPTION,
					SimpleType.INTEGER, true, false, false));
			// using bundles
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					ATTRIBUTE_USINGBUNDLES_NAME,
					ATTRIBUTE_USINGBUNDLES_DESCRIPTION, new ArrayType(1,
							SimpleType.OBJECTNAME), true, false, false));
			// vendor
			attributeList.add(new OpenMBeanAttributeInfoSupport(
					ATTRIBUTE_VENDOR_NAME, ATTRIBUTE_VENDOR_DESCRIPTION,
					SimpleType.STRING, true, false, false));

			OpenMBeanAttributeInfoSupport[] mbeanAttributeInfos = attributeList
					.toArray(new OpenMBeanAttributeInfoSupport[0]);
			OpenMBeanOperationInfoSupport[] mbeanOperationInfos = new OpenMBeanOperationInfoSupport[] {};
			OpenMBeanConstructorInfoSupport[] mbeanConstructorInfos = new OpenMBeanConstructorInfoSupport[] {};
			MBeanNotificationInfo[] mbeanNotificationInfos = new MBeanNotificationInfo[] {};
			OpenMBeanInfoSupport mbeanInfo = new OpenMBeanInfoSupport(
					className, MBEAN_DESCRIPTION, mbeanAttributeInfos,
					mbeanConstructorInfos, mbeanOperationInfos,
					mbeanNotificationInfos);
			return mbeanInfo;
		} catch (OpenDataException e) {
			throw new RuntimeException("cannot construct mbean info", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.DynamicMBean#invoke(java.lang.String,
	 * java.lang.Object[], java.lang.String[])
	 */
	public Object invoke(String actionName, Object[] params, String[] signature)
			throws MBeanException, ReflectionException {
		throw new ReflectionException(new NoSuchMethodException(actionName));
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
		throw new ReflectionException(new IllegalAccessException(attribute
				.getName()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.management.DynamicMBean#setAttributes(javax.management.AttributeList
	 * )
	 */
	public AttributeList setAttributes(AttributeList attributes) {
		return new AttributeList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Service#getBundle()
	 */
	public ObjectName getBundle() {
		return this.objectNameHelper.getObjectName(this.serviceReference
				.getBundle(), Bundle.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Service#getDescription()
	 */
	public String getDescription() {
		return (String) this.serviceReference
				.getProperty(Constants.SERVICE_DESCRIPTION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Service#getId()
	 */
	public Long getId() {
		return (Long) this.serviceReference.getProperty(Constants.SERVICE_ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Service#getObjectClass()
	 */
	public String[] getObjectClass() {
		return (String[]) this.serviceReference
				.getProperty(Constants.OBJECTCLASS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Service#getPid()
	 */
	public String getPid() {
		return (String) this.serviceReference
				.getProperty(Constants.SERVICE_PID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Service#getProperties()
	 */
	public TabularData getProperties() throws MBeanException {
		TabularDataSupport tabularProperties = new TabularDataSupport(
				this.propertiesType);
		String[] keys = this.serviceReference.getPropertyKeys();
		for (String key : keys) {
			if (!FRAMEWORK_PROPERTIES.contains(key)) {
				Object value = this.serviceReference.getProperty(key);
				String stringRepresentation = null;
				if (value != null) {
					stringRepresentation = value.toString();
				}
				try {
					CompositeDataSupport row = new CompositeDataSupport(
							this.propertiesRowType, new String[] {
									COMPOSITETYPE_PROPERTIES_NAME,
									COMPOSITETYPE_PROPERTIES_VALUE },
							new Object[] { key, stringRepresentation });
					tabularProperties.put(row);
				} catch (OpenDataException e) {
					throw new MBeanException(e);
				}
			}
		}
		return tabularProperties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Service#getRanking()
	 */
	public Integer getRanking() {
		return (Integer) this.serviceReference
				.getProperty(Constants.SERVICE_RANKING);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Service#getUsingBundles()
	 */
	public ObjectName[] getUsingBundles() {
		List<ObjectName> objectNames = new LinkedList<ObjectName>();
		Bundle[] usingBundles = this.serviceReference.getUsingBundles();
		if (usingBundles != null) {
			for (Bundle usingBundle : usingBundles) {
				objectNames.add(this.objectNameHelper.getObjectName(
						usingBundle, Bundle.class));
			}
		}
		return objectNames.toArray(new ObjectName[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.osgi.maexo.mbeans.osgi.core.Service#getVendor()
	 */
	public String getVendor() {
		return (String) this.serviceReference
				.getProperty(Constants.SERVICE_VENDOR);
	}
}
