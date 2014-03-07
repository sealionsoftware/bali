package bali.compiler;

import java.io.InputStream;

/**
 * User: Richard
 * Date: 22/01/14
 */
public class PackageDescription {

	private String name;
	private String sourceFileName;
	private InputStream file;

	public PackageDescription(String name, InputStream file, String sourceFileName) {
		this.name = name;
		this.file = file;
		this.sourceFileName = sourceFileName;
	}

	public String getName() {
		return name;
	}

	public String getSourceFileName() {
		return sourceFileName;
	}

	public InputStream getFile() {
		return file;
	}
}
