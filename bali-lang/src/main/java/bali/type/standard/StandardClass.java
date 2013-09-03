package bali.type.standard;

import bali.String;
import bali.Type;
import bali.collection.List;
import bali.type.Class;
import bali.type.Declaration;
import bali.type.Method;

/**
 * User: Richard
 * Date: 26/08/13
 */
public class StandardClass implements Class {

	private List<Declaration> arguments;
	private List<Method> methods;
	private String className;
	private List<Type> parameters;

	public StandardClass(String className, List<Type> parameters, List<Declaration> arguments, List<Method> methods) {
		this.arguments = arguments;
		this.methods = methods;
		this.className = className;
		this.parameters = parameters;
	}

	public List<Declaration> getArguments() {
		return arguments;
	}

	public List<Method> getMethods() {
		return methods;
	}

	public String getClassName() {
		return className;
	}

	public List<Type> getParameters() {
		return parameters;
	}
}
