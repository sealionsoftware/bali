package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class ConditionalBlock extends Node {

	private Value condition;
	private CodeBlock body;

	public ConditionalBlock(Integer line, Integer character) {
		super(line, character);
	}

	public void setCondition(Value condition) {
		this.condition = condition;
	}

	public void setBody(CodeBlock body) {
		this.body = body;
	}

	public Value getCondition() {
		return condition;
	}

	public CodeBlock getBody() {
		return body;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(condition);
		children.add(body);
		return children;
	}
}
