package bali.compiler.parser.tree;

import bali.compiler.reference.Reference;
import bali.compiler.reference.SimpleReference;
import bali.compiler.validation.ValidationFailure;
import bali.compiler.validation.visitor.Control;
import bali.compiler.validation.visitor.Validator;

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

	public List<ValidationFailure> accept(final Validator validator){
		final List<ValidationFailure> failures = new ArrayList<>();
		final Reference<Boolean> cont = new SimpleReference<>(true);
		failures.addAll(validator.validate(this, new Control() {
			public void validateChildrenNow() {
				acceptAll(validator, children);
				doNotValidateChildren();
			}
			public void doNotValidateChildren() {
				cont.set(false);
			}
		}));
		if (cont.get()){
			acceptAll(validator, children);
		}
		return failures;
	}

	private void acceptAll(final Validator validator, List<Node> nodes){
		for (Node child : nodes){
			child.accept(validator);
		}
	}

	public List<Node> getChildren() {
		return children;
	}
}
