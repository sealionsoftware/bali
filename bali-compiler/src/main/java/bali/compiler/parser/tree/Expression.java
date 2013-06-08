package bali.compiler.parser.tree;

/**
 * User: Richard
 * Date: 29/04/13
 */
public abstract class Expression extends Statement {

	protected Expression() {
	}

	protected Expression(Integer line, Integer character) {
		super(line, character);
	}

	public abstract Type getType();
}
