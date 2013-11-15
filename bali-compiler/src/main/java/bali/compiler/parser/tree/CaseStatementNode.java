package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 09/05/13
 */
public class CaseStatementNode extends StatementNode {

	private ExpressionNode condition;
	private ControlExpressionNode body;

	public CaseStatementNode(Integer line, Integer character) {
		super(line, character);
	}

	public ExpressionNode getCondition() {
		return condition;
	}

	public void setCondition(ExpressionNode condition) {
		this.condition = condition;
		children.add(condition);
	}

	public ControlExpressionNode getBody() {
		return body;
	}

	public void setBody(ControlExpressionNode body) {
		this.body = body;
		children.add(body);
	}
}
