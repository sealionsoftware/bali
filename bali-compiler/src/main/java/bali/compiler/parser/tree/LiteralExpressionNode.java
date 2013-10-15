package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.type.Site;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public abstract class LiteralExpressionNode extends ExpressionNode {

	private String serialization;

	private BlockingReference<Site> type = new BlockingReference<>();

	protected LiteralExpressionNode() {
	}

	public LiteralExpressionNode(Integer line, Integer character) {
		super(line, character);
	}

	public void setType(Site type) {
		this.type.set(type);
	}

	public Site getType() {
		return type.get();
	}

	public void setSerialization(String serialization) {
		this.serialization = serialization;
	}

	public String getSerialization() {
		return serialization;
	}
}
