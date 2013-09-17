package bali.compiler.parser.tree;

import bali.compiler.type.Type;

import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class ImportNode extends Node {

	private String name;
	private Type type;

	public ImportNode() {
	}

	public ImportNode(Integer line, Integer character) {
		super(line, character);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<Node> getChildren() {
		return Collections.emptyList();
	}
}
