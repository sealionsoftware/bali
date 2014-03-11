package bali.file;

import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 06/02/14
 */
@MetaType(Kind.INTERFACE)
public interface FileManager {

	public File getFile(String name);

	public Directory getDirectory(String name);

}
