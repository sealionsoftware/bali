package bali.compiler.parser.tree;

import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class MethodDeclarationNode extends MethodNode {

	private CodeBlockNode body;
	private Boolean isDeclared = Boolean.FALSE;

	public MethodDeclarationNode() {}

	public MethodDeclarationNode(Integer line, Integer character) {
		super(line, character);
	}

	public void setBody(CodeBlockNode body) {
		this.body = body;
	}

	public CodeBlockNode getBody() {
		return body;
	}

	public Boolean getDeclared() {
		return isDeclared;
	}

	public void setDeclared(Boolean declared) {
		isDeclared = declared;
	}

	public List<Node> getChildren() {
		List<Node> children = super.getChildren();
		children.add(body);
		return children;
	}
}
