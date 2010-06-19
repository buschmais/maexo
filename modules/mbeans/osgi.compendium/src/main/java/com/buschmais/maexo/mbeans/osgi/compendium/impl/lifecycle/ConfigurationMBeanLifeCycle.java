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
package com.buschmais.maexo.mbeans.osgi.compendium.impl.lifecycle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.management.ObjectName;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationEvent;
import org.osgi.service.cm.ConfigurationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.maexo.framework.commons.mbean.lifecycle.MBeanLifeCycleSupport;
import com.buschmais.maexo.mbeans.osgi.compendium.ConfigurationMBean;
import com.buschmais.maexo.mbeans.osgi.compendium.impl.ConfigurationMBeanImpl;

/**
 * This class implements a configuration event listener to manage the life cycle
 * of the associated configuration MBeans which are provided by a specific
 * instance of the {@link ConfigurationAdmin} service.
 */
public final class ConfigurationMBeanLifeCycle extends MBeanLifeCycleSupport
		implements ConfigurationListener {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ConfigurationMBeanLifeCycle.class);

	/**
	 * The service reference of the {@link ConfigurationAdmin}.
	 */
	private ServiceReference serviceReference;

	/**
	 * The instance of the {@link ConfigurationAdmin}.
	 */
	private ConfigurationAdmin configurationAdmin;

	/**
	 * The {@link ServiceRegistration} of the {@link ConfigurationListener}.
	 */
	private ServiceRegistration serviceRegistration;

	/**
	 * Constructor.
	 *
	 * @param bundleContext
	 *            The bundle context of the exporting bundle.
	 * @param serviceReference
	 *            The service reference of the {@link ConfigurationAdmin}.
	 * @param configurationAdmin
	 *            The instance of the {@link ConfigurationAdmin}.
	 */
	public ConfigurationMBeanLifeCycle(BundleContext bundleContext,
			ServiceReference serviceReference,
			ConfigurationAdmin configurationAdmin) {
		super(bundleContext);
		this.serviceReference = serviceReference;
		this.configurationAdmin = configurationAdmin;
	}

	/**
	 * Returns the {@link Configuration}s of the {@link ConfigurationAdmin}.
	 *
	 * @return The array of {@link Configuration} objects or <code>null</code>
	 */
	private Configuration[] getConfigurations() {
		Configuration[] configurations = null;
		try {
			configurations = this.configurationAdmin.listConfigurations(null);
		} catch (Exception e) {
			throw new IllegalStateException(String.format(
					"cannot get configurations from configuration admin %s.",
					this.serviceReference.toString()), e);
		}
		return configurations;
	}

	/**
	 * Constructs and returns the {@link ObjectName} for the provided
	 * {@link Configuration} object.
	 *
	 * @param pid
	 *            The persistent id.
	 * @return The {@link ObjectName} instance.
	 */
	private ObjectName getObjectName(String pid) {
		Map<String, Object> properties = new HashMap<String, Object>();
		Configuration configuration = this.getConfiguration(pid);
		properties.put(Constants.SERVICE_ID, this.serviceReference
				.getProperty(Constants.SERVICE_ID));
		properties.put(Constants.SERVICE_PID, pid);
		ObjectName objectName = super.getObjectNameHelper().getObjectName(
				configuration, Configuration.class, properties);
		return objectName;
	}

	/**
	 * Registers all existing configurations as MBeans. Adds configuration
	 * listener.
	 */
	public void start() {
		// register all existing configurations as MBeans
		Configuration[] configurations = this.getConfigurations();
		if (configurations != null) {
			for (Configuration configuration : configurations) {
				this.getConfigurationMBean(configuration, configuration
						.getPid());
			}
		}
		this.serviceRegistration = super.getBundleContext().registerService(
				ConfigurationListener.class.getName(), this, null);
	}

	/**
	 * Removes the configuration listener. Unregisters all registered
	 * configuration MBeans.
	 */
	public void stop() {
		// remove configuration listener
		this.serviceRegistration.unregister();
		// unregister all registered configuration MBeans
		Configuration[] configurations = this.getConfigurations();
		if (configurations != null) {
			for (Configuration configuration : configurations) {
				this.releaseConfigurationMBean(configuration.getPid());
			}
		}
	}

	/**
	 * Returns the {@link Configuration} for the provided persistent id.
	 *
	 * @param pid
	 *            The persistent id.
	 * @return The configuration.
	 */
	private Configuration getConfiguration(String pid) {
		try {
			return this.configurationAdmin.getConfiguration(pid, null);
		} catch (IOException e) {
			throw new IllegalStateException(String.format(
					"cannot get configuration for pid='%s'.", pid), e);
		}
	}

	/**
	 * Returns an {@link ObjectName} instance representing an MBean for a
	 * {@link Configuration}.
	 * <p>
	 * An MBean instance will be created if it does not yet exist for the
	 * provided {@link Configuration}.
	 *
	 * @param configuration
	 *            The configuration which will be represented by this MBean.
	 * @param pid
	 *            The persistent id of the configuration.
	 * @return The {@link ObjectName} instance.
	 */
	public ObjectName getConfigurationMBean(Configuration configuration,
			String pid) {
		ObjectName objectName = this.getObjectName(pid);
		if (!super.isMBeanServiceRegistered(objectName)) {
			ConfigurationMBean configurationMBean = new ConfigurationMBeanImpl(
					configuration, pid);
			super.registerMBeanService(ConfigurationMBean.class, objectName,
					configurationMBean);
		}
		return objectName;
	}

	/**
	 * Releases a {@link ConfigurationMBean}.
	 *
	 * @param pid
	 *            The persistent id.
	 */
	public void releaseConfigurationMBean(String pid) {
		ObjectName objectName = this.getObjectName(pid);
		super.unregisterMBeanService(objectName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void configurationEvent(ConfigurationEvent event) {
		String factoryPid = event.getFactoryPid();
		String pid = event.getPid();
		logger
				.debug(String
						.format(
								"received configuration event: type='%s', factory pid='%s', pid='%s'",
								Integer.valueOf(event.getType()), factoryPid,
								pid));
		if (this.serviceReference.equals(event.getReference())) {
			Configuration configuration;
			try {
				configuration = this.getConfiguration(pid);
				switch (event.getType()) {
				case ConfigurationEvent.CM_UPDATED:
					this.getConfigurationMBean(configuration, pid);
					break;
				case ConfigurationEvent.CM_DELETED:
					this.releaseConfigurationMBean(pid);
					break;
				default:
					logger.debug("unknown configuration event type: {}", event.getType());
				}
			} catch (RuntimeException e) {
				logger.warn("cannot process configuration event", e);
			}
		}
	}
}
