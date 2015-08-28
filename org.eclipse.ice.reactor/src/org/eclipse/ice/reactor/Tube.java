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
package org.eclipse.ice.reactor;

/**
 * <p>
 * The Tube class represents the hollow tubes in a FuelAssembly which allow for
 * the insertion of discrete poison rodlets (Guide Tubes) and instrument
 * thimbles (Instrument Tube).
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class Tube extends Ring {
	/**
	 * <p>
	 * One of the TubeType enumeration values.
	 * </p>
	 * 
	 */
	private TubeType tubeType;

	/**
	 * <p>
	 * The nullary Constructor.
	 * </p>
	 * 
	 */
	public Tube() {
		// Call super constructor - sets up defaults
		super();

		// Set name and description for tube
		this.name = "Tube";
		this.description = "Tube's Description";

		// Setup other attributes for tube
		this.tubeType = TubeType.GUIDE;

		// Setup the LWRComponentType to the correct type
		this.HDF5LWRTag = HDF5LWRTagType.TUBE;

	}

	/**
	 * <p>
	 * The parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of this Tube.
	 *            </p>
	 */
	public Tube(String name) {
		// Call nullary constructor
		this();

		// Set the name
		this.setName(name);

	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of this Tube.
	 *            </p>
	 * @param tubeType
	 *            <p>
	 *            The TubeType enumeration value for this Tube.
	 *            </p>
	 */
	public Tube(String name, TubeType tubeType) {

		// Call non-nullary constructor
		this(name);

		// Call extra operations
		this.setTubeType(tubeType);

	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of this Tube.
	 *            </p>
	 * @param tubeType
	 *            <p>
	 *            The TubeType enumeration value for this Tube.
	 *            </p>
	 * @param material
	 *            <p>
	 *            The Material for this Tube.
	 *            </p>
	 * @param height
	 *            <p>
	 *            The height of this Tube, which must be greater than zero.
	 *            </p>
	 * @param outerRadius
	 *            <p>
	 *            The outer radius of this Tube, which must be greater than the
	 *            innerRadius value.
	 *            </p>
	 */
	public Tube(String name, TubeType tubeType, Material material,
			double height, double outerRadius) {
		// Call non-nullary constructor
		this(name, tubeType);

		// Set other values
		super.setMaterial(material);
		super.setHeight(height);
		super.setOuterRadius(outerRadius);

	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of this Tube.
	 *            </p>
	 * @param tubeType
	 *            <p>
	 *            The TubeType enumeration value for this Tube.
	 *            </p>
	 * @param material
	 *            <p>
	 *            The Material for this Tube.
	 *            </p>
	 * @param height
	 *            <p>
	 *            The height of this Tube, which must be greater than zero.
	 *            </p>
	 * @param innerRadius
	 *            <p>
	 *            The inner radius of this Tube, which must be greater than or
	 *            equal to zero.
	 *            </p>
	 * @param outerRadius
	 *            <p>
	 *            The outer radius of this Tube, which must be greater than the
	 *            innerRadius value.
	 *            </p>
	 */
	public Tube(String name, TubeType tubeType, Material material,
			double height, double innerRadius, double outerRadius) {
		// Call non-nullary constructor
		this(name, tubeType, material, height, outerRadius);

		// Set other values
		this.setInnerRadius(innerRadius);

	}

	/**
	 * <p>
	 * Returns the TubeType enumeration value for this Tube.
	 * </p>
	 * 
	 * @return <p>
	 *         The TubeType enumeration value for this Tube.
	 *         </p>
	 */
	public TubeType getTubeType() {

		return this.tubeType;
	}

	/**
	 * <p>
	 * Sets the TubeType enumeration value for this Tube. Can not set to null.
	 * </p>
	 * 
	 * @param tubeType
	 *            <p>
	 *            The TubeType enumeration value for this Tube.
	 *            </p>
	 */
	public void setTubeType(TubeType tubeType) {

		// If tube type is not null
		if (tubeType != null) {
			this.tubeType = tubeType;
		}

	}

	/*
	 * Overrides a method from Ring.
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Local Declarations
		Tube tube;
		boolean retVal = false;

		// If they are equal to the same object, return true
		if (this == otherObject) {
			return true;
		}
		if (otherObject != null && otherObject instanceof Tube) {

			// Cast
			tube = (Tube) otherObject;

			// Check values
			retVal = super.equals(otherObject)
					&& this.tubeType.equals(tube.tubeType);
		}

		// Return retVal
		return retVal;

	}

	/*
	 * Overrides a method from Ring.
	 */
	@Override
	public int hashCode() {

		// Get hash from super
		int hash = super.hashCode();

		// Get local hash
		hash += 31 * this.tubeType.hashCode();

		// Return the hashCode
		return hash;

	}

	/**
	 * <p>
	 * Deep copies the contents of the object.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be copied.
	 *            </p>
	 */
	public void copy(Tube otherObject) {

		// If the otherObject is null, return
		if (otherObject == null) {
			return;
		}
		// Copy contents - super
		super.copy(otherObject);

		// copy local contents
		this.tubeType = otherObject.tubeType;

	}

	/*
	 * Overrides a method from Ring.
	 */
	@Override
	public Object clone() {
		// Local Declarations
		Tube tube = new Tube();

		// Copy contents
		tube.copy(this);

		// Return newly instantiated object
		return tube;

	}

	/*
	 * Overrides a method from Ring.
	 */
	@Override
	public void accept(ILWRComponentVisitor visitor) {
		visitor.visit(this);
	}
}