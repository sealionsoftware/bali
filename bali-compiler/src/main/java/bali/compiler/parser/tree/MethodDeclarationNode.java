package bali.compiler.parser.tree;

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
		children.add(body);
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

}
