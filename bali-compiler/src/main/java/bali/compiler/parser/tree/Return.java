package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 05/05/13
 */
public class Return extends Statement {

	private Expression value;

	public Return() {
	}

	public Return(Integer line, Integer character) {
		super(line, character);
	}

	public void setValue(Expression value) {
		this.value = value;
	}

	public Expression getValue() {
		return value;
	}

	public List<Node> getChildren() {
		return value != null ? Collections.singletonList((Node) value) : new ArrayList<Node>();
	}
}
