package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class CodeBlockNode extends Node {

	private List<StatementNode> statements = new ArrayList<>();

	public CodeBlockNode() {
	}

	public CodeBlockNode(Integer line, Integer character) {
		super(line, character);
	}

	public List<StatementNode> getStatements() {
		return statements;
	}

	public void addStatement(StatementNode statement){
		statements.add(statement);
	}

	public List<Node> getChildren() {
		return new ArrayList<Node>(statements);
	}
}
