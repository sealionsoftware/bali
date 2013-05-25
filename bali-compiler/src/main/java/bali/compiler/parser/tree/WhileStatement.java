package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 09/05/13
 */
public class WhileStatement extends Statement {

	private Value condition;
	private CodeBlock body;

	public WhileStatement(Integer line, Integer character) {
		super(line, character);
	}

	public Value getCondition() {
		return condition;
	}

	public void setCondition(Value condition) {
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
