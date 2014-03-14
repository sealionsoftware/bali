package bali.file;

import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.annotation.Nullable;

/**
 * User: Richard
 * Date: 06/02/14
 */
@MetaType(Kind.INTERFACE)
public interface FileManager {

	@Nullable
	public File getFile(@Name("name") String name);

	@Nullable
	public Directory getDirectory(@Name("name") String name);

	public Directory getHomeDirectory();

	public Directory getCurrentDirectory();

}
