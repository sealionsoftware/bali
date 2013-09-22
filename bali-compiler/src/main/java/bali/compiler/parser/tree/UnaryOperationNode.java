package bali.compiler.parser.tree;

import bali.compiler.type.ParametrizedSite;
import bali.compiler.type.Site;
import bali.compiler.type.UnaryOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/07/13
 */
public class UnaryOperationNode extends ExpressionNode {

	private ExpressionNode target;
	private String operator;

	private UnaryOperator resolvedOperator;

	public UnaryOperationNode() {
	}

	public UnaryOperationNode(Integer line, Integer character) {
		super(line, character);
	}

	public ExpressionNode getTarget() {
		return target;
	}

	public void setTarget(ExpressionNode target) {
		this.target = target;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public UnaryOperator getResolvedOperator() {
		return resolvedOperator;
	}

	public void setResolvedOperator(UnaryOperator resolvedOperator) {
		this.resolvedOperator = resolvedOperator;
	}

	public Site getType() {
		return resolvedOperator.getType();
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(target);
		return children;
	}
}
