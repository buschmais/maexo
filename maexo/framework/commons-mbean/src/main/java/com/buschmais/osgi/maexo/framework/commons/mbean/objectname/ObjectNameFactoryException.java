package com.buschmais.osgi.maexo.framework.commons.mbean.objectname;

public class ObjectNameFactoryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ObjectNameFactoryException(String message) {
		super(message);
	}

	public ObjectNameFactoryException(Throwable throwable) {
		super(throwable);
	}

	public ObjectNameFactoryException(String message, Throwable throwable) {
		super(message);
	}
}
