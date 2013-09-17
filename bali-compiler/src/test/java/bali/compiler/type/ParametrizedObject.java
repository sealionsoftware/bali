package bali.compiler.type;

/**
 * User: Richard
 * Date: 12/09/13
 */
public class ParametrizedObject<T extends TypeParamBase<T>> {

	private T getT() {
		return null;
	}

	private void setT(T t) {
		// Doesn't matter
	}

}
