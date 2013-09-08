package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 08/07/13
 */
public class BreakStatementNode extends StatementNode {

	public BreakStatementNode(Integer line, Integer character) {
		super(line, character);
	}

	public List<Node> getChildren() {
		return new ArrayList<>();
	}
}
