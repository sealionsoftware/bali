package bali.compiler.parser.tree;

/**
 * User: Richard
 * Date: 15/11/13
 */
public class AssignStatementNode extends StatementNode {

	private ExpressionNode value;

	public AssignStatementNode() {
	}

	public AssignStatementNode(Integer line, Integer character) {
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
