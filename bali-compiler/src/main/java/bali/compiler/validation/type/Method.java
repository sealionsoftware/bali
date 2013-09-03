package bali.compiler.validation.type;

import java.util.List;

/**
 * User: Richard
 * Date: 27/08/13
 */
public class Method extends Declaration {

	private List<Declaration> arguments;

	public Method(String name, Site returnType, List<Declaration> arguments) {
		super(name, returnType);
		this.arguments = arguments;
	}

	public List<Declaration> getArguments() {
		return arguments;
	}
}
