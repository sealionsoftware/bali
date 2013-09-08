package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class ListLiteralExpressionNode extends LiteralExpressionNode {

	private List<ExpressionNode> values = new ArrayList<>();

	public ListLiteralExpressionNode(Integer line, Integer character) {
		super(line, character);
	}

	public void addValue(ExpressionNode v) {
		values.add(v);
	}

	public List<ExpressionNode> getValues() {
		return values;
	}

	public List<Node> getChildren() {
		return new ArrayList<Node>(values);
	}
}
