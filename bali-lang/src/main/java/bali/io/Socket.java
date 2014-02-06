package bali.io;

import bali.Closable;
import bali.ReaderWriter;
import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

/**
 * User: Richard
 * Date: 06/02/14
 */
@MetaType(MetaTypes.INTERFACE)
public interface Socket extends ReaderWriter, Closable {

}
