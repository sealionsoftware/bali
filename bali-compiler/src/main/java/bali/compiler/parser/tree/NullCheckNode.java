package bali.compiler.parser.tree;

import bali.compiler.type.Site;

/**
 * User: Richard
 * Date: 02/07/13
 */
public class NullCheckNode extends ExpressionNode {

	private ExpressionNode target;
	private Site type;

	public NullCheckNode() {
	}

	public NullCheckNode(Integer line, Integer character) {
		super(line, character);
	}

	public ExpressionNode getTarget() {
		return target;
	}

	public void setTarget(ExpressionNode target) {
		children.add(target);
		this.target = target;
	}

	public void setType(Site site) {
		this.type = site;
	}

	public Site getType() {
		return type;
	}

	public String toString(){
		return "?" + getTarget();
	}

}
