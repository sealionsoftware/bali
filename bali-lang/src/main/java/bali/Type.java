package bali;

import bali.collection.List;

/**
 * User: Richard
 * Date: 26/08/13
 */
public interface Type {

	public String getClassName();

	public List<Type> getParameters();

}
