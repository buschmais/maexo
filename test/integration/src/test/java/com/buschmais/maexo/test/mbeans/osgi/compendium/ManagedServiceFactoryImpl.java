package com.buschmais.maexo.test.mbeans.osgi.compendium;

import java.util.Dictionary;
import java.util.concurrent.BlockingQueue;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;

/**
 * Implementation of a {@link ManagedServiceFactory}.
 */
public class ManagedServiceFactoryImpl implements ManagedServiceFactory {

	/**
	 * This map is used to store received configurationEvents.
	 */
	private BlockingQueue<ConfigurationEvent> queue;

	/**
	 * Constructor.
	 *
	 * @param dictionaries
	 *            This map used to store received dictionaries.
	 */
	public ManagedServiceFactoryImpl(BlockingQueue<ConfigurationEvent> queue) {
		this.queue = queue;
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleted(String name) {
		this.queue.offer(new ConfigurationEvent(name, null));
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return "com.buschmais.maexo.test.managedservicefactory";
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void updated(String name, Dictionary properties)
			throws ConfigurationException {
		this.queue.offer(new ConfigurationEvent(name, properties));
	}

}
