/*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Joe Osborn
 *******************************************************************************/
package org.eclipse.ice.tests.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.Connection;
import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.ConnectionManager;
import org.eclipse.ice.commands.ConnectionManagerFactory;
import org.eclipse.ice.commands.RemoteFileHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * This class tests remote file handling capabilities
 * 
 * @author Joe Osborn
 *
 */
public class RemoteFileHandlerTest {

	/**
	 * A string containing the source file to move
	 */
	String theSource = "";

	/**
	 * A string containing the destination to move the source file to
	 */
	String theDestination = "";

	/**
	 * A connection to use throughout the test
	 */
	static Connection fileTransferConn = new Connection();

	/**
	 * Setup the dummy connection so that the file transfer tests can access an ssh
	 * connection
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();
	
		ConnectionConfiguration config = makeConnectionConfiguration();

		fileTransferConn = manager.openConnection(config);

		fileTransferConn.setSftpChannel(fileTransferConn.getSession().openChannel("sftp"));
		fileTransferConn.getSftpChannel().connect();

	}

	/**
	 * Remove any extraneous connections that may still be open
	 * 
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ConnectionManagerFactory.getConnectionManager().removeAllConnections();
	}

	/**
	 * Function which checks nonexistent source exception throwing for both local
	 * and remote file handlers
	 * 
	 * @throws Exception
	 */
	@Test(expected = Exception.class)
	public void testNonexistentSource() throws Exception {
		System.out.println("Test nonexistent source");
		RemoteFileHandler handler = new RemoteFileHandler();
		ConnectionConfiguration config = fileTransferConn.getConfiguration();
		handler.setConnectionConfiguration(config);

		handler.move("/non/existent/source/", "/non/existent/destination/");

		System.out.println("Finished testing non existent source");
	}

	/**
	 * Test setting the connection for the remote file handler
	 */
	@Test
	public void testSetConnection() throws Exception {
		System.out.println("Test set connection");
		ConnectionConfiguration config = fileTransferConn.getConfiguration();
		RemoteFileHandler handler = new RemoteFileHandler();

		handler.setConnectionConfiguration(config);

		assert (handler.getConnection().getSession().isConnected());

		assert (handler.getConnection().getSftpChannel().isConnected());

		System.out.println("all finished testing set connection");
	}

	@Test
	public void testSetNewConnection() throws Exception {
		ConnectionConfiguration config = makeConnectionConfiguration();
		RemoteFileHandler handler = new RemoteFileHandler();
		handler.setConnectionConfiguration(config);

		assert (handler.getConnection().getSession().isConnected());
		assert (handler.getConnection().getSftpChannel().isConnected());

		ConnectionManagerFactory.getConnectionManager().removeConnection(config.getName());
	}

	/**
	 * Test the exists function for remote file handlers
	 */
	@Test
	public void testRemoteExists() throws Exception {
		System.out.println("Testing remote exists function");

		// Set up the connection first to create the file
		createRemoteSource();

		// Make the remote file handler with the dummy configuration
		RemoteFileHandler handler = new RemoteFileHandler();
		handler.setConnectionConfiguration(fileTransferConn.getConfiguration());

		// Check two asserts - that the created file exists, and that
		// some other nonexistent file throws an error
		assert (handler.exists(theSource));

		assert (!handler.exists("/some/nonexistent/path/file.txt"));

		// Done with the remote source, delete it
		deleteRemoteSource();

		// check and see if a nonexistent file remotely, which does exist locally,
		// returns true
		createLocalSource();

		assert (handler.exists(theSource));
		// Done with the local source, delete it

		deleteLocalSource();

	}

	/**
	 * Test method for testing remote moving capabilities when moving a remote file
	 * to the local host. Also tests throwing an exception for a nonexistent local
	 * directory
	 */
	@Test
	public void testRemoteToLocalMove() throws Exception {

		// Make a remote file to play with and a local directory to move it to
		createRemoteSource();
		createLocalDestination();

		RemoteFileHandler handler = new RemoteFileHandler();
		handler.setConnectionConfiguration(fileTransferConn.getConfiguration());

		String src = theSource;

		// Now try to move the file
		CommandStatus status = handler.move(src, theDestination);

		assert (status == CommandStatus.SUCCESS);

		deleteLocalDestination();

		// Keep the same source, but add a new subdirectory and file name for the
		// destination.
		// This test checks if an error is thrown for a nonexistent local directory
		// The API can't just make the local directory if it isn't found, because
		// otherwise there is no way to differentiate between a remote --> local and
		// remote --> remote move. Thus, it is up to the user to ensure that their
		// local destination directory exists
		String dest = theDestination + "/newDirectory/newFilename.txt";
		status = handler.move(src, dest);

		deleteRemoteSource();

	}

	/**
	 * Test method for testing remote moving capabilities when moving a local file
	 * to a remote host
	 */
	@Test
	public void testLocalToRemoteMove() throws Exception {

		// Make a local test file to play with
		// Make a remote destination directory to move to

		createLocalSource();
		createRemoteDestination();

		// Get the dummy connection configuration from the helper function
		// which allows reusing the dummy config code
		// Get the remote file handler
		RemoteFileHandler handler = new RemoteFileHandler();
		handler.setConnectionConfiguration(fileTransferConn.getConfiguration());

		// Now try to move the file
		CommandStatus status = handler.move(theSource, theDestination);
		assert (status == CommandStatus.SUCCESS);

		// Lets try a file move also where we change the name of the file
		// and add an additional directory
		String dest = theDestination + "newDirectory/newFileName.txt";
		status = handler.move(theSource, dest);

		assert (status == CommandStatus.SUCCESS);

		// Delete the test file/directory now that the test is finished
		deleteLocalSource();
		deleteRemoteDestination();

	}

	/**
	 * This function tests remote to remote file handling. Only remote to remote
	 * copying needs an additional test, because the functionality is identical
	 * except for the remote-to-remote handling
	 */
	@Test
	public void testRemoteToRemoteCopy() throws Exception {
		createRemoteSource();
		createRemoteDestination();

		RemoteFileHandler handler = new RemoteFileHandler();
		handler.setConnectionConfiguration(fileTransferConn.getConfiguration());

		String src = theSource;
		// Get the filename to check for existence
		String filename = src.substring(src.lastIndexOf("/"));

		// Now try to copy the file
		CommandStatus status = handler.copy(src, theDestination);

		assert (status == CommandStatus.SUCCESS);

		deleteRemoteDestination();
		deleteRemoteSource();

	}

	/**
	 * This function tests remote to remote file handling
	 */
	@Test
	public void testRemoteToRemoteMove() throws Exception {
		createRemoteSource();
		createRemoteDestination();

		RemoteFileHandler handler = new RemoteFileHandler();
		handler.setConnectionConfiguration(fileTransferConn.getConfiguration());

		String src = theSource;

		// Now try to move the file
		CommandStatus status = handler.move(src, theDestination);

		assert (status == CommandStatus.SUCCESS);

		deleteRemoteDestination();
		deleteRemoteSource();

	}

	/**
	 * Function to execute the remote file browsing and remote directory browsing
	 * test. We call one main function so that a file structure can be created at
	 * the beginning of the test, accessed by both "subtests", and then deleted at
	 * the end
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRemoteBrowsing() throws Exception {
		// We'll create our own code here to create the file structure rather than
		// using the already developed functions, since we need multiple files and
		// directories
		String topDirectory = "/tmp/fileBrowsingDir/";

		// put this in a try and finally so that the remote file structure
		// always gets deleted. Otherwise the next test run might fail when it
		// tries to build the remote file structure and see that it already
		// exists
		try {
			createRemoteFileStructure(topDirectory);

			testRemoteFileBrowsing(topDirectory);

			testRemoteDirectoryBrowsing(topDirectory);
		} finally {
			deleteRemoteFileStructure(topDirectory);
		}
	}

	/**
	 * Test for file browsing on remote system
	 * 
	 * @throws IOException
	 * @throws SftpException
	 */
	public void testRemoteFileBrowsing(String topDirectory) throws IOException, SftpException {

		RemoteFileHandler handler = new RemoteFileHandler();
		handler.setConnectionConfiguration(fileTransferConn.getConfiguration());

		ArrayList<String> files = handler.listFiles(topDirectory);

		// files should only be 4 entries since there are only 4 files in the tree
		// structure we created
		assert (files.size() == 4);

		for (int i = 0; i < files.size(); i++) {
			// Assert that the file exists
			assert (handler.exists(files.get(i)));

			ChannelSftp channel = fileTransferConn.getSftpChannel();
			Collection<ChannelSftp.LsEntry> structure = channel.ls(files.get(i));
			// This should just be a one file vector, since we are ls-ing the filename
			for (ChannelSftp.LsEntry filename : structure) {
				// Assert that the file is indeed a regular file
				assert (filename.getAttrs().isReg());
			}
		}
	}

	/**
	 * Test for directory browsing on remote system
	 * 
	 * @throws IOException
	 * @throws SftpException
	 */
	public void testRemoteDirectoryBrowsing(String topDirectory) throws IOException, SftpException {

		RemoteFileHandler handler = new RemoteFileHandler();
		handler.setConnectionConfiguration(fileTransferConn.getConfiguration());

		ArrayList<String> files = handler.listDirectories(topDirectory);

		// directories should only be 3 entries since there are only 3 directories in
		// the tree structure we created
		assert (files.size() == 3);

		// Use the default separator of the remote dummy system
		String separator = "/";

		for (int i = 0; i < files.size(); i++) {
			assert (handler.exists(files.get(i)));
			ChannelSftp channel = fileTransferConn.getSftpChannel();

			// ls the previous directory so that we can look at the subdirectories
			Collection<ChannelSftp.LsEntry> structure = channel
					.ls(files.get(i).substring(0, files.get(i).lastIndexOf(separator)));
			// Iterate through the ls
			for (ChannelSftp.LsEntry filename : structure) {
				// Check that the ls filename is actually in the directory list (and thus is a
				// directory)
				if (files.get(i).contains(filename.getFilename())) {
					// assert that it is a directory
					assert (filename.getAttrs().isDir());
				}
			}
		}

	}

	/**
	 * Function that creates a dummy remote file structure tree to test the file
	 * browsing source code
	 * 
	 * @param - topDirectory - refers to top directory whose contents will hold the
	 *          dummy directories/files
	 * @throws SftpException
	 */
	protected void createRemoteFileStructure(String topDirectory) throws Exception, SftpException {

		ChannelSftp sftpChannel = fileTransferConn.getSftpChannel();

		// Check if the directory already exists
		SftpATTRS attrs = null;
		try {
			attrs = sftpChannel.lstat(topDirectory);
		} catch (Exception e) {
			System.out.println("Remote directory not found, trying to make it");
		}
		if (attrs == null) {
			// Remote directory doesn't exist, so make it
			// Create a remote source directory
			sftpChannel.mkdir(topDirectory);
		}

		// make another directory in top directory
		sftpChannel.cd(topDirectory);
		sftpChannel.mkdir("dir1");
		sftpChannel.mkdir("dir2");
		sftpChannel.mkdir("dir3");

		// create a local file to put there
		createLocalSource();
		// Get the filename that was just created
		String filename = theSource;
		// put it in a few places in the directory structure
		sftpChannel.put(filename, topDirectory);
		sftpChannel.put(filename, topDirectory + "/dir1/");
		sftpChannel.put(filename, topDirectory + "/dir3/");
		sftpChannel.put(filename, topDirectory + "/dir3/newfile.txt");

	}

	/**
	 * Function that deletes the remote file structure tree created for testing file
	 * browsing
	 * 
	 * @param - topDirectory - refers to top directory whose contents hold the dummy
	 *          directories/files
	 * @throws JSchException
	 * @throws SftpException
	 */
	protected void deleteRemoteFileStructure(String topDirectory) throws SftpException, JSchException {
		// Connect the channel from the connection
		ChannelSftp sftpChannel = fileTransferConn.getSftpChannel();

		System.out.println("Deleting remote destination at : " + topDirectory);
		// Recursively delete the directory and its contents
		deleteRemoteDirectory(sftpChannel, topDirectory);

	}

	/**
	 * Dummy function which makes the connection configuration for the dummy remote
	 * ssh connection. This way functions can grab the configuration at will with
	 * one line of code.
	 * 
	 * @return
	 */
	private static ConnectionConfiguration makeConnectionConfiguration() {
		// Set the connection configuration to a dummy remote connection
		ConnectionConfiguration config = new ConnectionConfiguration();
		// Get a factory which determines the type of authorization
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
		// Request a ConnectionAuthorization of type text file which contains the
		// credentials
		String credFile = "/tmp/ice-remote-creds.txt";
		if (System.getProperty("os.name").toLowerCase().contains("win"))
			credFile = "C:\\Users\\Administrator\\ice-remote-creds.txt";
		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("text", credFile);
		// Set it
		config.setAuthorization(auth);

		// Note the password can be input at the console by not setting the
		// the password explicitly in the connection configuration
		String name = "dummyConnection" + Math.random();

		config.setName(name);

		config.deleteWorkingDirectory(false);

		return config;
	}

	/**
	 * This setup creates a test dummy file to transfer around. It creates the file
	 * by default locally.
	 * 
	 * @throws java.lang.Exception
	 */
	public void createLocalSource() throws Exception {
		// Create a dummy file to test
		String source = "dummyfile.txt";
		Path sourcePath = null;
		try {
			sourcePath = Files.createTempFile(null, source);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Set the global variable to the source file just created
		theSource = sourcePath.toString();

		System.out.println("Created source file at: " + theSource);

	}

	/**
	 * This setup function creates a test local destination path to transfer a file
	 * to.
	 * 
	 * @throws Exception
	 */
	public void createLocalDestination() throws Exception {

		// Create a destination path name
		Path destinationPath = null;
		String dest = "testDirectory";
		// Try to create the path
		try {
			destinationPath = Files.createTempDirectory(dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Turn the path into a string to give to the command
		theDestination = destinationPath.toString();
		System.out.println("Created destination file at: " + theDestination);

	}

	/**
	 * This function deletes/removes a test dummy file after the test has finished.
	 * 
	 * @throws java.lang.Exception
	 */

	public void deleteLocalSource() throws Exception {
		System.out.println("Delete temporary files/directories that were created.");

		// Get the paths
		Path localSourcePath = Paths.get(theSource);

		// Delete the source file that was created
		try {
			Files.deleteIfExists(localSourcePath);
		} catch (IOException e) {
			System.out.println("Couldn't delete path " + localSourcePath);
			e.printStackTrace();
		}
		System.out.println("Successfully deleted local source file");
	}

	/**
	 * This function deletes/removes a test dummy destination directory after the
	 * test has finished.
	 * 
	 * @throws java.lang.Exception
	 */
	public void deleteLocalDestination() throws Exception {
		// Get the path that was created
		Path localDestPath = Paths.get(theDestination);
		// Delete the destination path that was created
		try {
			Files.deleteIfExists(localDestPath);
		} catch (NoSuchFileException e) {
			System.err.format("%s: no such" + " file or directory%n", localDestPath);
			e.printStackTrace();
		} catch (DirectoryNotEmptyException e) {

			// If the directory is not empty, that is because it was a move command
			// and the moved file is in there. So delete the file first and then
			// delete the directory
			File localDestinationFile = new File(theDestination);
			boolean deleted = deleteLocalDirectory(localDestinationFile);

			// Something went wrong and couldn't be deleted
			if (!deleted) {
				System.err.println(e);
				e.printStackTrace();
			}
		} catch (IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}

	}

	/**
	 * This function creates a remote source file to move/copy
	 * 
	 * @throws Exception
	 */
	public void createRemoteSource() throws Exception {

		ChannelSftp sftpChannel = fileTransferConn.getSftpChannel();

		String remoteDest = "/tmp/remoteFileHandlerSource/";

		// Check if the directory already exists
		SftpATTRS attrs = null;
		try {
			attrs = sftpChannel.lstat(remoteDest);
		} catch (Exception e) {
			System.out.println("Remote directory not found, trying to make it");
		}
		if (attrs == null) {
			// Remote directory doesn't exist, so make it
			// Create a remote source directory
			sftpChannel.mkdir(remoteDest);
		}

		// Create a local source file since JSch doesn't have a way to make a dummy
		// file
		createLocalSource();

		// Get the filename by splitting the path by "/"
		String separator = FileSystems.getDefault().getSeparator();
		if (System.getProperty("os.name").toLowerCase().contains("win"))
			separator += "\\";
		String[] tokens = theSource.split(separator);

		// Get the last index of tokens, which will be the filename
		String filename = tokens[tokens.length - 1];

		// Move it to the remote host
		sftpChannel.put(theSource, remoteDest);

		// Delete the local directory that was created since it is no longer needed
		Path path = Paths.get(theSource);
		try {
			Files.deleteIfExists(path);
		} catch (NoSuchFileException e) {
			// If somehow the directory doesn't exist, indicate so
			System.err.format("%s: somehow this path doesn't exist... no such" + " file or directory%n", path);
			e.printStackTrace();
		}
		// Now set the source file name to the new location at the remote destination
		theSource = remoteDest + filename;

		System.out.println("Moved source file to new remote source destination " + theSource);

	}

	/**
	 * This function deletes the remote source that was created for testing
	 * 
	 * @throws Exception
	 */
	public void deleteRemoteSource() throws Exception {
		// Connect the channel from the connection
		ChannelSftp sftpChannel = fileTransferConn.getSftpChannel();

		// Get the path to the source file
		// Leave this as unix command since the remote system is unix
		String remoteSeparator = "/";
		String[] tokens = theSource.split(remoteSeparator);
		String sourcePath = "";
		// Build the source path
		for (int i = 0; i < tokens.length - 1; i++) {
			sourcePath += tokens[i] + remoteSeparator;
		}

		// Recursively delete the source directory and its contents
		deleteRemoteDirectory(sftpChannel, sourcePath);

		System.out.println("Deleted remote source directory at : " + sourcePath);
	}

	/**
	 * This function creates a remote directory to move/copy a file
	 * 
	 * @throws Exception
	 */
	public void createRemoteDestination() throws Exception {
		// Connect the channel from the connection
		ChannelSftp sftpChannel = fileTransferConn.getSftpChannel();

		String remoteDest = "/tmp/remoteFileHandlerDestination/";

		// Check if the directory already exists
		SftpATTRS attrs = null;
		try {
			attrs = sftpChannel.lstat(remoteDest);
		} catch (Exception e) {
			System.out.println("Remote directory not found, trying to make it");
		}
		if (attrs == null) {
			// Remote directory doesn't exist, so make it
			// Create a remote source directory
			sftpChannel.mkdir(remoteDest);
		}

		// Set the global variable
		theDestination = remoteDest;

		System.out.println("Created remote destination at : " + theDestination);

	}

	/**
	 * This function deletes the remote destination that was created for testing
	 * 
	 * @throws Exception
	 */
	public void deleteRemoteDestination() throws Exception {
		// Connect the channel from the connection
		ChannelSftp sftpChannel = fileTransferConn.getSftpChannel();

		System.out.println("Deleting remote destination at : " + theDestination);
		// Recursively delete the directory and its contents
		deleteRemoteDirectory(sftpChannel, theDestination);

	}

	/**
	 * A simple test method to recursively delete temporary files/directories
	 * created in this test class
	 * 
	 * @param directory - top level directory from which to delete everything
	 *                  underneath
	 * @return - boolean - true if everything deleted, false if not
	 */
	private boolean deleteLocalDirectory(File directory) {
		File[] contents = directory.listFiles();
		if (contents != null) {
			for (File file : contents) {
				deleteLocalDirectory(file);
			}
		}
		return directory.delete();
	}

	/**
	 * Recurisve function that deletes a remote directory and its contents
	 * 
	 * @param sftpChannel
	 * @param path
	 * @throws SftpException
	 * @throws JSchException
	 */
	private void deleteRemoteDirectory(ChannelSftp sftpChannel, String path) throws SftpException, JSchException {

		// Get the path's directory structure
		Collection<ChannelSftp.LsEntry> fileList = sftpChannel.ls(path);

		// Iterate through the list to get the file/directory names
		for (ChannelSftp.LsEntry file : fileList) {
			// If it isn't a directory delete it
			if (!file.getAttrs().isDir()) {
				// Can use / here because we know the dummy directory is on linux, not windows
				sftpChannel.rm(path + "/" + file.getFilename());
			} else if (!(".".equals(file.getFilename()) || "..".equals(file.getFilename()))) { // If it is a subdir.
				// Otherwise its a subdirectory, so try deleting it
				try {
					// remove the sub directory
					sftpChannel.rmdir(path + "/" + file.getFilename());
				} catch (Exception e) {
					// If the subdirectory is not empty, then iterate with this
					// subdirectory to remove the contents
					deleteRemoteDirectory(sftpChannel, path + "/" + file.getFilename());
				}
			}
		}
		try {
			sftpChannel.rmdir(path); // delete the parent directory after empty
		} catch (SftpException e) {
			// If the sftp channel can't delete it, just manually rm it with a execution
			// channel
			ChannelExec execChannel = (ChannelExec) fileTransferConn.getSession().openChannel("exec");
			execChannel.setCommand("rm -r " + path);
			execChannel.connect();
			execChannel.disconnect();
		}
	}

	/**
	 * Get the connection so that other classes can utilize the code already written
	 * here
	 * 
	 * @return
	 */
	public Connection getConnection() {
		return fileTransferConn;
	}

	/**
	 * Get the source string so that other classes can utilize the code already
	 * written here
	 * 
	 * @return
	 */
	public String getSource() {
		return theSource;
	}

	/**
	 * Get the destination string so that other classes can utilize the code already
	 * written here
	 * 
	 * @return
	 */
	public String getDestination() {
		return theDestination;
	}

}
