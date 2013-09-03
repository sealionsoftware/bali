package bali.compiler.parser.tree;

/**
 * User: Richard
 * Date: 10/07/13
 */
public class ArgumentDeclaration extends Declaration {

	public ArgumentDeclaration() {
	}

	public ArgumentDeclaration(Integer line, Integer character) {
		super(line, character);
	}

	public Boolean getFinal() {
		return true;
	}
}
