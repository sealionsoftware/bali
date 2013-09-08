package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public abstract class DeclarationNode extends Node {

	private SiteNode type;
	private String name;

	public DeclarationNode() {
	}

	public DeclarationNode(Integer line, Integer character) {
		super(line, character);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public SiteNode getType() {
		return type;
	}

	public void setType(SiteNode type) {
		this.type = type;
	}

	public abstract Boolean getFinal();

	public List<Node> getChildren() {
		ArrayList<Node> children = new ArrayList<>();
		if (type != null) {
			children.add(type);
		}
		return children;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DeclarationNode that = (DeclarationNode) o;

		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (type != null ? !type.equals(that.type) : that.type != null) return false;

		return true;
	}

	public String toString() {
		String ret = type.toString();
		if (name != null) {
			ret += " " + name;
		}
		return ret;
	}
}
