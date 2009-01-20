package com.buschmais.maexo.test.common.mbeans;

import javax.management.ObjectName;

import org.osgi.framework.Bundle;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import com.buschmais.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;
import com.buschmais.maexo.test.Constants;
import com.buschmais.maexo.test.MaexoTests;

public class MaexoMBeanTests extends MaexoTests {
	
	/** Symbolic name for the testbundle. */
	private static final String TESTBUNDLE_SYMBOLIC_NAME = "maexo-test.testbundle";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String[] getTestFrameworkBundlesNames() {
		return Constants.TEST_FRAMEWORK_BUNDLES_NAMES;
	}

	/**
	 * Returns a TestBundle from OSGI container for testing of general Bundle
	 * functionality.
	 * 
	 * @return the bundle
	 * @throws InvalidSyntaxException
	 *             if no PackageAdmin could be found
	 */
	protected Bundle getTestBundle() throws InvalidSyntaxException {
		ServiceReference serviceReference = this.bundleContext
				.getServiceReference(org.osgi.service.packageadmin.PackageAdmin.class
						.getName());
		final org.osgi.service.packageadmin.PackageAdmin packageAdmin = (org.osgi.service.packageadmin.PackageAdmin) bundleContext
				.getService(serviceReference);
		Bundle[] bundles = packageAdmin.getBundles(TESTBUNDLE_SYMBOLIC_NAME,
				"0.0.0");
		assertTrue(bundles.length == 1);
		Bundle bundle = bundles[0];
		return bundle;
	}

	/**
	 * Returns the object name for the given Object.
	 * 
	 * @return the object name
	 */
	protected ObjectName getObjectName(Object o, Class<?> c) {
		ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(
				this.bundleContext);
		return objectNameFactoryHelper.getObjectName(o, c);
	}
}
