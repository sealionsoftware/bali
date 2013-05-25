package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class Declaration extends Node {

	private Type type;
	private String name;

	public Declaration(){}

	public Declaration(Integer line, Integer character) {
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
		ArrayList<Node> children = new ArrayList<>();
		if (type != null){
			children.add(type);
		}
		return children;
	}
}
