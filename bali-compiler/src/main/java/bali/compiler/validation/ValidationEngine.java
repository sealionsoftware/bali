package bali.compiler.validation;

import bali.compiler.parser.tree.CompilationUnit;

import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 30/04/13
 */
public interface ValidationEngine {

	public Map<String, List<ValidationFailure>> validate(List<CompilationUnit> node);

}
