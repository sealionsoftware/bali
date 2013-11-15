package bali.compiler.parser.tree;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class VariableNode extends StatementNode {

	private DeclarationNode declaration;
	private ExpressionNode value;
	private ControlExpressionNode assignable;

	public VariableNode() {
		super();
	}

	public VariableNode(Integer line, Integer character) {
		super(line, character);
	}

	public DeclarationNode getDeclaration() {
		return declaration;
	}

	public void setDeclaration(DeclarationNode declaration) {
		children.add(declaration);
		this.declaration = declaration;
	}

	public ExpressionNode getValue() {
		return value;
	}

	public void setValue(ExpressionNode value) {
		children.add(value);
		this.value = value;
	}

	public ControlExpressionNode getAssignable() {
		return assignable;
	}

	public void setAssignable(ControlExpressionNode assignable) {
		children.add(assignable);
		this.assignable = assignable;
	}
}
