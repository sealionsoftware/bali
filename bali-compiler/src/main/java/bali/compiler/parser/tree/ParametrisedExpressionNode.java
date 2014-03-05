package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 05/05/13
 */
public abstract class ParametrisedExpressionNode extends ExpressionNode {

	private List<ArgumentNode> arguments = new ArrayList<>();

	private BlockingReference<List<ExpressionNode>> resolvedArguments = new BlockingReference<>();

	protected ParametrisedExpressionNode() {
	}

	public ParametrisedExpressionNode(Integer line, Integer character) {
		super(line, character);
	}

	public void addArgument(ArgumentNode argument) {
		children.add(argument);
		arguments.add(argument);
	}

	public List<ArgumentNode> getArguments() {
		return arguments;
	}

	public List<ExpressionNode> getResolvedArguments() {
		return resolvedArguments.get();
	}

	public void setResolvedArguments(List<ExpressionNode> resolvedArguments) {
		this.resolvedArguments.set(resolvedArguments);
	}
}
