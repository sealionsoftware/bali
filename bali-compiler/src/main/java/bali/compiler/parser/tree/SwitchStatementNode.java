package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 09/05/13
 */
public class SwitchStatementNode extends StatementNode {

	private ExpressionNode value;
	private List<CaseStatementNode> caseStatements = new ArrayList<>();
	private CodeBlockNode defaultStatement;

	public SwitchStatementNode(Integer line, Integer character) {
		super(line, character);
	}

	public ExpressionNode getValue() {
		return value;
	}

	public void setValue(ExpressionNode value) {
		this.value = value;
	}

	public List<CaseStatementNode> getCaseStatements() {
		return caseStatements;
	}

	public void addCaseStatement(CaseStatementNode caseStatement) {
		this.caseStatements.add(caseStatement);
	}

	public CodeBlockNode getDefaultStatement() {
		return defaultStatement;
	}

	public void setDefaultStatement(CodeBlockNode defaultStatement) {
		this.defaultStatement = defaultStatement;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(value);
		children.addAll(caseStatements);
		if (defaultStatement != null){
			children.add(defaultStatement);
		}
		return children;
	}
}
