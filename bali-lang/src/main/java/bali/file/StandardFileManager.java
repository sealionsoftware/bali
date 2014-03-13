package bali.file;

import bali.String;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 09/03/14
 */
public class StandardFileManager implements FileManager {

	private static final java.lang.String USER_HOME = System.getProperty("user.home");

	public File getFile(String name) {
		return getFile(convert(name));
	}

	public Directory getDirectory(String name) {
		return getDirectory(convert(name));
	}

	public Directory getHomeDirectory() {
		return getDirectory(USER_HOME);
	}

	public Directory getCurrentDirectory() {
		return getDirectory("");
	}

	private File getFile(java.lang.String name) {
		java.io.File file = new java.io.File(name).getAbsoluteFile();
		if (file.exists() && file.isFile()){
			return new StandardFile(file);
		}
		return null;
	}

	private Directory getDirectory(java.lang.String name) {
		java.io.File file = new java.io.File(name).getAbsoluteFile();
		if (file.exists() && file.isDirectory()){
			return new StandardDirectory(file);
		}
		return null;
	}
}
