package bali.compiler.parser.tree;

import bali.Boolean;

import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class BooleanLiteralExpression extends Expression {

	private String serialization;

	public BooleanLiteralExpression(Integer line, Integer character) {
		super(line, character);
	}

	public Type getType() {
		Type t = new Type();
		t.setQualifiedClassName(Boolean.class.getName());
		return t;
	}

	public void setSerialization(String serialization) {
		this.serialization = serialization;
	}

	public String getSerialization() {
		return serialization;
	}

	public List<Node> getChildren() {
		return Collections.emptyList();
	}
}
