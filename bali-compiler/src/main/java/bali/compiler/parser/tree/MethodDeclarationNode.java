package bali.compiler.parser.tree;

import bali.compiler.type.Method;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class MethodDeclarationNode extends MethodNode {

	private CodeBlockNode body;
	private Method declared;

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

	public Method getDeclared() {
		return declared;
	}

	public void setDeclared(Method declared) {
		this.declared= declared;
	}

}
