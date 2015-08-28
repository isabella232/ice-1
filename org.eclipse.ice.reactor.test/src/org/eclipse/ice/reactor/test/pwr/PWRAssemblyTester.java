/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.LWRComposite;
import org.eclipse.ice.reactor.LWRData;
import org.eclipse.ice.reactor.LWRDataProvider;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.pwr.PWRAssembly;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * This class is responsible for testing the PWAssembly class.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class PWRAssemblyTester {
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
		PWRAssembly assembly;
		// Default values. Change here as needed for tests
		String defaultName = "PWRAssembly";
		String defaultDesc = "PWRAssembly's Description";
		int defaultId = 1;
		int defaultSize = 1;
		double defaultRodPitch = 1;
		HDF5LWRTagType type = HDF5LWRTagType.PWRASSEMBLY;

		// New values
		String newName = "Super Assembly!";
		int newSize = 10;

		// Test non-nullary constructor - size
		assembly = new PWRAssembly(defaultSize);
		// Check values
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(defaultRodPitch, assembly.getRodPitch(), 0.0);
		assertEquals(type, assembly.getHDF5LWRTag());

		// Test non-nullary constructor - size
		assembly = new PWRAssembly(newSize);
		// Check values
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(newSize, assembly.getSize());
		assertEquals(defaultRodPitch, assembly.getRodPitch(), 0.0);
		assertEquals(type, assembly.getHDF5LWRTag());

		// Test non-nullary constructor - illegal size value
		assembly = new PWRAssembly(0);
		// Check values
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultSize, assembly.getSize()); // Defaults
		assertEquals(defaultRodPitch, assembly.getRodPitch(), 0.0);
		assertEquals(type, assembly.getHDF5LWRTag());

		// Test non-nullary constructor - illegal size value - negative
		assembly = new PWRAssembly(-1);
		// Check values
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultSize, assembly.getSize()); // Defaults
		assertEquals(defaultRodPitch, assembly.getRodPitch(), 0.0);
		assertEquals(type, assembly.getHDF5LWRTag());

		// Test non-nullary constructor - name and size
		assembly = new PWRAssembly(defaultName, defaultSize);
		// Check values
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(defaultRodPitch, assembly.getRodPitch(), 0.0);
		assertEquals(type, assembly.getHDF5LWRTag());

		// Test non-nullary constructor - name and size
		assembly = new PWRAssembly(newName, defaultSize);
		// Check values
		assertEquals(newName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(defaultRodPitch, assembly.getRodPitch(), 0.0);
		assertEquals(type, assembly.getHDF5LWRTag());

		// Test non-nullary constructor - bad name but good size
		assembly = new PWRAssembly(null, defaultSize);
		// Check values
		assertEquals(defaultName, assembly.getName()); // Defaults
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(defaultRodPitch, assembly.getRodPitch(), 0.0);
		assertEquals(type, assembly.getHDF5LWRTag());

	}

	/**
	 * <p>
	 * This operation checks the adders, getters, and setters for LWRRods.
	 * </p>
	 * 
	 */
	@Test
	public void checkLWRRodOperations() {
		// Local Declarations
		int assemblySize = 17;
		PWRAssembly assembly = new PWRAssembly(assemblySize);
		LWRRod testComponent = new LWRRod(), testComponent2 = new LWRRod(),
				testComponent3 = new LWRRod();
		String testComponentName = "Bob";
		String testComponentName2 = "Bill!";
		int rowLoc1 = 5, colLoc1 = 5;
		int rowLoc2 = 6, colLoc2 = 6;
		int testComponentId = 1000001;
		double defaultRodPitch;

		// Check the default values of the Component under test
		assembly = new PWRAssembly(assemblySize);

		// Check rodPitch setting
		// Set the defaultRodPitch
		defaultRodPitch = assembly.getRodPitch();

		// Set the rodPitch to 0 - fails
		assembly.setRodPitch(0.0);
		assertEquals(defaultRodPitch, assembly.getRodPitch(), 0.0);

		// Set the rod Pitch to 1
		assembly.setRodPitch(1);
		assertEquals(1.0, assembly.getRodPitch(), 0.0);
		defaultRodPitch = assembly.getRodPitch(); // Reset default value to
													// reflect next test.

		// Set the rod pitch to negative - does not work
		assembly.setRodPitch(-1.0);
		assertEquals(defaultRodPitch, assembly.getRodPitch(), 0.0);

		// No rods should be added by default. Therefore every
		// location is bad
		for (int i = 0; i < assemblySize; i++) {
			for (int j = 0; j < assemblySize; j++) {
				assertNull(assembly.getLWRRodByLocation(i, j));
			}
		}

		// Check the names, should be empty!
		assertEquals(0, assembly.getLWRRodNames().size());

		// Try to get by name - valid string, empty string, and null
		assertNull(assembly
				.getLWRRodByName("validNameThatDoesNotExistInThere152423"));
		assertNull(assembly.getLWRRodByName(""));
		assertNull(assembly.getLWRRodByName(null));

		// Set the name
		testComponent.setName(testComponentName);

		// Add to the assembly
		assembly.addLWRRod(testComponent);

		// See that no location is set
		assertNull(assembly.getLWRRodByLocation(rowLoc1, colLoc1));
		// Check locations to be within bounds
		assertNull(assembly.getLWRRodByLocation(-1, assemblySize - 1));
		assertNull(assembly.getLWRRodByLocation(1, assemblySize - 1));
		assertNull(assembly.getLWRRodByLocation(assemblySize + 25,
				assemblySize - 1));
		assertNull(assembly.getLWRRodByLocation(assemblySize - 1,
				assemblySize + 25));

		// Set the valid location:
		assertTrue(assembly.setLWRRodLocation(testComponentName, rowLoc1,
				colLoc1));

		// Try to break location setter
		assertFalse(assembly.setLWRRodLocation(null, rowLoc1, colLoc1));
		assertFalse(assembly.setLWRRodLocation(testComponentName, -1, colLoc1));
		assertFalse(assembly.setLWRRodLocation(testComponentName, rowLoc1, -1));
		assertFalse(assembly.setLWRRodLocation(null, -1, colLoc1));
		assertFalse(assembly.setLWRRodLocation(null, rowLoc1, -1));
		assertFalse(assembly.setLWRRodLocation(null, -1, -1));
		assertFalse(assembly.setLWRRodLocation(testComponentName, rowLoc1,
				assemblySize + 25));
		assertFalse(assembly.setLWRRodLocation(testComponentName,
				assemblySize + 25, colLoc1));

		// The above erroneous settings does not change the original location of
		// the first, valid set
		assertTrue(testComponent
				.equals(assembly.getLWRRodByName(testComponentName)));

		// Check invalid overwrite of location:
		testComponent2.setName(testComponentName2);

		// Add assembly, overwrite the previous testComponent's location
		assertFalse(assembly.setLWRRodLocation(testComponent2.getName(),
				rowLoc1, colLoc1));

		// Check that it is the first, but not second
		assertTrue(testComponent
				.equals(assembly.getLWRRodByName(testComponentName)));

		// Add it in there
		assertTrue(assembly.addLWRRod(testComponent2));

		// Show that you can have at least 2 components in there
		assertTrue(assembly.setLWRRodLocation(testComponent2.getName(), rowLoc2,
				colLoc2));

		// Check values - see the components are different and they reside in
		// the table correctly
		assertTrue(testComponent
				.equals(assembly.getLWRRodByName(testComponentName)));
		assertTrue(testComponent2
				.equals(assembly.getLWRRodByName(testComponentName2)));

		// Check the locations
		assertTrue(testComponent
				.equals(assembly.getLWRRodByLocation(rowLoc1, colLoc1)));
		assertTrue(testComponent2
				.equals(assembly.getLWRRodByLocation(rowLoc2, colLoc2)));

		// Check the names, should contain 2!
		assertEquals(2, assembly.getLWRRodNames().size());
		assertEquals(testComponentName, assembly.getLWRRodNames().get(0));
		assertEquals(testComponentName2, assembly.getLWRRodNames().get(1));

		// Check operation for null
		assembly.addLWRRod(null);
		assertNull(assembly.getLWRRodByName(null)); // Make sure null does
													// not work!

		// Finally, demonstrate what happens when a component of the same name
		// is added, it should not overwrite the previous item in the table!
		testComponent3.setName(testComponent.getName()); // Same name as the
															// other
		// component
		testComponent3.setId(testComponentId); // Id should differ from
												// testComponent!
		assertFalse(testComponent.getId() == testComponentId);

		// Overwrite in table
		assertFalse(assembly.addLWRRod(testComponent3));

		// Check that the object has not been overwritten
		assertTrue(testComponent
				.equals(assembly.getLWRRodByName(testComponentName)));
		assertFalse(testComponent3
				.equals(assembly.getLWRRodByName(testComponentName)));

		// Test to remove components from the assembly
		assertFalse(assembly.removeLWRRod(null));
		assertFalse(assembly.removeLWRRod(""));
		assertFalse(assembly.removeLWRRod(
				"!--+ANamETHaTDoESNOTEXIST19674376393<><(@#*)%^"));

		// Remove the first component
		assertTrue(assembly.removeLWRRod(testComponent.getName()));

		// Check that it does not exist in the location or getting the name
		assertNull(assembly.getLWRRodByLocation(rowLoc1, colLoc1));
		assertNull(assembly.getLWRRodByName(testComponent.getName()));
		// Check size
		assertEquals(1, assembly.getNumberOfLWRRods());

		// It can now be overridden!
		assertTrue(assembly.setLWRRodLocation(testComponent2.getName(), rowLoc1,
				colLoc1));

		// Show that the component's names can NOT overwrite each others
		// locations
		assertTrue(assembly.addLWRRod(testComponent));
		assertFalse(assembly.setLWRRodLocation(testComponent.getName(), rowLoc1,
				colLoc1));

		// Check the size, the respective locations
		assertEquals(testComponent2.getName(),
				assembly.getLWRRodByLocation(rowLoc1, colLoc1).getName());
		assertEquals(testComponent2.getName(),
				assembly.getLWRRodByLocation(rowLoc2, colLoc2).getName());
		assertEquals(2, assembly.getNumberOfLWRRods());

	}

	/**
	 * <p>
	 * This operation demonstrates the behaviors listed with the overridden
	 * composite implementations from LWRComposite.
	 * </p>
	 * 
	 */
	@Test
	public void checkCompositeImplementations() {
		// Local Declarations
		int assemblySize = 17;
		PWRAssembly assembly;
		ArrayList<String> compNames = new ArrayList<String>();
		ArrayList<Component> components = new ArrayList<Component>();
		int numberOfDefaultComponents = 0;

		// Defaults for rodComposite
		LWRComposite rodComposite;
		String compName = "LWRRods";
		String compDescription = "A Composite that contains many LWRRods.";
		int compId = 1;

		// Setup LWRRodComposite for comparison
		rodComposite = new LWRComposite();
		rodComposite.setName(compName);
		rodComposite.setId(compId);
		rodComposite.setDescription(compDescription);

		// Add components to arrays
		compNames.add(rodComposite.getName());
		components.add((Component) rodComposite);
		// Setup the default number of components
		numberOfDefaultComponents = components.size();

		// Check the default Composite size and attributes on PWRAssembly
		assembly = new PWRAssembly(assemblySize);

		// Has a size of numberOfDefaultComponents
		assertEquals(numberOfDefaultComponents,
				assembly.getNumberOfComponents());
		// It is equal to the default rodComposite for many of the composite
		// getters
		assertTrue(rodComposite.equals(assembly.getComponent(1)));
		assertTrue(rodComposite
				.equals(assembly.getComponent(rodComposite.getName())));
		assertTrue(compNames.equals(assembly.getComponentNames()));
		assertTrue(components.equals(assembly.getComponents()));

		// These operations will show that these will not work for this class

		// Check addComponent
		assertEquals(numberOfDefaultComponents,
				assembly.getNumberOfComponents());
		assembly.addComponent(new LWRComposite());
		// No size change!
		assertEquals(numberOfDefaultComponents,
				assembly.getNumberOfComponents());

		// Check removeComponent - id
		assertEquals(numberOfDefaultComponents,
				assembly.getNumberOfComponents());
		assembly.removeComponent(1);
		// No size change!
		assertEquals(numberOfDefaultComponents,
				assembly.getNumberOfComponents());

		// Check remove component - name
		assertEquals(numberOfDefaultComponents,
				assembly.getNumberOfComponents());
		assembly.removeComponent(components.get(0).getName()); // Try to remove
																// the first off
																// the list
		// No size change!
		assertEquals(numberOfDefaultComponents,
				assembly.getNumberOfComponents());

	}

	/**
	 * <p>
	 * This operation checks the getter and setter for rodPitch.
	 * </p>
	 * 
	 */
	@Test
	public void checkRodPitch() {
		PWRAssembly assembly = new PWRAssembly(2);
		double defaultPitch = 1;

		// Check default value on assembly
		assertEquals(defaultPitch, assembly.getRodPitch(), 0.0);

		// Set it to 1
		assembly.setRodPitch(1.0);
		assertEquals(1.0, assembly.getRodPitch(), 0.0);

		// Set it to 0 - fails
		assembly.setRodPitch(0.0);
		assertEquals(1.0, assembly.getRodPitch(), 0.0);

		// Set it to negative - illegal value
		assembly.setRodPitch(-1.0);
		assertEquals(1.0, assembly.getRodPitch(), 0.0);

	}

	/**
	 * <p>
	 * This operation checks the equals and hashCode operations.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {

		// Local Declarations
		PWRAssembly object, equalObject, unEqualObject, transitiveObject;
		String name = "Billy";
		int size = 5;
		LWRRod rod = new LWRRod("Bob the rod");

		// Setup root object
		object = new PWRAssembly(name, size);
		object.addLWRRod(rod);
		object.setLWRRodLocation(rod.getName(), 0, 0);

		// Setup equalObject equal to object
		equalObject = new PWRAssembly(name, size);
		equalObject.addLWRRod(rod);
		equalObject.setLWRRodLocation(rod.getName(), 0, 0);

		// Setup transitiveObject equal to object
		transitiveObject = new PWRAssembly(name, size);
		transitiveObject.addLWRRod(rod);
		transitiveObject.setLWRRodLocation(rod.getName(), 0, 0);

		// Set its data, not equal to object
		unEqualObject = new PWRAssembly(name, size);
		// Uses the default rod

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
	@Test
	public void checkCopying() {

		// Local Declarations
		PWRAssembly object, copyObject, clonedObject;
		String name = "Billy";
		int size = 5;
		LWRRod rod = new LWRRod("Bob the rod");

		// Setup root object
		object = new PWRAssembly(name, size);
		object.addLWRRod(rod);
		object.setLWRRodLocation(rod.getName(), 0, 0);

		// Run the copy routine
		copyObject = new PWRAssembly(1);
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (PWRAssembly) object.clone();

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

	/**
	 * 
	 */
	@Test
	public void checkLWRDataProvider() {

		// Make an assembly and setup some locations
		PWRAssembly assembly;

		String name = "Billy";
		int size = 5;
		LWRRod rod = new LWRRod("Bob the rod");

		// Setup root object
		assembly = new PWRAssembly(name, size);
		assembly.addLWRRod(rod);

		// Locations
		int row1 = 0, col1 = 0, row2 = 1, col2 = 1, row3 = 2, col3 = 3;

		// Check getters with nothing on the object
		assertNull(assembly.getLWRRodDataProviderAtLocation(row1, col1));
		assertNull(assembly.getLWRRodDataProviderAtLocation(row2, col2));
		assertNull(assembly.getLWRRodDataProviderAtLocation(row3, col3));

		// Add locations
		assembly.setLWRRodLocation(rod.getName(), row1, col1);
		assembly.setLWRRodLocation(rod.getName(), row2, col2);
		assembly.setLWRRodLocation(rod.getName(), row3, col3);

		// Check not null
		assertNotNull(assembly.getLWRRodDataProviderAtLocation(row1, col1));
		assertNotNull(assembly.getLWRRodDataProviderAtLocation(row2, col2));
		assertNotNull(assembly.getLWRRodDataProviderAtLocation(row3, col3));

		// Prepare some data
		LWRData data1 = new LWRData("Feature1111");
		LWRData data2 = new LWRData("Feature1111");
		LWRData data3 = new LWRData("Feature1112");
		LWRData data4 = new LWRData("Feature1113");

		// Setup providers and times
		LWRDataProvider provider = new LWRDataProvider();

		double time1 = 0.0, time2 = 0.1;

		// Setup provider
		provider.addData(data1, time1);
		provider.addData(data2, time1);
		provider.addData(data3, time1);
		provider.addData(data4, time2);

		// Add data
		assembly.getLWRRodDataProviderAtLocation(row1, col1).addData(data1,
				time1);
		assembly.getLWRRodDataProviderAtLocation(row1, col1).addData(data2,
				time1);
		assembly.getLWRRodDataProviderAtLocation(row1, col1).addData(data3,
				time1);
		assembly.getLWRRodDataProviderAtLocation(row1, col1).addData(data4,
				time2);

		// Verify data
		assertTrue(provider
				.equals(assembly.getLWRRodDataProviderAtLocation(row1, col1)));

	}
}