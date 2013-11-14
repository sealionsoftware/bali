package bali;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.annotation.Monitor;

/**
 * User: Richard
 * Date: 04/07/13
 */
@Monitor
@MetaType(MetaTypes.INTERFACE)
public interface Serializer<T extends Value> extends Formatter<T>, Parser<T> {

}
