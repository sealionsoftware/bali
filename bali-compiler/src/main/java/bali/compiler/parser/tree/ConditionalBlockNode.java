package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class ConditionalBlockNode extends Node {

	private ExpressionNode condition;
	private CodeBlockNode body;

	public ConditionalBlockNode(Integer line, Integer character) {
		super(line, character);
	}

	public void setCondition(ExpressionNode condition) {
		this.condition = condition;
	}

	public void setBody(CodeBlockNode body) {
		this.body = body;
	}

	public ExpressionNode getCondition() {
		return condition;
	}

	public CodeBlockNode getBody() {
		return body;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(condition);
		children.add(body);
		return children;
	}
}