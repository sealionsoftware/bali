package bali.file;

import bali.String;
import bali.Boolean;
import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 06/02/14
 */
@MetaType(Kind.INTERFACE)
public interface FileSystemEntry {

	public String getName();

	public Directory getParent();

	public Boolean isWritable();

	public Boolean isReadable();

	public void delete();

}
