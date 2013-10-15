package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class VariableNode extends StatementNode {

	private DeclarationNode declaration;
	private ExpressionNode value;

	public VariableNode() {
		super();
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

	public VariableNode(Integer line, Integer character) {
		super(line, character);
	}

}
