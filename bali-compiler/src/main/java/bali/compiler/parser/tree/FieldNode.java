package bali.compiler.parser.tree;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class FieldNode extends DeclarationNode {

	private ExpressionNode value;

	public FieldNode() {
	}

	public FieldNode(Integer line, Integer character) {
		super(line, character);
	}

	public ExpressionNode getValue() {
		return value;
	}

	public void setValue(ExpressionNode value) {
		children.add(value);
		this.value = value;
	}

	public Boolean getFinal() {
		return false;
	}

}
