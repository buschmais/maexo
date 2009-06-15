package com.buschmais.maexo.test.mbeans.osgi.compendium;

import java.util.Dictionary;

import org.osgi.service.cm.ManagedService;
import org.osgi.service.cm.ManagedServiceFactory;

/**
 * Represents configuration event received by a {@link ManagedService} of
 * {@link ManagedServiceFactory}.
 *
 */
public class ConfigurationEvent {

	private String name;

	private Dictionary<String, Object> dictionary;

	/**
	 * The constructor.
	 *
	 * @param dictionary
	 *            The dictionary.
	 *
	 */
	public ConfigurationEvent(Dictionary<String, Object> dictionary) {
		this(null, dictionary);
	}

	/**
	 * The constructor.
	 *
	 * @param name
	 *            The name (PID).
	 * @param dictionary
	 *            The dictionary.
	 */
	public ConfigurationEvent(String name, Dictionary<String, Object> dictionary) {
		this.name = name;
		this.dictionary = dictionary;
	}

	/**
	 * @return the name
	 */
	protected String getName() {
		return name;
	}

	/**
	 * @return the dictionary
	 */
	public Dictionary<String, Object> getDictionary() {
		return dictionary;
	}

}
