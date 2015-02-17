package bali.compiler.processors;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * User: Richard
 * Date: 01/05/13
 */
public class ValidationFailure {

	private ParserRuleContext node;
	private String message;

	public ValidationFailure(ParserRuleContext node, String message) {
		this.node = node;
		this.message = message;
	}

	public ParserRuleContext getNode() {
		return node;
	}

	public String getMessage() {
		return message;
	}

	public String toString() {
		return message + " (line " + node.getStart().getLine() + ")";
	}
}
