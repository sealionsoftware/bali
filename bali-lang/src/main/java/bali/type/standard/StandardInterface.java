package bali.type.standard;

import bali.*;
import bali.String;
import bali.collection.List;
import bali.type.Declaration;
import bali.type.Interface;
import bali.type.Method;

/**
 * User: Richard
 * Date: 27/08/13
 */
public class StandardInterface implements Interface {

	private List<Method> methods;
	private String className;
	private List<Type> parameters;

	public StandardInterface(List<Method> methods, String className, List<Type> parameters) {
		this.methods = methods;
		this.className = className;
		this.parameters = parameters;
	}

	public List<Method> getMethods() {
		return methods;
	}

	public bali.String getClassName() {
		return className;
	}

	public List<Type> getParameters() {
		return parameters;
	}
}
