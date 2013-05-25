package bali.compiler.parser.tree;

/**
 * User: Richard
 * Date: 29/04/13
 */
public abstract class Value extends Statement {

	protected Value() {
	}

	protected Value(Integer line, Integer character) {
		super(line, character);
	}

	public abstract String getValueTypeName();
}
