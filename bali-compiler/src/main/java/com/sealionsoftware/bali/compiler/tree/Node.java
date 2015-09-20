package com.sealionsoftware.bali.compiler.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 08/05/13
 */
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

	public void accept(Visitor visitor){
        for (Node child : children){
            child.accept(visitor);
        }
    }
}
