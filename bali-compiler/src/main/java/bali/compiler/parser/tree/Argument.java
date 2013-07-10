package bali.compiler.parser.tree;

/**
 * User: Richard
 * Date: 10/07/13
 */
public class Argument extends Declaration {

	public Argument() {
	}

	public Argument(Integer line, Integer character) {
		super(line, character);
	}

	public Boolean getFinal() {
		return true;
	}
}
