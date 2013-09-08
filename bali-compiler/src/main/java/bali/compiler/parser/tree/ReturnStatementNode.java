package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 05/05/13
 */
public class ReturnStatementNode extends StatementNode {

	private ExpressionNode value;

	public ReturnStatementNode() {
	}

	public ReturnStatementNode(Integer line, Integer character) {
		super(line, character);
	}

	public void setValue(ExpressionNode value) {
		this.value = value;
	}

	public ExpressionNode getValue() {
		return value;
	}

	public List<Node> getChildren() {
		return value != null ? Collections.singletonList((Node) value) : new ArrayList<Node>();
	}
}
