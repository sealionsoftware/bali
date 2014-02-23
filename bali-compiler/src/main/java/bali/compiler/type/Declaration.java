package bali.compiler.type;

/**
 * User: Richard
 * Date: 27/08/13
 */
public class Declaration<T extends Type> {

	private String name;
	private T type;

	public Declaration(String name, T type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public T getType() {
		return type;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Declaration that = (Declaration) o;

		if (!name.equals(that.name)) return false;

		return true;
	}

	public String toString() {
		return name;
	}
}
