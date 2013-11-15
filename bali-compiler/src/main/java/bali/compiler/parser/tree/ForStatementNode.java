package bali.compiler.parser.tree;

/**
 * User: Richard
 * Date: 09/05/13
 */
public class ForStatementNode extends LoopStatementNode {

	private DeclarationNode element;
	private ExpressionNode collection;

	public ForStatementNode(Integer line, Integer character) {
		super(line, character);
	}

	public DeclarationNode getElement() {
		return element;
	}

	public void setElement(DeclarationNode element) {
		children.add(element);
		this.element = element;
	}

	public ExpressionNode getCollection() {
		return collection;
	}

	public void setCollection(ExpressionNode collection) {
		children.add(collection);
		this.collection = collection;
	}
}
