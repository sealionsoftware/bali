package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 09/05/13
 */
public class SwitchStatement extends Statement {

	private Expression value;
	private List<CaseStatement> caseStatements = new ArrayList<>();
	private CodeBlock defaultStatement;

	public SwitchStatement(Integer line, Integer character) {
		super(line, character);
	}

	public Expression getValue() {
		return value;
	}

	public void setValue(Expression value) {
		this.value = value;
	}

	public List<CaseStatement> getCaseStatements() {
		return caseStatements;
	}

	public void addCaseStatement(CaseStatement caseStatement) {
		this.caseStatements.add(caseStatement);
	}

	public CodeBlock getDefaultStatement() {
		return defaultStatement;
	}

	public void setDefaultStatement(CodeBlock defaultStatement) {
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
