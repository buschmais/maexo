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
package com.buschmais.maexo.samples.commons.mbean.objectname;

import java.util.Map;

import javax.management.ObjectName;

import com.buschmais.maexo.framework.commons.mbean.objectname.ObjectNameFactory;

/**
 * Implementation of an {@link ObjectNameFactory} which creates object names for
 * instances of the class {@link Person}.
 */
public class PersonObjectNameFactory implements ObjectNameFactory {

	/**
	 * {@inheritDoc}
	 */
	public ObjectName getObjectName(Object resource,
			Map<String, Object> properties) {
		Person person = (Person) resource;
		try {
			return new ObjectName(String.format(
					"com.buschmais.maexo.sample:type=Person,name=%s_%s", person
							.getFirstName(), person.getLastName()));
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

}
