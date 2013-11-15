package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public abstract class ControlExpressionNode extends StatementNode {

	public ControlExpressionNode() {
	}

	public ControlExpressionNode(Integer line, Integer character) {
		super(line, character);
	}

}
