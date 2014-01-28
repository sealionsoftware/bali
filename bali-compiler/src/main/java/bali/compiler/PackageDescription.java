package bali.compiler;

import java.io.InputStream;

/**
 * User: Richard
 * Date: 22/01/14
 */
public class PackageDescription {

	public PackageDescription(String name, InputStream file) {
		this.name = name;
		this.file = file;
	}

	public String name;
	public InputStream file;
}
