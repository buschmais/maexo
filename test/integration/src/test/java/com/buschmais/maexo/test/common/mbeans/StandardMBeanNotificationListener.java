/*
 * Copyright 2008 buschmais GbR
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
package com.buschmais.maexo.test.common.mbeans;

import javax.management.Notification;
import javax.management.NotificationListener;

public class StandardMBeanNotificationListener implements NotificationListener,
		NotificationValidator {

	private Notification lastNotification;

	private Object lastHandback;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.management.NotificationListener#handleNotification(javax.management
	 * .Notification, java.lang.Object)
	 */
	public void handleNotification(Notification notification, Object handback) {
		this.lastNotification = notification;
		this.lastHandback = handback;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.maexo.test.common.mbeans.NotificationValidator#
	 * getLastNotification()
	 */
	public Notification getLastNotification() {
		return lastNotification;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buschmais.maexo.test.common.mbeans.NotificationValidator#
	 * getLastHandback()
	 */
	public Object getLastHandback() {
		return lastHandback;
	}

}
