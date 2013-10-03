package bali.compiler.parser.tree;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class StringLiteralExpressionNode extends LiteralExpressionNode {

	public StringLiteralExpressionNode() {
	}

	public StringLiteralExpressionNode(Integer line, Integer character) {
		super(line, character);
	}

	public String toString(){
		return "\"" + getSerialization() + "\"";
	}
}
