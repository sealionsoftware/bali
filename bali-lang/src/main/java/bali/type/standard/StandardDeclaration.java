package bali.type.standard;

import bali.Type;
import bali.type.Declaration;

/**
 * User: Richard
 * Date: 27/08/13
 */
public class StandardDeclaration implements Declaration {

	private String name;
	private Type type;

	public StandardDeclaration(String name, Type type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}
}
