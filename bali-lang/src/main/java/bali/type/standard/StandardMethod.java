package bali.type.standard;

import bali.Type;
import bali.String;
import bali.collection.List;
import bali.type.Declaration;
import bali.type.Method;

/**
 * User: Richard
 * Date: 27/08/13
 */
public class StandardMethod implements Method {

	private String name;
	private Type returnType;
	private List<Declaration> arguments;

	public StandardMethod(String name, Type returnType, List<Declaration> arguments) {
		this.name = name;
		this.returnType = returnType;
		this.arguments = arguments;
	}

	public String getName() {
		return name;
	}

	public Type getReturnType() {
		return returnType;
	}

	public List<Declaration> getArguments() {
		return arguments;
	}
}
