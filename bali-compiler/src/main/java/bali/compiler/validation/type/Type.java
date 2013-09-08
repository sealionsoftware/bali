package bali.compiler.validation.type;

import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 28/08/13
 */
public abstract class Type {

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

	public abstract boolean isAbstract();

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Type type = (Type) o;

		if (!className.equals(type.className)) return false;
		if (!parameters.equals(type.parameters)) return false;

		return true;
	}

	public int hashCode() {
		int result = className.hashCode();
		result = 31 * result + parameters.hashCode();
		return result;
	}
}
