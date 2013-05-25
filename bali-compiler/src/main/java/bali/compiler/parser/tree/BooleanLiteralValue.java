package bali.compiler.parser.tree;

import bali.Boolean;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class BooleanLiteralValue extends Value {

	private String serialization;

	public BooleanLiteralValue(Integer line, Integer character) {
		super(line, character);
	}

	public String getValueTypeName() {
		return Boolean.class.getName();
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
