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

/**
 * Represents an address which belongs to a person.
 */
public class Address {

	/**
	 * The id.
	 */
	private int id;

	/**
	 * The person.
	 */
	private Person person;

	/**
	 * Returns the id.
	 *
	 * @return The id.
	 */
	public final  int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            The id to set.
	 */
	public final void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the person.
	 *
	 * @return The person.
	 */
	public final Person getPerson() {
		return person;
	}

	/**
	 * Sets the person.
	 *
	 * @param person
	 *            The person to set.
	 */
	public final void setPerson(Person person) {
		this.person = person;
	}

}
