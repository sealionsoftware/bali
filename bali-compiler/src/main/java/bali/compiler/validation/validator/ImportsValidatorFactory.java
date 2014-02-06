package bali.compiler.validation.validator;

import bali.compiler.parser.tree.ImportNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.type.Type;
import bali.compiler.type.TypeLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Checks all the imports refer to valid types, and stores these types for future use
 * <p/>
 * User: Richard
 * Date: 14/05/13
 */
public class ImportsValidatorFactory implements ValidatorFactory {

	private TypeLibrary library;

	public ImportsValidatorFactory(TypeLibrary library) {
		this.library = library;
	}

	public Validator createValidator() {
		return new Validator() {
			public List<ValidationFailure> validate(Node node, Control control) {

				if (node instanceof ImportNode){
					List<ValidationFailure> failures = new ArrayList<>();
					ImportNode iport = (ImportNode) node;
					try {
						Type type = library.getType(iport.getName());
						iport.setType(type);
					} catch (Exception e) {
						failures.add(new ValidationFailure(
								iport,
								"Could not resolve import " + iport.getName() + " because " + e.getMessage()
						));
						iport.setType(null);
					}
					return failures;
				} else {
					control.validateChildren();
				}
				return Collections.emptyList();
			}
		};
	}
}
