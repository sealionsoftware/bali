package bali.compiler.parser.tree;

import bali.compiler.type.ParametrizedSite;
import bali.compiler.type.Site;

/**
 * User: Richard
 * Date: 29/04/13
 */
public abstract class ExpressionNode extends StatementNode {

	protected ExpressionNode() {
	}

	protected ExpressionNode(Integer line, Integer character) {
		super(line, character);
	}

	public abstract Site getType();
}
