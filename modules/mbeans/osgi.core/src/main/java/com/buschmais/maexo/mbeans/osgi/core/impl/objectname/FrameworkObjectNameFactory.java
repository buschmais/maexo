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
package com.buschmais.maexo.mbeans.osgi.core.impl.objectname;

import java.util.Map;

import javax.management.ObjectName;

import com.buschmais.maexo.framework.commons.mbean.objectname.ObjectNameFactory;
import com.buschmais.maexo.mbeans.osgi.core.FrameworkMBeanConstants;

/**
 * Object name factory implementation for framework.
 */
public final class FrameworkObjectNameFactory implements ObjectNameFactory {

	/**
	 * {@inheritDoc}
	 */
	public ObjectName getObjectName(Object resource,
			Map<String, Object> properties) {

		String objectName = String.format(
				FrameworkMBeanConstants.OBJECTNAME_FORMAT);
		try {
			return new ObjectName(objectName);
		} catch (Exception e) {
			throw new IllegalArgumentException(String.format(
					"Cannot create object name instance for '%s'", objectName),
					e);
		}
	}
}
