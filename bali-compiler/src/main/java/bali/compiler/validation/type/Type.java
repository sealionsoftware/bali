package bali.compiler.validation.type;

import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 28/08/13
 */
public class Type {

	private String className;
	private List<Declaration> parameters;

	public Type(String className, List<Declaration> parameters) {
		this.parameters = parameters;
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public List<Declaration> getParameters() {
		return parameters;
	}


}
