package bali.compiler.validation;

import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;

import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 30/04/13
 */
public interface ValidationEngine {

	public Map<String, List<ValidationFailure>> validate(List<CompilationUnitNode> node, ClassLibrary classLibrary, ConstantLibrary constantLibrary);

}
