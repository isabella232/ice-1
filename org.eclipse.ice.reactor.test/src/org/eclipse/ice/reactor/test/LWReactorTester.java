/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.reactor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.LWRComposite;
import org.eclipse.ice.reactor.LWReactor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Scott Forest Hull II
 */
public class LWReactorTester {
	// An @BeforeClass that sets up the library path. This should be added to
	// the model or removed if it can be fixed for local machine
	@BeforeClass
	public static void beforeClass() {

		// Set the path to the library
		// System.setProperty("java.library.path", "/usr/lib64");
		// System.setProperty("java.library.path", "/home/Scott Forest Hull
		// II/usr/local/lib64");
		// System.setProperty("java.library.path",
		// "/home/ICE/hdf-java/lib/linux");

	}

	/**
	 * <p>
	 * This operation checks the constructor and it's default values.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {
		// Local Declarations
		LWReactor reactor;
		String defaultName = "LWReactor 1";
		String defaultDescription = "LWReactor 1's Description";
		int defaultId = 1;
		int defaultSize = 1;
		HDF5LWRTagType type = HDF5LWRTagType.LWREACTOR;

		// This test is to show the default value for a reactor when it is
		// created with a negative value.
		reactor = new LWReactor(-1);
		assertEquals(defaultSize, reactor.getSize());
		assertEquals(defaultName, reactor.getName());
		assertEquals(defaultDescription, reactor.getDescription());
		assertEquals(defaultId, reactor.getId());
		assertEquals(type, reactor.getHDF5LWRTag());

		// This test is to show the default value for a reactor when its created
		// with a zero value
		reactor = new LWReactor(0);
		assertEquals(defaultSize, reactor.getSize());
		assertEquals(defaultName, reactor.getName());
		assertEquals(defaultDescription, reactor.getDescription());
		assertEquals(defaultId, reactor.getId());
		assertEquals(type, reactor.getHDF5LWRTag());

		// This is a test to show a valid creation of a reactor
		reactor = new LWReactor(17);
		assertEquals(17, reactor.getSize());
		assertEquals(defaultName, reactor.getName());
		assertEquals(defaultDescription, reactor.getDescription());
		assertEquals(defaultId, reactor.getId());
		assertEquals(type, reactor.getHDF5LWRTag());

	}

	/**
	 * <p>
	 * ﻿This operation demonstrates the behaviors listed with the overridden
	 * composite implementations from LWRcomposite.
	 * </p>
	 * 
	 */
	@Test
	public void checkCompositeImplementations() {

		// Local Declarations
		int reactorSize = 17;
		LWReactor reactor;
		ArrayList<String> compNames = new ArrayList<String>();
		ArrayList<Component> components = new ArrayList<Component>();
		int numberOfDefaultComponents = 0;

		// Setup the default number of components
		numberOfDefaultComponents = components.size();

		// Check the default Composite size and attributes on PWRAssembly
		reactor = new LWReactor(reactorSize);

		// Has a size of numberOfDefaultComponents
		assertEquals(numberOfDefaultComponents,
				reactor.getNumberOfComponents());
		// Check general getters for the other pieces
		assertTrue(compNames.equals(reactor.getComponentNames()));
		assertTrue(components.equals(reactor.getComponents()));

		// These operations will show that these will not work for this class

		// Check addComponent
		assertEquals(numberOfDefaultComponents,
				reactor.getNumberOfComponents());
		reactor.addComponent(new LWRComposite());
		// No size change!
		assertEquals(numberOfDefaultComponents,
				reactor.getNumberOfComponents());

		// Check removeComponent - id
		assertEquals(numberOfDefaultComponents,
				reactor.getNumberOfComponents());
		reactor.removeComponent(1);
		// No size change!
		assertEquals(numberOfDefaultComponents,
				reactor.getNumberOfComponents());

		// Check remove component - name
		assertEquals(numberOfDefaultComponents,
				reactor.getNumberOfComponents());

		// No size change!
		assertEquals(numberOfDefaultComponents,
				reactor.getNumberOfComponents());

	}

	/**
	 * <p>
	 * This operation checks equals() and hashCode() operations.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {

		// Local Declarations
		LWReactor object, equalObject, unEqualObject, transitiveObject;
		int size = 5;
		int unEqualSize = 7;

		// Setup root object
		object = new LWReactor(size);

		// Setup equalObject equal to object
		equalObject = new LWReactor(size);

		// Setup transitiveObject equal to object
		transitiveObject = new LWReactor(size);

		// Set its data, not equal to object
		unEqualObject = new LWReactor(unEqualSize);

		// Assert that these two objects are equal
		assertTrue(object.equals(equalObject));

		// Assert that two unequal objects returns false
		assertFalse(object.equals(unEqualObject));

		// Check that equals() is Reflexive
		// x.equals(x) = true
		assertTrue(object.equals(object));

		// Check that equals() is Symmetric
		// x.equals(y) = true iff y.equals(x) = true
		assertTrue(object.equals(equalObject) && equalObject.equals(object));

		// Check that equals() is Transitive
		// x.equals(y) = true, y.equals(z) = true => x.equals(z) = true
		if (object.equals(equalObject)
				&& equalObject.equals(transitiveObject)) {
			assertTrue(object.equals(transitiveObject));
		} else {
			fail();
		}

		// Check the Consistent nature of equals()
		assertTrue(object.equals(equalObject) && object.equals(equalObject)
				&& object.equals(equalObject));
		assertTrue(
				!object.equals(unEqualObject) && !object.equals(unEqualObject)
						&& !object.equals(unEqualObject));

		// Assert checking equality with null value returns false
		assertFalse(object == null);

		// Assert that two equal objects have the same hashcode
		assertTrue(object.equals(equalObject)
				&& object.hashCode() == equalObject.hashCode());

		// Assert that hashcode is consistent
		assertTrue(object.hashCode() == object.hashCode());

		// Assert that hashcodes are different for unequal objects
		assertFalse(object.hashCode() == unEqualObject.hashCode());

	}

	/**
	 * <p>
	 * This operation checks the copy and clone routines.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {

		// Local declarations
		LWReactor object;
		LWReactor copyObject = new LWReactor(0), clonedObject;

		int size = 5;

		// Setup root object
		object = new LWReactor(size);

		// Run the copy routine
		copyObject = new LWReactor(0);
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (LWReactor) object.clone();

		// Check contents
		assertTrue(object.equals(clonedObject));

		// Pass null for the copy routine
		copyObject.copy(null);

		// Show that nothing as changed
		assertTrue(object.equals(copyObject));

	}

	/**
	 * <p>
	 * Removes the test.h5 file after the tests fails (to keep the workspace
	 * clean).
	 * </p>
	 * 
	 */
	@AfterClass
	public static void afterClass() {

		// Cleans up the datafile if it exists due to a failed test
		File dataFile = new File(System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "test.h5");

		// If it exists, remove it
		if (dataFile.exists()) {
			dataFile.delete();
		}

	}

}