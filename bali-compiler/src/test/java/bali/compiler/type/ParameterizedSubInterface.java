package bali.compiler.type;

import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 28/09/13
 */
@MetaType(Kind.INTERFACE)
public interface ParameterizedSubInterface extends ParameterizedSuperInterface<D> {
}
