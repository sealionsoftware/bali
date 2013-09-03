package bali.compiler.validation.type;

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
}
