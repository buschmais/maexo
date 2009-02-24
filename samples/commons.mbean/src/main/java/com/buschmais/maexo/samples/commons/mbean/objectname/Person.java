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

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a person which owns a list of addresses.
 */
public class Person {

	/**
	 * The first name.
	 */
	private String firstName;

	/**
	 * The last name.
	 */
	private String lastName;

	/**
	 * The list of addresses.
	 */
	private List<Address> addresses = new LinkedList<Address>();

	/**
	 * Returns the first name.
	 *
	 * @return The first name.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name.
	 *
	 * @param firstName
	 *            The first name to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 *Returns the last name.
	 *
	 * @return The last name.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 *
	 * @param lastName
	 *            The last name to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns the addresses.
	 *
	 * @return The addresses.
	 */
	public List<Address> getAdresses() {
		return addresses;
	}

	/**
	 * Sets the list of addresses.
	 *
	 * @param addresses
	 *            The address list to set.
	 */
	public void setAdresses(List<Address> adresses) {
		this.addresses = adresses;
	}
}
