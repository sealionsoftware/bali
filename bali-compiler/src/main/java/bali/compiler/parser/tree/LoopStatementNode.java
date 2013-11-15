package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 08/07/13
 */
public abstract class LoopStatementNode extends ControlExpressionNode {

	protected ControlExpressionNode body;

	public LoopStatementNode(Integer line, Integer character) {
		super(line, character);
	}

	public ControlExpressionNode getBody() {
		return body;
	}

	public void setBody(ControlExpressionNode body) {
		this.body = body;
		children.add(body);
	}

}
