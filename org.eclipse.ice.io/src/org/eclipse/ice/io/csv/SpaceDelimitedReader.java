/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation -
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.io.csv;

/**
 * This class implements the IReader interface to provide a reader for space
 * delimited text files. It can read any well-formed space delimited text file.
 * It stores its results in a ListComponent<String[]> on the Form returned from
 * read(). Each String [] in the ListComponent is a line of the file, split and
 * trimmed but uncast. Clients must know the concrete type to which they want to
 * cast.
 *
 * Comments are ignored and begin with the "#" character.
 *
 * @author Jay Jay Billings
 *
 */
public class SpaceDelimitedReader extends DelimitedReader {

	/**
	 * The constructor
	 */
	public SpaceDelimitedReader() {
		// Set the file type. The delimiter is already a space by default.
		type = "space-delimited";
	}

}
