package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class Method extends MethodDeclaration {

	private CodeBlock body;
	private Boolean isDeclared = Boolean.FALSE;

	public Method() {}

	public Method(Integer line, Integer character) {
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
