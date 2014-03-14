package bali.file;

import bali.Iterable;
import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.annotation.Nullable;
import bali.collection.Collection;

/**
 * User: Richard
 * Date: 09/03/14
 */
@MetaType(Kind.INTERFACE)
public interface Directory extends FileSystemEntry, Iterable<File> {

	@Nullable
	public File getFile(@Name("name") String name);

	@Nullable
	public Directory getDirectory(@Name("name") String name);

	public Collection<Directory> getChildren();

	public Collection<File> getFiles();

	public File createFile(@Name("name") String name);

	public Directory createDirectory(@Name("name") String name);

}
