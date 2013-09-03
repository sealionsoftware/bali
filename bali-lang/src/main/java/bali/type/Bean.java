package bali.type;

import bali.Type;
import bali.collection.List;

/**
 * User: Richard
 * Date: 26/08/13
 */
public interface Bean extends Type {

	public List<Declaration> getProperties();

}
