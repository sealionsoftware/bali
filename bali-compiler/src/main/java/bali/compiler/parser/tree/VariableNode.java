package bali.compiler.parser.tree;

import bali.compiler.type.Site;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class VariableNode extends AssignmentNode {

	private DeclarationNode declaration;

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

	public Site getType() {
		return declaration.getType().getSite();
	}
}
