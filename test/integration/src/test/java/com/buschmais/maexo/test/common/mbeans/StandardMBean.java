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

public interface StandardMBean {

	/**
	 * Sets the new attribute.
	 * 
	 * @param newAttribute
	 *            The new attribute.
	 */
	public void setAttribute(String newAttribute);
	
	/**
	 * Returns the attribute.
	 * 
	 * @return the attribute.
	 */
	public String getAttribute();

	/**
	 * This is a dummy method for testing.
	 * 
	 * @param value
	 *            A boolean value.
	 * @return Another boolean value.
	 */
	public boolean operation(boolean value);
}
