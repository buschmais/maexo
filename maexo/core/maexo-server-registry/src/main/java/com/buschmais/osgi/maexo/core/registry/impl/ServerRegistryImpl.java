package com.buschmais.osgi.maexo.core.registry.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerRegistryImpl {

	private static Logger logger = LoggerFactory.getLogger(ServerRegistryImpl.class);

	private Map<MBeanServer, MBeanServer> mbeanServers = new IdentityHashMap<MBeanServer, MBeanServer>();

	private Map<ObjectName, Object> mbeans = new HashMap<ObjectName, Object>();

	public ServerRegistryImpl() {
		if (logger.isDebugEnabled()) {
			logger.debug("creating mbean registry");
		}
	}

	/**
	 * Registers a mbean on a mbean server
	 * 
	 * @param mbeanServer
	 *            the mbean server
	 * @param objectName
	 *            the name
	 * @param mbean
	 *            the mbean
	 */
	private void registerMBean(MBeanServer mbeanServer, ObjectName objectName,
			Object mbean) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("registering mbean " + objectName + " on server "
						+ mbeanServer);
			}
			mbeanServer.registerMBean(mbean, objectName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("caught exception while registering mbean " + objectName
					+ ", on mbean server " + mbeanServer, e);
		}
	}

	/**
	 * Unregisters a mbean from a mbean server
	 * 
	 * @param mbeanServer
	 *            the mbean server
	 * @param objectName
	 *            the name
	 */
	private void unregisterMBean(MBeanServer mbeanServer, ObjectName objectName) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("unregistering mbean " + objectName + " from server "
						+ mbeanServer);
			}
			mbeanServer.unregisterMBean(objectName);
		} catch (Exception e) {
			logger.warn("caught exception while unregistering mbean " + objectName
					+ ", from mbean server " + mbeanServer, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.osgimaexoxo.core.registry.impl.OSGiMBeanRegistry#getMBeanServers
	 * ()
	 */
	public synchronized Set<MBeanServer> getMBeanServers() {
		return Collections.unmodifiableSet(this.mbeanServers.keySet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmaimaexoexo.core.registry.JmxRegistry#registerMBeanServer
	 * (javax.management.MBeanServer)
	 */
	public synchronized void registerMBeanServer(MBeanServer mbeanServer) {
		if (this.mbeanServers.put(mbeanServer, mbeanServer) == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("registering mbean server " + mbeanServer);
			}
			for (Entry<ObjectName, Object> entry : this.mbeans.entrySet()) {
				this.registerMBean(mbeanServer, entry.getKey(), entry
						.getValue());
			}
		} else {
			logger.warn("mbean server " + mbeanServer + " is already registered");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmamaexoaexo.core.registry.JmxRegistry#unregisterMBeanServer
	 * (javax.management.MBeanServer)
	 */
	public synchronized void unregisterMBeanServer(MBeanServer mbeanServer) {
		if (this.mbeanServers.remove(mbeanServer) != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("unregistering mbean server " + mbeanServer);
			}
			for (ObjectName objectName : this.mbeans.keySet()) {
				this.unregisterMBean(mbeanServer, objectName);
			}
		} else {
			logger.warn("mbean server " + mbeanServer
					+ " is not registered, skipping.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmmaexomaexo.core.registry.JmxRegistry#getMBeans()
	 */
	public Map<ObjectName, Object> getMBeans() {
		return Collections.unmodifiableMap(this.mbeans);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmais.maexo.maexo.core.registry.impl.OSGiMBeanRegistry#registerMBean
	 * (javax.management.ObjectName, java.lang.Object)
	 */
	public synchronized void registerMBean(ObjectName objectName, Object mbean) {
		if (this.mbeans.put(objectName, mbean) == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("registering mbean " + objectName);
			}
			for (MBeanServer mbeanServer : this.mbeanServers.keySet()) {
				this.registerMBean(mbeanServer, objectName, mbean);
			}
		} else {
			logger.warn("mbean " + objectName + " is already registered");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buschmaismaexoi.maexo.core.registry.impl.OSGiMBeanRegistry#unregisterMBean
	 * (javax.management.ObjectName)
	 */
	public synchronized void unregisterMBean(ObjectName objectName) {
		if (this.mbeans.remove(objectName) != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("unregistering mbean " + objectName);
			}
			for (MBeanServer mbeanServer : this.mbeanServers.keySet()) {
				this.unregisterMBean(mbeanServer, objectName);
			}
		} else {
			logger.warn("mbean " + objectName + " is not registered");
		}
	}

}