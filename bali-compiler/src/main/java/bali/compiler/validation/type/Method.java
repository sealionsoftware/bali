package bali.compiler.validation.type;

import java.util.List;

/**
 * User: Richard
 * Date: 27/08/13
 */
public class Method extends Declaration {

	private List<Declaration> parameters;

	public Method(String name, Site returnType, List<Declaration> parameters) {
		super(name, returnType);
		this.parameters = parameters;
	}

	public List<Declaration> getParameters() {
		return parameters;
	}
}
