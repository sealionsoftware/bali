package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.type.Site;
import bali.compiler.type.UnaryOperator;

/**
 * User: Richard
 * Date: 02/07/13
 */
public class UnaryOperationNode extends ExpressionNode {

	private ExpressionNode target;
	private String operator;

	private BlockingReference<UnaryOperator> resolvedOperator = new BlockingReference<>();

	public UnaryOperationNode() {
	}

	public UnaryOperationNode(Integer line, Integer character) {
		super(line, character);
	}

	public ExpressionNode getTarget() {
		return target;
	}

	public void setTarget(ExpressionNode target) {
		children.add(target);
		this.target = target;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public UnaryOperator getResolvedOperator() {
		return resolvedOperator.get();
	}

	public void setResolvedOperator(UnaryOperator resolvedOperator) {
		this.resolvedOperator.set(resolvedOperator);
	}

	public Site getType() {
		return resolvedOperator.get().getType();
	}

}
