package bali.compiler.type;

import bali.annotation.Name;

/**
 * User: Richard
 * Date: 12/09/13
 */
public class ParametrizedObject<T extends B<T>> {

	public T getT() {
		return null;
	}

	public void setT(@Name("t") T t) {
		// Doesn't matter
	}

}
