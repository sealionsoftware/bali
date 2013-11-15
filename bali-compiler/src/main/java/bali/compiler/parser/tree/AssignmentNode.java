package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class AssignmentNode extends StatementNode {

	private ReferenceNode reference;
	private ExpressionNode value;
	private ControlExpressionNode assignable;

	private BlockingReference<String> setterName = new BlockingReference<>();

	public AssignmentNode() {
	}

	public AssignmentNode(Integer line, Integer character) {
		super(line, character);
	}

	public ReferenceNode getReference() {
		return reference;
	}

	public void setReference(ReferenceNode reference) {
		this.reference = reference;
		children.add(reference);
	}

	public ExpressionNode getValue() {
		return value;
	}

	public void setSetterName(String setterName) {
		this.setterName.set(setterName);
	}

	public String getSetterName() {
		return setterName.get();
	}

	public void setValue(ExpressionNode value) {
		this.value = value;
		children.add(value);
	}

	public ControlExpressionNode getAssignable() {
		return assignable;
	}

	public void setAssignable(ControlExpressionNode assignable) {
		this.assignable = assignable;
		children.add(assignable);
	}

}
