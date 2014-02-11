package bali.compiler.type;

/**
 * User: Richard
 * Date: 27/08/13
 */
public class Declaration {

	private String name;
	private Site type;

	public Declaration(String name, Site type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public Site getType() {
		return type;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Declaration that = (Declaration) o;

		if (!name.equals(that.name)) return false;
		if (!type.equals(that.type)) return false;

		return true;
	}

	public String toString() {
		return name;
	}
}
