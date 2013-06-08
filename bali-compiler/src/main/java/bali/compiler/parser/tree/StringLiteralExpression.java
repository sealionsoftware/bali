package bali.compiler.parser.tree;

import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class StringLiteralExpression extends Expression {

	private String serialization;

	public StringLiteralExpression(Integer line, Integer character) {
		super(line, character);
	}

	public Type getType() {
		Type t = new Type();
		t.setQualifiedClassName(bali.String.class.getName());
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
