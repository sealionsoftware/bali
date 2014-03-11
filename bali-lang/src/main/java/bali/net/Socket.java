package bali.net;

import bali.Closable;
import bali.Iterable;
import bali.ReaderWriter;
import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 06/02/14
 */
@MetaType(Kind.INTERFACE)
public interface Socket extends ReaderWriter, Iterable<String>, Closable {

}
