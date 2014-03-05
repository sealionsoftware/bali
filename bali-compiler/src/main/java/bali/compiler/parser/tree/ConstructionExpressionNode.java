package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.type.Site;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 05/05/13
 */
public class ConstructionExpressionNode extends ParametrisedExpressionNode {

	private String className;
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

	public Site getType() {
		return type.get();
	}

	public String toString() {
		return className;
	}
}
