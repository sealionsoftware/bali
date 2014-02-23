package bali;

import bali.annotation.MetaType;
import bali.annotation.Kind;

/**
 * User: Richard
 * Date: 04/07/13
 */
@MetaType(Kind.INTERFACE)
public interface Serializer<T extends Value> extends Formatter<T>, Parser<T> {

}
