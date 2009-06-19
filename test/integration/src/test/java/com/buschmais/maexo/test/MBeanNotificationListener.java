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
package com.buschmais.maexo.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.management.Notification;
import javax.management.NotificationListener;

/**
 * Implementation of a {@link NotificationListener} which records notifications.
 */
public class MBeanNotificationListener<E extends Notification> implements
		NotificationListener {

	/**
	 * Represents a received {@link Notification}.
	 */
	public static class NotificationEvent<E extends Notification> {
		private E notification;
		private Object handle;

		/**
		 * The constructor.
		 *
		 * @param notification
		 *            The notification.
		 * @param handle
		 *            The handle.
		 */
		public NotificationEvent(E notification, Object handle) {
			this.notification = notification;
			this.handle = handle;
		}

		/**
		 * Returns the notification.
		 *
		 * @return The notification.
		 */
		public E getNotification() {
			return (E) notification;
		}

		/**
		 * Returns the handle.
		 *
		 * @return The handle.
		 */
		public Object getHandle() {
			return handle;
		}

	}

	/**
	 * The queue which stores the received notifications.
	 */
	private BlockingQueue<NotificationEvent<E>> notificationEvents = new LinkedBlockingQueue<NotificationEvent<E>>();

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void handleNotification(Notification notification, Object handle) {
		E e = (E) notification;
		this.notificationEvents.offer(new NotificationEvent<E>(e, handle));

	}

	/**
	 * Returns the queue which holds the received notifications.
	 *
	 * @return The notificationEvents.
	 */
	public BlockingQueue<NotificationEvent<E>> getNotificationEvents() {
		return notificationEvents;
	}

}
