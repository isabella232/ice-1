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
import org.eclipse.ice.reactor.GridLabelProvider;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.LWRComposite;
import org.eclipse.ice.reactor.LWRData;
import org.eclipse.ice.reactor.LWRDataProvider;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.Tube;
import org.eclipse.ice.reactor.pwr.FuelAssembly;
import org.eclipse.ice.reactor.pwr.RodClusterAssembly;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * This class tests the operations on FuelAssembly.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class FuelAssemblyTester {
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
		FuelAssembly assembly;
		String defaultName = "FuelAssembly";
		String defaultDesc = "FuelAssembly's Description";
		int defaultId = 1;
		int defaultSize = 1;
		HDF5LWRTagType type = HDF5LWRTagType.FUEL_ASSEMBLY;

		// New names
		String newName = "Super FuelAssembly!";
		int newSize = 10;

		// Check the default constructor with a default size. Check default
		// values
		// Test non-nullary constructor - size
		assembly = new FuelAssembly(defaultSize);
		// Check values
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(type, assembly.getHDF5LWRTag());

		// Check with new size
		// Test non-nullary constructor - size
		assembly = new FuelAssembly(newSize);
		// Check values
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(newSize, assembly.getSize());
		assertEquals(type, assembly.getHDF5LWRTag());

		// Check with bad size - negative
		// Test non-nullary constructor - size
		assembly = new FuelAssembly(-1);
		// Check values
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultSize, assembly.getSize()); // Defaults
		assertEquals(type, assembly.getHDF5LWRTag());

		// Check with name and size
		// Test non-nullary constructor - name, size
		assembly = new FuelAssembly(defaultName, defaultSize);
		// Check values
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(type, assembly.getHDF5LWRTag());

		// Check with bad name
		// Test non-nullary constructor - name, size
		assembly = new FuelAssembly(null, defaultSize);
		// Check values
		assertEquals(defaultName, assembly.getName()); // Defaults
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(type, assembly.getHDF5LWRTag());

		// Check with new name and size
		// Test non-nullary constructor - name, size
		assembly = new FuelAssembly(newName, newSize);
		// Check values
		assertEquals(newName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(newSize, assembly.getSize());
		assertEquals(type, assembly.getHDF5LWRTag());

	}

	/**
	 * <p>
	 * This operation checks the operations for the GridLabelProvider.
	 * </p>
	 * 
	 */
	@Test
	public void checkLabels() {
		// Local Declarations
		FuelAssembly assembly = null;
		ArrayList<String> rowLabels = new ArrayList<String>();
		ArrayList<String> colLabels = new ArrayList<String>();
		GridLabelProvider provider;
		int assemblySize = 5;

		// Set the rowLabels
		rowLabels.add("1");
		rowLabels.add("2");
		rowLabels.add("3");
		rowLabels.add("4");
		rowLabels.add("5");

		// Set the colLabels
		colLabels.add("A");
		colLabels.add("B");
		colLabels.add("C");
		colLabels.add("D");
		colLabels.add("E");

		// Make a new assembly
		assembly = new FuelAssembly(assemblySize);
		// Check to see the default's gridLabelprovider
		assertEquals(-1,
				assembly.getGridLabelProvider().getColumnFromLabel("A"));
		assertEquals(assemblySize, assembly.getGridLabelProvider().getSize()); // Size
																				// the
																				// same
																				// as
																				// assembly.
																				// Very
																				// important!

		// Check default values for gridlabelprovider
		provider = new GridLabelProvider(assemblySize);
		// Adding the column and row labels
		provider.setColumnLabels(colLabels);
		provider.setRowLabels(rowLabels);
		assembly.setGridLabelProvider(provider);

		// Check the row and column labels
		assertTrue(
				this.doLabelsMatchLabelsInAssembly(true, rowLabels, assembly));
		assertTrue(
				this.doLabelsMatchLabelsInAssembly(false, colLabels, assembly));

		// You can not set it to null or illegal size
		assembly.setGridLabelProvider(null);

		// Check the row and column labels
		assertTrue(
				this.doLabelsMatchLabelsInAssembly(true, rowLabels, assembly));
		assertTrue(
				this.doLabelsMatchLabelsInAssembly(false, colLabels, assembly));

		assembly.setGridLabelProvider(new GridLabelProvider(assemblySize + 22));

		// Stays the same as before
		// Check the row and column labels
		assertTrue(
				this.doLabelsMatchLabelsInAssembly(true, rowLabels, assembly));
		assertTrue(
				this.doLabelsMatchLabelsInAssembly(false, colLabels, assembly));

	}

	/**
	 * <p>
	 * This operation checks the getters and setters for tube.
	 * </p>
	 * 
	 */
	@Test
	public void checkTube() {
		// Local Declarations
		int assemblySize = 17;
		FuelAssembly assembly = new FuelAssembly(assemblySize);
		Tube testComponent = new Tube(), testComponent2 = new Tube(),
				testComponent3 = new Tube();
		String testComponentName = "Bob";
		String testComponentName2 = "Bill!";
		int rowLoc1 = 5, colLoc1 = 5;
		int rowLoc2 = 6, colLoc2 = 6;
		int testComponentId = 1000001;

		// Check the default values of the Component under test
		assembly = new FuelAssembly(assemblySize);

		// No tubes should be added by default. Therefore every
		// location is bad
		for (int i = 0; i < assemblySize; i++) {
			for (int j = 0; j < assemblySize; j++) {
				assertNull(assembly.getTubeByLocation(i, j));
			}
		}

		// Check the names, should be empty!
		assertEquals(0, assembly.getTubeNames().size());

		// Try to get by name - valid string, empty string, and null
		assertNull(assembly
				.getTubeByName("validNameThatDoesNotExistInThere152423"));
		assertNull(assembly.getTubeByName(""));
		assertNull(assembly.getTubeByName(null));

		// Set the name
		testComponent.setName(testComponentName);

		// Add to the assembly
		assembly.addTube(testComponent);

		// See that no location is set
		assertNull(assembly.getTubeByLocation(rowLoc1, colLoc1));
		// Check locations to be within bounds
		assertNull(assembly.getTubeByLocation(-1, assemblySize - 1));
		assertNull(assembly.getTubeByLocation(1, assemblySize - 1));
		assertNull(assembly.getTubeByLocation(assemblySize + 25,
				assemblySize - 1));
		assertNull(assembly.getTubeByLocation(assemblySize - 1,
				assemblySize + 25));

		// Set the valid location:
		assertTrue(
				assembly.setTubeLocation(testComponentName, rowLoc1, colLoc1));

		// Try to break location setter
		assertFalse(assembly.setTubeLocation(null, rowLoc1, colLoc1));
		assertFalse(assembly.setTubeLocation(testComponentName, -1, colLoc1));
		assertFalse(assembly.setTubeLocation(testComponentName, rowLoc1, -1));
		assertFalse(assembly.setTubeLocation(null, -1, colLoc1));
		assertFalse(assembly.setTubeLocation(null, rowLoc1, -1));
		assertFalse(assembly.setTubeLocation(null, -1, -1));
		assertFalse(assembly.setTubeLocation(testComponentName, rowLoc1,
				assemblySize + 25));
		assertFalse(assembly.setTubeLocation(testComponentName,
				assemblySize + 25, colLoc1));

		// The above erroneous settings does not change the original location of
		// the first, valid set
		assertTrue(testComponent
				.equals(assembly.getTubeByName(testComponentName)));

		// Check invalid overwrite of location:
		testComponent2.setName(testComponentName2);

		// Add assembly, overwrite the previous testComponent's location
		assertFalse(assembly.setTubeLocation(testComponent2.getName(), rowLoc1,
				colLoc1));

		// Check that it is the first, but not second
		assertTrue(testComponent
				.equals(assembly.getTubeByName(testComponentName)));

		// Add it in there
		assertTrue(assembly.addTube(testComponent2));

		// Show that you can have at least 2 components in there
		assertTrue(assembly.setTubeLocation(testComponent2.getName(), rowLoc2,
				colLoc2));

		// Check values - see the components are different and they reside in
		// the table correctly
		assertTrue(testComponent
				.equals(assembly.getTubeByName(testComponentName)));
		assertTrue(testComponent2
				.equals(assembly.getTubeByName(testComponentName2)));

		// Check the locations
		assertTrue(testComponent
				.equals(assembly.getTubeByLocation(rowLoc1, colLoc1)));
		assertTrue(testComponent2
				.equals(assembly.getTubeByLocation(rowLoc2, colLoc2)));

		// Check the names, should contain 2!
		assertEquals(2, assembly.getTubeNames().size());
		assertEquals(testComponentName, assembly.getTubeNames().get(0));
		assertEquals(testComponentName2, assembly.getTubeNames().get(1));

		// Check operation for null
		assembly.addTube(null);
		assertNull(assembly.getTubeByName(null)); // Make sure null does
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
		assertFalse(assembly.addTube(testComponent3));

		// Check that the object has not been overwritten
		assertTrue(testComponent
				.equals(assembly.getTubeByName(testComponentName)));
		assertFalse(testComponent3
				.equals(assembly.getTubeByName(testComponentName)));

		// Test to remove components from the assembly
		assertFalse(assembly.removeTube(null));
		assertFalse(assembly.removeTube(""));
		assertFalse(assembly
				.removeTube("!--+ANamETHaTDoESNOTEXIST19674376393<><(@#*)%^"));

		// Remove the first component
		assertTrue(assembly.removeTube(testComponent.getName()));

		// Check that it does not exist in the location or getting the name
		assertNull(assembly.getTubeByLocation(rowLoc1, colLoc1));
		assertNull(assembly.getTubeByName(testComponent.getName()));
		// Check size
		assertEquals(1, assembly.getNumberOfTubes());

		// It can now be overridden!
		assertTrue(assembly.setTubeLocation(testComponent2.getName(), rowLoc1,
				colLoc1));

		// Show that the component's names can NOT overwrite each others
		// locations
		assertTrue(assembly.addTube(testComponent));
		assertFalse(assembly.setTubeLocation(testComponent.getName(), rowLoc1,
				colLoc1));

		// Check the size, the respective locations
		assertEquals(testComponent2.getName(),
				assembly.getTubeByLocation(rowLoc1, colLoc1).getName());
		assertEquals(testComponent2.getName(),
				assembly.getTubeByLocation(rowLoc2, colLoc2).getName());
		assertEquals(2, assembly.getNumberOfTubes());

	}

	/**
	 * <p>
	 * This operation checks the getter and setter for rodClusterAssembly.
	 * </p>
	 * 
	 */
	@Test
	public void checkRodClusterAssembly() {
		// Local Declarations
		RodClusterAssembly newRod = new RodClusterAssembly(1);
		FuelAssembly assembly = new FuelAssembly(2);
		String name = "1231231A rod12313123/*/";

		// Set the newRod's info
		newRod.setName(name);

		// Check the getter on fuel assembly. Show that it is null
		assertNull(assembly.getRodClusterAssembly());

		// Set it
		assembly.setRodClusterAssembly(newRod);
		// Check that it was created and is the newly created rod
		assertEquals(name, assembly.getRodClusterAssembly().getName());

		// Try to set it to null
		assembly.setRodClusterAssembly(null);

		// See that it is null
		assertNull(assembly.getRodClusterAssembly());

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
		FuelAssembly assembly;
		ArrayList<String> compNames = new ArrayList<String>();
		ArrayList<Component> components = new ArrayList<Component>();
		int numberOfDefaultComponents = 0;

		// Defaults for tubeComposite
		LWRComposite tubeComposite;
		String compName = "Tubes";
		String compDescription = "A Composite that contains many Tubes.";
		int compId = 2;

		// Setup LWRRodComposite for comparison
		tubeComposite = new LWRComposite();
		tubeComposite.setName(compName);
		tubeComposite.setId(compId);
		tubeComposite.setDescription(compDescription);

		// Add components to arrays
		compNames.add(tubeComposite.getName());
		components.add((Component) tubeComposite);

		// Defaults for rodComposite
		LWRComposite rodComposite;
		compName = "LWRRods";
		compDescription = "A Composite that contains many LWRRods.";
		compId = 1;

		// Setup LWRRodComposite for comparison
		rodComposite = new LWRComposite();
		rodComposite.setName(compName);
		rodComposite.setId(compId);
		rodComposite.setDescription(compDescription);

		// Add component to arrays
		compNames.add(rodComposite.getName());
		components.add((Component) rodComposite);

		// Setup the default number of components
		numberOfDefaultComponents = components.size();

		// Check the default Composite size and attributes on PWRAssembly
		assembly = new FuelAssembly(assemblySize);

		// Has a size of numberOfDefaultComponents
		assertEquals(numberOfDefaultComponents,
				assembly.getNumberOfComponents());
		// It is equal to the default rodComposite for many of the composite
		// getters
		assertTrue(rodComposite.equals(assembly.getComponent(1)));
		assertTrue(rodComposite
				.equals(assembly.getComponent(rodComposite.getName())));

		// It is equal to the default tubeComposite for many of the composite
		// getters
		assertTrue(tubeComposite.equals(assembly.getComponent(2)));
		assertTrue(tubeComposite
				.equals(assembly.getComponent(tubeComposite.getName())));

		// Check general getters for the other pieces
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
	 * This is a utility operation that checks the operations for the labels on
	 * the assembly to see if they are equal to the passed labels. Keep in mind
	 * that the first parameter, rows, checks to see if its comparing rows
	 * (true) or columns (false). Returns true if the labels are equal, false
	 * otherwise.
	 * </p>
	 * 
	 * @param rows
	 *            <p>
	 *            True if comparing rows. False if comparing columns.
	 *            </p>
	 * @param labels
	 *            <p>
	 *            The labels to be compared to the rows or columns.
	 *            </p>
	 * @param assembly
	 *            <p>
	 *            The assembly to be compared with.
	 *            </p>
	 * @return
	 * 		<p>
	 *         True if they are equal. False otherwise.
	 *         </p>
	 */
	private boolean doLabelsMatchLabelsInAssembly(boolean rows,
			ArrayList<String> labels, FuelAssembly assembly) {
		int colCount = 0, rowCount = 0;

		if (rows == false) {
			// Check you can get the column labels
			for (int i = 0; i < labels.size(); i++) {
				// Assert that the column labels are in order and are set
				// correctly to the 0 indexed value of size
				assertEquals(labels.get(i),
						assembly.getGridLabelProvider().getLabelFromColumn(i));
				assertEquals(i, assembly.getGridLabelProvider()
						.getColumnFromLabel(labels.get(i)));
				colCount++;
			}

			// Assert that the size of the columns equals the size of the
			// reactor
			assertEquals(assembly.getSize(), colCount);
		}

		else {

			// Check you can get the rowlabels
			for (int i = 0; i < labels.size(); i++) {
				// Assert that the row labels are in order and are set correctly
				// to the 0 indexed value of size
				assertEquals(labels.get(i),
						assembly.getGridLabelProvider().getLabelFromRow(i));
				assertEquals(i, assembly.getGridLabelProvider()
						.getRowFromLabel(labels.get(i)));
				rowCount++;
			}

			// Assert that the size of the rows equals the size of the assembly
			assertEquals(assembly.getSize(), rowCount);
		}

		return true;

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
		FuelAssembly object, equalObject, unEqualObject, transitiveObject;
		String name = "Billy";
		int size = 5;
		LWRRod rod = new LWRRod("Bob the rod");
		Tube tube = new Tube("Billy the tube");

		// Setup root object
		object = new FuelAssembly(name, size);
		object.addLWRRod(rod);
		object.addTube(tube);
		object.setLWRRodLocation(rod.getName(), 0, 0);
		object.setTubeLocation(tube.getName(), 1, 1);

		// Setup equalObject equal to object
		equalObject = new FuelAssembly(name, size);
		equalObject.addLWRRod(rod);
		equalObject.addTube(tube);
		equalObject.setLWRRodLocation(rod.getName(), 0, 0);
		equalObject.setTubeLocation(tube.getName(), 1, 1);

		// Setup transitiveObject equal to object
		transitiveObject = new FuelAssembly(name, size);
		transitiveObject.addLWRRod(rod);
		transitiveObject.addTube(tube);
		transitiveObject.setLWRRodLocation(rod.getName(), 0, 0);
		transitiveObject.setTubeLocation(tube.getName(), 1, 1);

		// Set its data, not equal to object
		unEqualObject = new FuelAssembly(name, size);
		unEqualObject.addLWRRod(rod);
		unEqualObject.setLWRRodLocation(rod.getName(), 0, 0);
		// No tube

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
		FuelAssembly object, copyObject, clonedObject;
		String name = "Billy";
		int size = 5;
		LWRRod rod = new LWRRod("Bob the rod");
		Tube tube = new Tube("Billy the tube");

		// Setup root object
		object = new FuelAssembly(name, size);
		object.addLWRRod(rod);
		object.addTube(tube);
		object.setLWRRodLocation(rod.getName(), 0, 0);
		object.setTubeLocation(tube.getName(), 1, 1);

		// Run the copy routine
		copyObject = new FuelAssembly(1);
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (FuelAssembly) object.clone();

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
	public void checkLWRDataProvider() {

		// Make an assembly and setup some locations
		FuelAssembly assembly;

		String name = "Billy";
		int size = 5;
		Tube tube = new Tube("Bob the rod");

		// Setup root object
		assembly = new FuelAssembly(name, size);
		assembly.addTube(tube);

		// Locations
		int row1 = 0, col1 = 0, row2 = 1, col2 = 1, row3 = 2, col3 = 3;

		// Check getters with nothing on the object
		assertNull(assembly.getLWRRodDataProviderAtLocation(row1, col1));
		assertNull(assembly.getLWRRodDataProviderAtLocation(row2, col2));
		assertNull(assembly.getLWRRodDataProviderAtLocation(row3, col3));

		// Add locations
		assembly.setLWRRodLocation(tube.getName(), row1, col1);
		assembly.setLWRRodLocation(tube.getName(), row2, col2);
		assembly.setLWRRodLocation(tube.getName(), row3, col3);

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