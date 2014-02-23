package bali.compiler.type;

import bali.annotation.MetaType;
import bali.annotation.Kind;
import bali.annotation.Name;

/**
 * User: Richard
 * Date: 12/09/13
 */
@MetaType(Kind.OBJECT)
public class ParametrizedObject<T extends B<T>> {

	public T getT() {
		return null;
	}

	public void setT(@Name("t") T t) {
		// Doesn't matter
	}

}
