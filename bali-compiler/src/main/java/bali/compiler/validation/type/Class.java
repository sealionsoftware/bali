package bali.compiler.validation.type;

import java.util.List;

/**
 * User: Richard
 * Date: 26/08/13
 */
public class Class extends MethodDeclaringType {

	private List<Declaration> arguments;

	public Class(String className,
	             List<Declaration> parameters,
	             List<Declaration> arguments,
	             List<Method> methods,
	             List<Site> interfaces) {
		super(className, parameters, methods, interfaces);
		this.arguments = arguments;
	}

	public List<Declaration> getArguments() {
		return arguments;
	}

	public boolean isAbstract() {
		return false;
	}
}
