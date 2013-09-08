package bali.compiler.parser.tree;

/**
 * User: Richard
 * Date: 02/05/13
 */
public abstract class StatementNode extends Node {

	protected StatementNode() {
	}

	protected StatementNode(Integer line, Integer character) {
		super(line, character);
	}
}
