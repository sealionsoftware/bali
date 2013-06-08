package bali.compiler.validation;

import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.validation.visitor.Validator;

/**
 * User: Richard
 * Date: 30/04/13
 */
public interface ValidationEngine extends Validator<CompilationUnit> {
}
