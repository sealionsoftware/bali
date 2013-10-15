package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.type.Site;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 05/05/13
 */
public class ConstructionExpressionNode extends ExpressionNode {

	private String className;
	private List<ExpressionNode> arguments = new ArrayList<>();

	private BlockingReference<Site> type = new BlockingReference<>();

	public ConstructionExpressionNode(Integer line, Integer character) {
		super(line, character);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setType(Site type) {
		this.type.set(type);
	}

	public void addArgument(ExpressionNode argument) {
		children.add(argument);
		arguments.add(argument);
	}

	public Site getType() {
		return type.get();
	}

	public List<ExpressionNode> getArguments() {
		return arguments;
	}

}
