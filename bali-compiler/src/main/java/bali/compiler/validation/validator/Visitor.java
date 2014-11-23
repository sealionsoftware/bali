package bali.compiler.validation.validator;

import bali.compiler.parser.tree.Node;
import bali.compiler.validation.ValidationFailure;

import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public interface Visitor {

	public void visit(Node node);

}
