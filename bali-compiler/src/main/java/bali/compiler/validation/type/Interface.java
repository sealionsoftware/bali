package bali.compiler.validation.type;

import java.util.List;

/**
 * User: Richard
 * Date: 27/08/13
 */
public class Interface extends MethodDeclaringType {

	public Interface(String className, List<Declaration> parameters, List<Method> methods, List<Site> interfaces) {
		super(className, parameters, methods, interfaces);
	}
}
