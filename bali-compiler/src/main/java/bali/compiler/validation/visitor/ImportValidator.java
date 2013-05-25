package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Import;
import bali.compiler.parser.tree.Interface;
import bali.compiler.parser.tree.Method;
import bali.compiler.parser.tree.MethodDeclaration;
import bali.compiler.parser.tree.Type;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ImportValidator implements Validator<Import> {

	private ClassLoader loader = ImportValidator.class.getClassLoader();

	// Engages imports, uses the base classloader to load the imported class (to check that it exists!)
	public List<ValidationFailure> validate(Import iport) {
		List<ValidationFailure> failures = new ArrayList<>();
		try {
			Class resolved = loader.loadClass(iport.getName());
			iport.setResolvedClass(resolved);
		} catch (ClassNotFoundException cnfe){
			failures.add(new ValidationFailure(
				iport,
				"Could not resolve import " + iport.getName()
			));
		}
		return failures;
	}



}
