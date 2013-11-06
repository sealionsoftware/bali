package bali;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.collection.List;

/**
 * User: Richard
 * Date: 26/08/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface Type {

	public String getClassName();

	public List<Type> getParameters();

}
