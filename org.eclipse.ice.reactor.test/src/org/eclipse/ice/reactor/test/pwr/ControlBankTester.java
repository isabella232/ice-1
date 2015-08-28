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
package org.eclipse.ice.reactor.test.pwr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.pwr.ControlBank;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * This class tests the ControlBank class.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class ControlBankTester {
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
	 * This operation checks the constructors and their default values.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {
		// Local Declarations
		ControlBank controlBank;
		// Default Values
		String defaultName = "ControlBank 1";
		String defaultDesc = "Default Control Bank";
		int defaultId = 1;
		double defaultStepSize = 0.0;
		int defaultMaxNumberOfSteps = 1;
		HDF5LWRTagType type = HDF5LWRTagType.CONTROL_BANK;

		// New Values
		String newName = "ControlBank 2";
		double newStepSize = 10.0;
		int newMaxNumberOfSteps = 100;

		// Check nullary constructor
		controlBank = new ControlBank();
		assertEquals(defaultName, controlBank.getName());
		assertEquals(defaultStepSize, controlBank.getStepSize(), 0.0);
		assertEquals(defaultMaxNumberOfSteps,
				controlBank.getMaxNumberOfSteps());
		assertEquals(defaultId, controlBank.getId());
		assertEquals(defaultDesc, controlBank.getDescription());
		assertEquals(type, controlBank.getHDF5LWRTag());

		// Check non-nullary constructor
		controlBank = new ControlBank(newName, newStepSize,
				newMaxNumberOfSteps);
		assertEquals(newName, controlBank.getName());
		assertEquals(newStepSize, controlBank.getStepSize(), 0.0);
		assertEquals(newMaxNumberOfSteps, controlBank.getMaxNumberOfSteps());
		assertEquals(type, controlBank.getHDF5LWRTag());

		// Check non-nullary constructor for illegal values
		controlBank = new ControlBank(null, -1.0, -1);
		assertEquals(defaultName, controlBank.getName());
		assertEquals(defaultStepSize, controlBank.getStepSize(), 0.0);
		assertEquals(defaultMaxNumberOfSteps,
				controlBank.getMaxNumberOfSteps());
		assertEquals(type, controlBank.getHDF5LWRTag());

		// Check non-nullary constructor for 0 value of MaxNumberOfSteps
		controlBank = new ControlBank(newName, newStepSize, 0);
		assertEquals(newName, controlBank.getName());
		assertEquals(newStepSize, controlBank.getStepSize(), 0.0);
		assertEquals(defaultMaxNumberOfSteps,
				controlBank.getMaxNumberOfSteps());
		assertEquals(type, controlBank.getHDF5LWRTag());

	}

	/**
	 * <p>
	 * This operation checks the getter and setter for the stepSize.
	 * </p>
	 * 
	 */
	@Test
	public void checkStepSize() {
		// Local Declarations
		ControlBank controlBank = new ControlBank();

		// Set the size to 0
		controlBank.setStepSize(0.0);

		// This checks a valid return after the step size is set
		assertEquals(0.0, controlBank.getStepSize(), 0.0);

		// Pass an invalid number: negative
		controlBank.setStepSize(-1.0);

		// Should not change the previous value.
		assertEquals(0.0, controlBank.getStepSize(), 0.0);

		// Test a valid length
		controlBank.setStepSize(10.0);

		// Should set correct value.
		assertEquals(10.0, controlBank.getStepSize(), 0.0);

	}

	/**
	 * <p>
	 * This operation checks the getter and setter for the maxNumberOfSteps
	 * </p>
	 * 
	 */
	@Test
	public void checkMaxNumberOfSteps() {
		// Local Declarations
		ControlBank controlBank = new ControlBank();

		// Set the Max Step Size to default
		controlBank.setMaxNumberOfSteps(1);

		// This checks a valid return after the Max step size is set
		assertEquals(1, controlBank.getMaxNumberOfSteps());

		// Pass an invalid number: negative
		controlBank.setMaxNumberOfSteps(-1);

		// Should not change the previous value.
		assertEquals(1, controlBank.getMaxNumberOfSteps());

		// Test a valid length
		controlBank.setMaxNumberOfSteps(10);

		// Should set correct value.
		assertEquals(10, controlBank.getMaxNumberOfSteps());

	}

	/**
	 * <p>
	 * This operation checks the getStrokeLength method.
	 * </p>
	 * 
	 */
	@Test
	public void checkStrokeLength() {

		// Local Declarations
		ControlBank controlBank = new ControlBank();

		// Checking default values.
		assertEquals(1, controlBank.getMaxNumberOfSteps());
		assertEquals(0.0, controlBank.getStepSize(), 0.0);

		// Check valid test
		assertEquals(0.0, controlBank.getStrokeLength(), 0.0);

		// Check nonzero
		controlBank.setStepSize(100.0);

		// Check valid test
		assertEquals(100.0, controlBank.getStrokeLength(), 0.0);

		// Check valid test - decimal
		controlBank.setMaxNumberOfSteps(2);
		controlBank.setStepSize(50.05);

		// Check valid test
		assertEquals(100.1, controlBank.getStrokeLength(), 0.0);

	}

	/**
	 * <p>
	 * This operation checks the equals and hashCode operations.
	 * </p>
	 * 
	 */
	public void checkEquality() {

		// Local Declarations
		ControlBank object, equalObject, unEqualObject, transitiveObject;
		String name = "CONTROL!";
		double stepSize = 5;
		int maxNumberOfSteps = 10;
		int unEqualMaxNumberOfSteps = 9;

		// Setup root object
		object = new ControlBank(name, stepSize, maxNumberOfSteps);

		// Setup equalObject equal to object
		equalObject = new ControlBank(name, stepSize, maxNumberOfSteps);

		// Setup transitiveObject equal to object
		transitiveObject = new ControlBank(name, stepSize, maxNumberOfSteps);

		// Set its data, not equal to object
		// Does not contain components!
		unEqualObject = new ControlBank(name, stepSize,
				unEqualMaxNumberOfSteps);

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
	 * This operation checks the copying and clone operations.
	 * </p>
	 * 
	 */
	public void checkCopying() {

		// Local Declarations
		ControlBank object, copyObject, clonedObject;
		String name = "CONTROL!";
		double stepSize = 5;
		int maxNumberOfSteps = 10;

		// Setup root object
		object = new ControlBank(name, stepSize, maxNumberOfSteps);

		// Run the copy routine
		copyObject = new ControlBank();
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (ControlBank) object.clone();

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