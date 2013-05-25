package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class Assignment extends Statement  {

	private String name;
	private Value value;

	public Assignment(Integer line, Integer character) {
		super(line, character);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(value);
		return children;
	}
}
