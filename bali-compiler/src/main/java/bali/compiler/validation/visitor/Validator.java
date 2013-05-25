package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.Node;
import bali.compiler.validation.ValidationFailure;

import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public interface Validator<T extends Node> {

	public List<ValidationFailure> validate(T node);

}
