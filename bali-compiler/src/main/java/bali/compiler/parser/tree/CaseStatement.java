package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 09/05/13
 */
public class CaseStatement extends Statement {

	private Expression condition;
	private CodeBlock body;

	public CaseStatement(Integer line, Integer character) {
		super(line, character);
	}

	public Expression getCondition() {
		return condition;
	}

	public void setCondition(Expression condition) {
		this.condition = condition;
	}

	public CodeBlock getBody() {
		return body;
	}

	public void setBody(CodeBlock body) {
		this.body = body;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(condition);
		children.add(body);
		return children;
	}
}
