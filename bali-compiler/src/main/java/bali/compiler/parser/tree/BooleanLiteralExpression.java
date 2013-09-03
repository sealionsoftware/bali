package bali.compiler.parser.tree;

import bali.IdentityBoolean;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class BooleanLiteralExpression extends Expression {

	private String serialization;
	private TypeReference type; {
		TypeReference t = new TypeReference();
		t.setClassName(IdentityBoolean.class.getName());
		type = t;
	}

	public BooleanLiteralExpression(Integer line, Integer character) {
		super(line, character);

	}

	public TypeReference getType() {
		return type;
	}

	public void setSerialization(String serialization) {
		this.serialization = serialization;
	}

	public String getSerialization() {
		return serialization;
	}

	public List<Node> getChildren() {
		List<Node> ret = new ArrayList<>();
		ret.add(type);
		return ret;
	}
}
