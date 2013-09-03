package bali.type;

import bali.Type;
import bali.collection.List;

/**
 * User: Richard
 * Date: 26/08/13
 */
public interface Method {

	public Type getReturnType();

	public List<Declaration> getArguments();
}
