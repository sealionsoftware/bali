package bali.compiler.validation;

import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Node;

import java.util.List;

/**
 * User: Richard
 * Date: 30/04/13
 */
public interface ValidationEngine {

	public List<ValidationFailure> validate(Node node);

}
