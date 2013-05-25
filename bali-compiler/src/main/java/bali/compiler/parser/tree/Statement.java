package bali.compiler.parser.tree;

/**
 * User: Richard
 * Date: 02/05/13
 */
public abstract class Statement extends Node {

	protected Statement() {
	}

	protected Statement(Integer line, Integer character) {
		super(line, character);
	}
}
