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
package com.buschmais.maexo.framework.commons.mbean.objectname;

import java.util.Map;

import javax.management.ObjectName;

/**
 * Declares methods to work with object names.
 */
public interface ObjectNameFactory {

	/**
	 * Returns the object name for the given resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @param properties
	 *            Additional properties which are not available from the
	 *            resource itself but which may be required to construct unique
	 *            object names (e.g. the service id).
	 * @return The object name.
	 */
	ObjectName getObjectName(Object resource,
			Map<String, Object> properties);

}
