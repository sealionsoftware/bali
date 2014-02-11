package bali.compiler.parser.tree;

import bali.compiler.validation.ValidationFailure;
import bali.compiler.validation.validator.Control;
import bali.compiler.validation.validator.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 08/05/13
 */
public abstract class Node {

	private Integer line;
	private Integer character;

	protected List<Node> children = new ArrayList<>();

	protected Node(){}

	protected Node(Integer line, Integer character) {
		this.line = line;
		this.character = character;
	}

	public Integer getLine() {
		return line;
	}

	public Integer getCharacter() {
		return character;
	}

	public List<ValidationFailure> accept(final Validator validator) throws TerminatedException {
		final List<ValidationFailure> failures = new ArrayList<>();
		try {
			failures.addAll(validator.validate(this, new Control() {
				public void validateChildren() throws TerminatedException {
					for (Node child : children){
						failures.addAll(child.accept(validator));
					}
				}
			}));
		} catch (RuntimeException e){
			if (e instanceof TerminatedException){
				throw e;
			}
			Throwable cause = e.getCause();
			if (cause instanceof InterruptedException){
				throw new TerminatedException(failures);
			}
//			throw e;
		}
		return failures;
	}

	public List<Node> getChildren() {
		return children;
	}
}
