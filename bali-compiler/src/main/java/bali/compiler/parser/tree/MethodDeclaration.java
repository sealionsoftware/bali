package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class MethodDeclaration extends Declaration {

	private List<Declaration> arguments = new ArrayList<>();

	public MethodDeclaration() {}

	public MethodDeclaration(Integer line, Integer character) {
		super(line, character);
	}

	public List<Declaration> getArguments() {
		return arguments;
	}

	public void addArgument(Declaration argument) {
		this.arguments.add(argument);
	}

	public List<Node> getChildren() {
		List<Node> ret = super.getChildren();
		ret.addAll(arguments);
		return ret;
	}
}
