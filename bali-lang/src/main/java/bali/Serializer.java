package bali;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

/**
 * User: Richard
 * Date: 04/07/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface Serializer<T extends Value> extends Formatter<T>, Parser<T> {

}
