package bali.file;

import bali.Boolean;
import bali.Iterator;
import bali.String;
import bali.collection.Collection;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 12/03/14
 */
public class StandardDirectory implements Directory {

	private java.io.File delegate;

	public StandardDirectory(java.io.File delegate) {
		this.delegate = delegate;
	}

	public Collection<Directory> getChildren() {
		return null;
	}

	public Collection<File> getFiles() {
		return null;
	}

	public File createFile(String name) {
		return null;
	}

	public Directory createDirectory(String name) {
		return null;
	}

	public File getFile(String name) {
		return null;
	}

	public Directory getDirectory(String name) {
		return null;
	}

	public String getPath() {
		return convert(delegate.getPath());
	}

	public String getName() {
		return null;
	}

	public Directory getParent() {
		return null;
	}

	public Boolean isWritable() {
		return null;
	}

	public Boolean isReadable() {
		return null;
	}

	public void delete() {
		delegate.delete();
	}

	public Iterator<File> iterator() {
		return getFiles().iterator();
	}
}
