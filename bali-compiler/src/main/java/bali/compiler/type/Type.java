package bali.compiler.type;

import java.util.List;

/**
 * User: Richard
 * Date: 28/08/13
 */
public abstract class Type {

	private String className;
	private List<Declaration> typeParameters;

	public Type(String className, List<Declaration> typeParameters) {
		this.typeParameters = typeParameters;
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public List<Declaration> getTypeParameters() {
		return typeParameters;
	}

	public abstract boolean isAbstract();

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Type type = (Type) o;

		if (!className.equals(type.className)) return false;
		if (!typeParameters.equals(type.typeParameters)) return false;

		return true;
	}

	public int hashCode() {
		int result = className.hashCode();
		result = 31 * result + typeParameters.hashCode();
		return result;
	}
}
