package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.ImportNode;
import bali.compiler.validation.TypeLibrary;
import bali.compiler.validation.ValidationFailure;
import bali.compiler.validation.type.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks all the imports refer to valid types, and stores these types for future use
 *
 * User: Richard
 * Date: 14/05/13
 */
public class ImportsValidator implements Validator<ImportNode> {

	private TypeLibrary library;

	public ImportsValidator(TypeLibrary library) {
		this.library = library;
	}

	public List<ValidationFailure> validate(ImportNode iport) {
		List<ValidationFailure> failures = new ArrayList<>();
		try {
			Type type = library.getType(iport.getName());
			iport.setType(type);
		} catch (Exception cnfe) {
			failures.add(new ValidationFailure(
					iport,
					"Could not resolve import " + iport.getName()
			));
		}
		return failures;
	}
}
