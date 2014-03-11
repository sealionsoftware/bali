package bali.file;

import bali.Closable;
import bali.Iterable;
import bali.String;
import bali.ReaderWriter;
import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 09/03/14
 */
@MetaType(Kind.INTERFACE)
public interface File extends ReaderWriter, Iterable<String>, Closable, FileSystemEntry {

	public void clear();

}
