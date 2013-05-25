package bali.compiler.parser.tree;

import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class StringLiteralValue extends Value {

	private String serialization;

	public StringLiteralValue(Integer line, Integer character) {
		super(line, character);
	}

	public String getValueTypeName() {
		return bali.String.class.getName();
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
