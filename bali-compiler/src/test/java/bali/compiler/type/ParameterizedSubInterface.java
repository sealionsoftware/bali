package bali.compiler.type;

import bali.annotation.MetaType;
import bali.annotation.Kind;

/**
 * User: Richard
 * Date: 28/09/13
 */
@MetaType(Kind.INTERFACE)
public interface ParameterizedSubInterface extends ParameterizedSuperInterface<D> {
}
