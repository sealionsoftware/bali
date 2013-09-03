package bali.compiler.parser.tree;

import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class MethodDeclaration extends Method {

	private CodeBlock body;
	private Boolean isDeclared = Boolean.FALSE;

	public MethodDeclaration() {}

	public MethodDeclaration(Integer line, Integer character) {
		super(line, character);
	}

	public void setBody(CodeBlock body) {
		this.body = body;
	}

	public CodeBlock getBody() {
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
