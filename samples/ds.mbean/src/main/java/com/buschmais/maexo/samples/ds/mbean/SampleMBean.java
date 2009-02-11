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
package com.buschmais.maexo.samples.ds.mbean;

/**
 * Represents a standard MBean interface for demonstration purposes. It contains
 * the definition of an attribute and an operation.
 */
public interface SampleMBean {

	/**
	 * Returns the value of the attribute.
	 * 
	 * @return The attribute value.
	 */
	public String getAttribute();

	/**
	 * Sets the value of the attribute.
	 * 
	 * @param attribute
	 *            The new value of the attribute.
	 */
	public void setAttribute(String attribute);

	/**
	 * Represents the operation. In this example it returns the sum of the two
	 * provided parameters.
	 * 
	 * @param a
	 *            The first parameter of the operation.
	 * @param b
	 *            The second parameter of the operation.
	 * @return The sum of <code>a</code> and <code>b</code>.
	 */
	public int operation(int a, int b);

}
