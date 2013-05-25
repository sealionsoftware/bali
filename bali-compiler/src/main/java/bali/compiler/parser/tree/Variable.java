package bali.compiler.parser.tree;

import org.objectweb.asm.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class Variable extends Assignment  {

	private Type type;

	public Variable(Integer line, Integer character) {
		super(line, character);
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<Node> getChildren() {
		List<Node> children = super.getChildren();
		children.add(type);
		return children;
	}

}
