package bali.compiler.type;

import java.util.Collections;

/**
 * User: Richard
 * Date: 21/09/13
 */
public class VanillaSite extends ParametrizedSite {

	public VanillaSite(Type type) {
		super(type, Collections.<Site>emptyList());
	}

	public VanillaSite(Reference<Type> typeReference) {
		super(typeReference, Collections.<Site>emptyList());
	}
}
