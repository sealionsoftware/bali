package bali.compiler.parser.tree;

/**
 * User: Richard
 * Date: 10/07/13
 */
public class ParameterNode extends DeclarationNode {

	public ParameterNode() {
	}

	public ParameterNode(Integer line, Integer character) {
		super(line, character);
	}

	public Boolean getFinal() {
		return true;
	}
}
