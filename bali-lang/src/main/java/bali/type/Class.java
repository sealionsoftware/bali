package bali.type;

import bali.Type;
import bali.collection.List;

/**
 * User: Richard
 * Date: 26/08/13
 */
public interface Class extends Type {

	public List<Declaration> getArguments();

	public List<Method> getMethods();

}
