package bali.compiler.parser.tree;

import bali.compiler.validation.type.Site;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 05/05/13
 */
public class ConstructionExpressionNode extends ExpressionNode {

	private String className;
	private List<ExpressionNode> arguments = new ArrayList<>();

	private Site type;

	public ConstructionExpressionNode(Integer line, Integer character) {
		super(line, character);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setType(Site type) {
		this.type = type;
	}

	public void addArgument(ExpressionNode argument) {
		arguments.add(argument);
	}

	public Site getType() {
		return type;
	}

	public List<ExpressionNode> getArguments() {
		return arguments;
	}

	public List<Node> getChildren() {
		return new ArrayList<Node>(arguments);
	}
}
