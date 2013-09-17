package bali.compiler.parser.tree;

import bali.compiler.type.Site;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class NumberLiteralExpressionNode extends LiteralExpressionNode {

	private String serialization;
	private Site type;

	public NumberLiteralExpressionNode() {
	}

	public NumberLiteralExpressionNode(Integer line, Integer character) {
		super(line, character);
	}

}
