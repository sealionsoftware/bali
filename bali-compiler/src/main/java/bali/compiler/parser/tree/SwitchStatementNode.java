package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 09/05/13
 */
public class SwitchStatementNode extends ControlExpressionNode {

	private ExpressionNode value;
	private List<CaseStatementNode> caseStatements = new ArrayList<>();
	private ControlExpressionNode defaultStatement;

	public SwitchStatementNode(Integer line, Integer character) {
		super(line, character);
	}

	public ExpressionNode getValue() {
		return value;
	}

	public void setValue(ExpressionNode value) {
		children.add(value);
		this.value = value;
	}

	public List<CaseStatementNode> getCaseStatements() {
		return caseStatements;
	}

	public void addCaseStatement(CaseStatementNode caseStatement) {
		children.add(caseStatement);
		this.caseStatements.add(caseStatement);
	}

	public ControlExpressionNode getDefaultStatement() {
		return defaultStatement;
	}

	public void setDefaultStatement(ControlExpressionNode defaultStatement) {
		children.add(defaultStatement);
		this.defaultStatement = defaultStatement;
	}

}
