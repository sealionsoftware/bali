package bali.compiler.parser.tree;

import bali.CharArrayString;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class StringLiteralExpression extends Expression {

	private String serialization;
	private TypeReference type;{
		TypeReference t = new TypeReference();
		t.setClassName(CharArrayString.class.getName());
		type = t;
	}

	public StringLiteralExpression() {
	}

	public StringLiteralExpression(Integer line, Integer character) {
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
