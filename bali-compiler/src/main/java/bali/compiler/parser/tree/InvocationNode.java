package bali.compiler.parser.tree;

import bali.compiler.validation.type.Site;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class InvocationNode extends ExpressionNode {

	private ExpressionNode target;
	private String method;
	private List<ExpressionNode> arguments = new ArrayList<>();

	private Site returnType;

	public InvocationNode() {
	}

	public InvocationNode(Integer line, Integer character) {
		super(line, character);
	}

	public Site getType() {
		return returnType;
	}

	public void setReturnType(Site returnType) {
		this.returnType = returnType;
	}

	public void setTarget(ExpressionNode target) {
		this.target = target;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void addArgument(ExpressionNode argument) {
		arguments.add(argument);
	}

	public ExpressionNode getTarget() {
		return target;
	}

	public String getMethod() {
		return method;
	}

	public List<ExpressionNode> getArguments() {
		return arguments;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		if (target != null) {
			children.add(target);
		}
		children.addAll(arguments);
		return children;
	}
}
