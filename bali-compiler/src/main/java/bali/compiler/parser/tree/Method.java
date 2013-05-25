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
	private List<Variable> declaredVariables = new ArrayList<>();

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

	public void addDeclaredVariable(Variable v){
		declaredVariables.add(v);
	}

	public List<Variable> getDeclaredVariables() {
		return declaredVariables;
	}

	public List<Node> getChildren() {
		List<Node> children = super.getChildren();
		children.add(body);
		return children;
	}
}
