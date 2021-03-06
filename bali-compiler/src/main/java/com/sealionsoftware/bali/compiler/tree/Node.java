package com.sealionsoftware.bali.compiler.tree;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {

	private Integer line;
	private Integer character;

	protected List<Node> children = new ArrayList<>();

	protected Node(Integer line, Integer character) {
		this.line = line;
		this.character = character;
	}

	public Integer getLine() {
		return line;
	}

	public Integer getCharacter() {
		return character;
	}

	public List<Node> getChildren() {
		return children;
	}

	public abstract void accept(Visitor visitor);
}
