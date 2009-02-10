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
package com.buschmais.maexo.samples.bundle.spring.notification;

import javax.management.Notification;
import javax.management.NotificationFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sample implementation of a {@link NotificationFilter}
 */
public class SampleNotificationFilter implements NotificationFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1708333410465497442L;

	/**
	 * The logger instance.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(SampleNotificationListener.class);

	/**
	 * {@inheritDoc}
	 */
	public boolean isNotificationEnabled(Notification notification) {
		logger.info("The notification filter received a notification: \"{}\"",
				notification.toString());
		return true;
	}

}
