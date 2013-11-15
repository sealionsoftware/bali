package bali.compiler.parser.tree;

/**
 * User: Richard
 * Date: 25/06/13
 */
public class CatchStatementNode extends Node {

	private DeclarationNode declaration;
	private ControlExpressionNode body;

	public CatchStatementNode() {
	}

	public CatchStatementNode(Integer line, Integer character) {
		super(line, character);
	}

	public DeclarationNode getDeclaration() {
		return declaration;
	}

	public void setDeclaration(DeclarationNode declaration) {
		this.declaration = declaration;
		children.add(declaration);
	}

	public ControlExpressionNode getBody() {
		return body;
	}

	public void setBody(ControlExpressionNode body) {
		this.body = body;
		children.add(body);
	}

}
