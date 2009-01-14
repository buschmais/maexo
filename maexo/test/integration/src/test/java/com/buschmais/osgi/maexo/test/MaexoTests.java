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
package com.buschmais.osgi.maexo.test;

import org.osgi.framework.Bundle;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;

/**
 * @see AbstractConfigurableBundleCreatorTests
 */
public class MaexoTests extends AbstractConfigurableBundleCreatorTests {


	/** Symbolic name for the testbundle. */
	private static final String TESTBUNDLE_SYMBOLIC_NAME = "com.buschmais.osgi.maexo.test.testbundle";

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
		Bundle[] bundles = packageAdmin.getBundles(TESTBUNDLE_SYMBOLIC_NAME, "0.0.0");
		assertTrue(bundles.length == 1);
		Bundle bundle = bundles[0];
		return bundle;
	}

}
