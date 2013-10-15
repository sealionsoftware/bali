package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.type.Operator;
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

	private BlockingReference<Operator> resolvedOperator = new BlockingReference<>();

	public OperationNode() {
	}

	public OperationNode(Integer line, Integer character) {
		super(line, character);
	}

	public void setOne(ExpressionNode one) {
		children.add(one);
		this.one = one;
	}

	public void setTwo(ExpressionNode two) {
		children.add(two);
		this.two = two;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Operator getResolvedOperator() {
		return resolvedOperator.get();
	}

	public void setResolvedOperator(Operator resolvedOperator) {
		this.resolvedOperator.set(resolvedOperator);
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
		return resolvedOperator.get().getType();
	}

	public String toString() {
		return one + " " + operator + " " + two;
	}
}
