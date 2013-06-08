package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Import;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ImportsValidator implements Validator<CompilationUnit> {

	private ImportValidator importValidator = new ImportValidator(ImportValidator.class.getClassLoader());

	public List<ValidationFailure> validate(CompilationUnit unit) {
		List<ValidationFailure> failures = new ArrayList<>();
		for (Import iport : unit.getImports()) {
			failures.addAll(importValidator.validate(iport));
		}
		return failures;
	}

	public static class ImportValidator implements Validator<Import> {

		private ClassLoader loader;

		public ImportValidator(ClassLoader loader) {
			this.loader = loader;
		}

		// Engages imports, uses the base classloader to load the imported class (to check that it exists!)
		public List<ValidationFailure> validate(Import iport) {
			List<ValidationFailure> failures = new ArrayList<>();
			try {
				Class resolved = loader.loadClass(iport.getName());
				iport.setResolvedClass(resolved);
			} catch (ClassNotFoundException cnfe) {
				failures.add(new ValidationFailure(
						iport,
						"Could not resolve import " + iport.getName()
				));
			}
			return failures;
		}

	}
}
