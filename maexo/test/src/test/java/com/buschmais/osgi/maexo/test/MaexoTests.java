package com.buschmais.osgi.maexo.test;

import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;

/**
 * @see AbstractConfigurableBundleCreatorTests
 */
public class MaexoTests extends AbstractConfigurableBundleCreatorTests {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.springframework.osgi.test.AbstractDependencyManagerTests#
	 * getTestFrameworkBundlesNames()
	 */
	@Override
	protected String[] getTestFrameworkBundlesNames() {
		return Constants.TEST_FRAMEWORK_BUNDLES_NAMES;
	}

}
