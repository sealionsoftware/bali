package bali.compiler.parser.tree;

/**
 * User: Richard
 * Date: 10/07/13
 */
public class ArgumentDeclarationNode extends DeclarationNode {

	public ArgumentDeclarationNode() {
	}

	public ArgumentDeclarationNode(Integer line, Integer character) {
		super(line, character);
	}

	public Boolean getFinal() {
		return true;
	}
}
