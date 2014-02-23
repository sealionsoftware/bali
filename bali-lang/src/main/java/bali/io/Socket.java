package bali.io;

import bali.Closable;
import bali.ReaderWriter;
import bali.annotation.MetaType;
import bali.annotation.Kind;

/**
 * User: Richard
 * Date: 06/02/14
 */
@MetaType(Kind.INTERFACE)
public interface Socket extends ReaderWriter, Closable {

}
