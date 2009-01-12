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
package com.buschmais.osgi.maexo.framework.commons.mbean.objectname;

/**
 * Indicates that an object name cannot be constructed by an object name
 * factory.
 */
// FIXME FS: the purpose of this exception is unclear, maybe an
// IllegalStateException is better
public class ObjectNameFactoryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs the exception using a message.
	 * 
	 * @param message
	 *            The detail message.
	 */
	public ObjectNameFactoryException(String message) {
		super(message);
	}

	/**
	 * Constructs the exception using a {@link Throwable}.
	 * 
	 * @param throwable
	 *            The throwable which will be nested in this exception.
	 */
	public ObjectNameFactoryException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructs the exception using a {@link Throwable}.
	 * 
	 * @param message
	 *            The detail message.
	 * @param throwable
	 *            The throwable which will be nested in this exception.
	 */
	public ObjectNameFactoryException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
