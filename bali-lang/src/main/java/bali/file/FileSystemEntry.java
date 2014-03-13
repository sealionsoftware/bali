package bali.file;

import bali.Boolean;
import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Nullable;

/**
 * User: Richard
 * Date: 06/02/14
 */
@MetaType(Kind.INTERFACE)
public interface FileSystemEntry {

	public String getPath();

	public String getName();

	@Nullable
	public Directory getParent();

	public Boolean isWritable();

	public Boolean isReadable();

	public void delete();

}
