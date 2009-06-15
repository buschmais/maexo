package com.buschmais.maexo.test.mbeans.osgi.compendium;

import java.util.Dictionary;
import java.util.concurrent.BlockingQueue;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

/**
 * Implementation of a {@link ManagedService}.
 */
public class ManagedServiceImpl implements ManagedService {
	private BlockingQueue<ConfigurationEvent> queue;

	/**
	 * Constructor.
	 *
	 * @param queue
	 *            The queue to use for storing received dictionaries.
	 */
	public ManagedServiceImpl(BlockingQueue<ConfigurationEvent> queue) {
		this.queue = queue;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void updated(Dictionary properties) throws ConfigurationException {
		this.queue.offer(new ConfigurationEvent(properties));
	}

}
