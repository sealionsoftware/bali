package bali.compiler.validation.validator;

import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;

/**
 * User: Richard
 * Date: 17/10/13
 */
public interface ValidatorFactory {

	public Visitor createValidator(ClassLibrary classLib, ConstantLibrary constLib);

}
