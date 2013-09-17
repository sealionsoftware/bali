package bali.compiler.type;

import java.util.List;

/**
 * User: Richard
 * Date: 26/08/13
 */
public class Class extends MethodDeclaringType {

	private List<Declaration> parameters;

	public Class(String className,
	             List<Declaration> typeParameters,
	             List<Declaration> parameters,
	             List<Method> methods,
	             List<Site> interfaces) {
		super(className, typeParameters, methods, interfaces);
		this.parameters = parameters;
	}

	public List<Declaration> getParameters() {
		return parameters;
	}

	public boolean isAbstract() {
		return false;
	}
}
