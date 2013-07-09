package bali.compiler.parser.tree;

import java.util.List;

/**
 * User: Richard
 * Date: 09/05/13
 */
public class ForStatement extends LoopStatement {

	private Declaration element;
	private Expression collection;

	public ForStatement(Integer line, Integer character) {
		super(line, character);
	}

	public Declaration getElement() {
		return element;
	}

	public void setElement(Declaration element) {
		this.element = element;
	}

	public Expression getCollection() {
		return collection;
	}

	public void setCollection(Expression collection) {
		this.collection = collection;
	}

	public List<Node> getChildren() {
		List<Node> children = super.getChildren();
		children.add(element);
		children.add(collection);
		return children;
	}
}
