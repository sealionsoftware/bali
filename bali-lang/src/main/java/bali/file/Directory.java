package bali.file;

import bali.Iterable;
import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.collection.Collection;

/**
 * User: Richard
 * Date: 09/03/14
 */
@MetaType(Kind.INTERFACE)
public interface Directory extends FileSystemEntry, Iterable<File> {

	public File getFile(String name);

	public Directory getDirectory(String name);

	public Collection<Directory> getChildren();

	public Collection<File> getFiles();

	public File createFile(String name);

	public Directory createDirectory(String name);

}
