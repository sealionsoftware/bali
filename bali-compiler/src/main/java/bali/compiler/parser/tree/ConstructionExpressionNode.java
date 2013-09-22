package bali.compiler.parser.tree;

import bali.compiler.type.ParametrizedSite;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 05/05/13
 */
public class ConstructionExpressionNode extends ExpressionNode {

	private String className;
	private List<ExpressionNode> arguments = new ArrayList<>();

	private ParametrizedSite type;

	public ConstructionExpressionNode(Integer line, Integer character) {
		super(line, character);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setType(ParametrizedSite type) {
		this.type = type;
	}

	public void addArgument(ExpressionNode argument) {
		arguments.add(argument);
	}

	public ParametrizedSite getType() {
		return type;
	}

	public List<ExpressionNode> getArguments() {
		return arguments;
	}

	public List<Node> getChildren() {
		return new ArrayList<Node>(arguments);
	}
}
