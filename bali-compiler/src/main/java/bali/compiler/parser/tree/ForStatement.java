package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 09/05/13
 */
public class ForStatement extends Statement {

	private Declaration element;
	private Expression collection;
	private CodeBlock body;

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

	public CodeBlock getBody() {
		return body;
	}

	public void setBody(CodeBlock body) {
		this.body = body;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(element);
		children.add(collection);
		children.add(body);
		return children;
	}
}
