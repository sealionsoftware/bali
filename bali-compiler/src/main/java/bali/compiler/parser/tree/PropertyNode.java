package bali.compiler.parser.tree;

/**
 * User: Richard
 * Date: 23/10/13
 */
public class PropertyNode extends DeclarationNode {

	public PropertyNode() {
	}

	public PropertyNode(Integer line, Integer character) {
		super(line, character);
	}

	public Boolean getFinal() {
		return true;
	}
}
