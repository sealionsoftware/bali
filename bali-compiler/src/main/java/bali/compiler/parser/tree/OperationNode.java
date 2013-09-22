package bali.compiler.parser.tree;

import bali.compiler.type.Operator;
import bali.compiler.type.ParametrizedSite;
import bali.compiler.type.Site;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/07/13
 */
public class OperationNode extends ExpressionNode {

	private ExpressionNode one;
	private ExpressionNode two;
	private String operator;

	private Operator resolvedOperator;

	public OperationNode() {
	}

	public OperationNode(Integer line, Integer character) {
		super(line, character);
	}

	public void setOne(ExpressionNode one) {
		this.one = one;
	}

	public void setTwo(ExpressionNode two) {
		this.two = two;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Operator getResolvedOperator() {
		return resolvedOperator;
	}

	public void setResolvedOperator(Operator resolvedOperator) {
		this.resolvedOperator = resolvedOperator;
	}

	public ExpressionNode getOne() {
		return one;
	}

	public ExpressionNode getTwo() {
		return two;
	}

	public String getOperator() {
		return operator;
	}

	public Site getType() {
		return resolvedOperator.getType();
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(one);
		children.add(two);
		return children;
	}
}
