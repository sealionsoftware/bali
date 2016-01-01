package com.sealionsoftware.bali.compiler.tree;

import java.util.ArrayList;
import java.util.List;

public class CodeBlockNode extends StatementNode {

	private List<StatementNode> statements = new ArrayList<>();

	public CodeBlockNode(Integer line, Integer character) {
		super(line, character);
	}

	public List<StatementNode> getStatements() {
		return statements;
	}

	public void addStatement(StatementNode statement){
		statements.add(statement);
		children.add(statement);
	}

	public void accept(Visitor visitor) {
        visitor.visit(this);
	}
}
