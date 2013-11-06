package bali.compiler.type;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

/**
 * User: Richard
 * Date: 28/09/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface ParameterizedSubInterface extends ParameterizedSuperInterface<D> {
}
