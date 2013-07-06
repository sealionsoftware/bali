package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Import;
import bali.compiler.parser.tree.TypeDeclaration;
import bali.compiler.validation.TypeDeclarationLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ImportsValidator implements Validator<CompilationUnit> {

	private ImportValidator importValidator = new ImportValidator();
	private TypeDeclarationLibrary library;

	public ImportsValidator(TypeDeclarationLibrary library) {
		this.library = library;
	}

	public List<ValidationFailure> validate(CompilationUnit unit) {
		List<ValidationFailure> failures = new ArrayList<>();
		for (Import iport : unit.getImports()) {
			failures.addAll(importValidator.validate(iport));
		}
		return failures;
	}

	public class ImportValidator implements Validator<Import> {

		// Engages imports, uses the base classloader to load the imported class (to check that it exists!)
		public List<ValidationFailure> validate(Import iport) {
			List<ValidationFailure> failures = new ArrayList<>();
			try {
				TypeDeclaration declaration = library.getTypeDeclaration(iport.getName());
				iport.setDeclaration(declaration);
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
