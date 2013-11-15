package bali.compiler.parser.tree;

/**
 * User: Richard
 * Date: 05/05/13
 */
public class ThrowStatementNode extends StatementNode {

	private ExpressionNode value;

	public ThrowStatementNode() {
	}

	public ThrowStatementNode(Integer line, Integer character) {
		super(line, character);
	}

	public void setValue(ExpressionNode value) {
		children.add(value);
		this.value = value;
	}

	public ExpressionNode getValue() {
		return value;
	}

}
