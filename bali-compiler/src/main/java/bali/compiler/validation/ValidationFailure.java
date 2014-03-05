package bali.compiler.validation;

import bali.compiler.parser.tree.Node;

/**
 * User: Richard
 * Date: 01/05/13
 */
public class ValidationFailure {

	private Node node;
	private String message;

	public ValidationFailure(Node node, String message) {
		this.node = node;
		this.message = message;
	}

	public Node getNode() {
		return node;
	}

	public String getMessage() {
		return message;
	}

	public String toString() {
		return message;
	}
}
