package bali.compiler.parser.tree;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class ConstantNode extends FieldNode {

	public ConstantNode(Integer line, Integer character) {
		super(line, character);
	}

	public Boolean getFinal() {
		return true;
	}
}
