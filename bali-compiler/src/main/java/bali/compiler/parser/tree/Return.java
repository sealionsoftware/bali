package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 05/05/13
 */
public class Return extends Statement {

	private Value value;

	public Return() {}

	public Return(Integer line, Integer character) {
		super(line, character);
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public Value getValue() {
		return value;
	}

	public List<Node> getChildren() {
		return value != null ? Collections.singletonList((Node) value) : new ArrayList<Node>();
	}
}
