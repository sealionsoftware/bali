package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class ArrayLiteralExpressionNode extends LiteralExpressionNode {

	private List<ExpressionNode> values = new ArrayList<>();

	public ArrayLiteralExpressionNode(Integer line, Integer character) {
		super(line, character);
	}

	public void addValue(ExpressionNode v) {
		values.add(v);
		children.add(v);
	}

	public List<ExpressionNode> getValues() {
		return values;
	}
}
