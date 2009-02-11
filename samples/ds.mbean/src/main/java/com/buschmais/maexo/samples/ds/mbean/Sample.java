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
package com.buschmais.maexo.samples.ds.mbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation of the Sample MBean interface.
 */
public class Sample implements SampleMBean {

	/**
	 * The logger instance.
	 */
	private static final Logger logger = LoggerFactory.getLogger(Sample.class);

	/**
	 * The constructor.
	 */
	public Sample() {
		logger.info("created sample MBean instance");
	}

	/**
	 * The attribute.
	 */
	private String attribute;

	/**
	 * {@inheritDoc}
	 */
	public String getAttribute() {
		return this.attribute;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	/**
	 * {@inheritDoc}
	 */
	public int operation(int a, int b) {
		return a + b;
	}
}
