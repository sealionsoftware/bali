package bali.file;

import bali.Boolean;
import bali.Iterator;
import bali.String;
import bali.collection.Collection;
import bali.collection.LinkedList;
import bali.collection.List;

import java.io.IOException;

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
		List<Directory> ret = new LinkedList<>(null);
		java.io.File[] children = delegate.listFiles();
		if (children == null){
			return ret;
		}
		for (java.io.File child : children){
			if (child.isDirectory()){
				ret.add(new StandardDirectory(child));
			}
		}
		return ret;
	}

	public Collection<File> getFiles() {
		List<File> ret = new LinkedList<>(null);
		java.io.File[] children = delegate.listFiles();
		if (children == null){
			return ret;
		}
		for (java.io.File child : children){
			if (child.isFile()){
				ret.add(new StandardFile(child));
			}
		}
		return ret;
	}

	public File createFile(String name) {
		java.io.File newFile = new java.io.File(delegate, convert(name));
		try {
			if (!newFile.createNewFile()){
				throw new RuntimeException("Directory already contains a child named " + name);
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not create file", e);
		}
		return new StandardFile(newFile);
	}

	public Directory createDirectory(String name) {
		java.io.File newDirectory = new java.io.File(delegate, convert(name));
		if (newDirectory.exists()){
			throw new RuntimeException("Directory already contains a child named " + name);
		}
		newDirectory.mkdir();
		return new StandardDirectory(newDirectory);
	}

	public File getFile(String name) {
		java.io.File file = new java.io.File(delegate, convert(name));
		if (file.exists() && file.isFile()){
			return new StandardFile(file);
		}
		return null;
	}

	public Directory getDirectory(String name) {
		java.io.File dir = new java.io.File(delegate, convert(name));
		if (dir.exists() && dir.isDirectory()){
			return new StandardDirectory(dir);
		}
		return null;
	}

	public String getPath() {
		return convert(delegate.getPath());
	}

	public String getName() {
		return convert(delegate.getName());
	}

	public Directory getParent() {
		java.io.File parent = delegate.getParentFile();
		if (parent == null){
			return null;
		}
		return new StandardDirectory(parent);
	}

	public Boolean isWritable() {
		return convert(delegate.canWrite());
	}

	public Boolean isReadable() {
		return convert(delegate.canRead());
	}

	public void delete() {
		if (!delegate.delete()){
			throw new RuntimeException("Could not delete directory");
		}
	}

	public Iterator<File> iterator() {
		return getFiles().iterator();
	}
}
