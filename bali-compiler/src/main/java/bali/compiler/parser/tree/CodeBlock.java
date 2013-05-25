package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class CodeBlock extends Node {

	private List<Statement> statements = new ArrayList<>();

	public CodeBlock() {
	}

	public CodeBlock(Integer line, Integer character) {
		super(line, character);
	}

	public List<Statement> getStatements() {
		return statements;
	}

	public void addStatement(Statement statement){
		statements.add(statement);
	}

	public List<Node> getChildren() {
		return new ArrayList<Node>(statements);
	}
}
