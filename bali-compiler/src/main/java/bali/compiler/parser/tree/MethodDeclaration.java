package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class MethodDeclaration extends Declaration {

	private String operator;
	private List<Declaration> arguments = new ArrayList<>();

	public MethodDeclaration() {
	}

	public MethodDeclaration(Integer line, Integer character) {
		super(line, character);
	}

	public List<Declaration> getArguments() {
		return arguments;
	}

	public void addArgument(Declaration argument) {
		this.arguments.add(argument);
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public List<Node> getChildren() {
		List<Node> ret = super.getChildren();
		ret.addAll(arguments);
		return ret;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass().isAssignableFrom(o.getClass())) return false;

		MethodDeclaration that = (MethodDeclaration) o;

		if (!arguments.equals(that.arguments)) return false;

		return true;
	}

	public String toString() {
		return getName() + "(" + arguments + ")";
	}

}
