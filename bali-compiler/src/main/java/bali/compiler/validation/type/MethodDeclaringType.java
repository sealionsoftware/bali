package bali.compiler.validation.type;

import java.util.List;

/**
 * User: Richard
 * Date: 27/08/13
 */
public abstract class MethodDeclaringType extends Type {

	private List<Method> methods;
	private List<Site> interfaces;

	public MethodDeclaringType(String className, List<Declaration> parameters, List<Method> methods, List<Site> interfaces) {
		super(className, parameters);
		this.interfaces = interfaces;
		this.methods = methods;
	}

	public List<Site> getInterfaces() {
		return interfaces;
	}

	public List<Method> getMethods() {
		return methods;
	}



}
