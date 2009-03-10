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
package com.buschmais.maexo.samples.spring.notification;

import javax.management.Notification;
import javax.management.NotificationListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sample implementation of a {@link NotificationListener}.
 */
public class SampleNotificationListener implements NotificationListener {

	/**
	 * The logger instance.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(SampleNotificationListener.class);

	/**
	 * The constructor.
	 */
	public SampleNotificationListener() {
		logger.info("created notification listener instance");
	}

	/**
	 * {@inheritDoc}
	 */
	public final void handleNotification(Notification notification, Object handback) {
		logger.info("received notification: \"{}\"", notification.toString());

	}

}
