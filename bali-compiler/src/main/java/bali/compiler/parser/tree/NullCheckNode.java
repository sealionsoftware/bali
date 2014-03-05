package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.reference.Reference;
import bali.compiler.type.Site;

/**
 * User: Richard
 * Date: 02/07/13
 */
public class NullCheckNode extends ExpressionNode {

	private ExpressionNode target;
	private Reference<Site> type = new BlockingReference<>();

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

	public void setType(Site type) {
		this.type.set(type);
	}

	public Site getType() {
		return type.get();
	}

	public String toString(){
		return "?" + getTarget();
	}

}
