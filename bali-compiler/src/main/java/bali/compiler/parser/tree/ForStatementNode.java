package bali.compiler.parser.tree;

import java.util.List;

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
		this.element = element;
	}

	public ExpressionNode getCollection() {
		return collection;
	}

	public void setCollection(ExpressionNode collection) {
		this.collection = collection;
	}

	public List<Node> getChildren() {
		List<Node> children = super.getChildren();
		children.add(element);
		children.add(collection);
		return children;
	}
}
