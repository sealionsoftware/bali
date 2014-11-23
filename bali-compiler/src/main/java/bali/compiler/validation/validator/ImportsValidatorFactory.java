package bali.compiler.validation.validator;

import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ImportNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.type.Class;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Checks all the imports refer to valid types, and stores these types for future use
 * <p/>
 * User: Richard
 * Date: 14/05/13
 */
public class ImportsValidatorFactory implements ValidatorFactory {

	public Visitor createValidator(final ClassLibrary library, final ConstantLibrary constantLibrary) {
		return new Visitor() {

			private Set<String> importNames;

			public List<ValidationFailure> validate(Node node, Control control) {

				if (node instanceof CompilationUnitNode){
					importNames = new HashSet<>();
				} else if (node instanceof ImportNode){
					List<ValidationFailure> failures = new ArrayList<>();
					ImportNode iport = (ImportNode) node;
					String name = iport.getName();

					if (!importNames.add(name)){
						failures.add(new ValidationFailure(iport, "Duplicate import " + name));
					}

					try {
						Class aClass = library.getReference(name).get();
						iport.setType(aClass);
					} catch (RuntimeException e) {
						failures.add(new ValidationFailure(
								iport,
								"Could not resolve import " + iport.getName()
						));
						iport.setType(null);
					}
					return failures;
				}

				control.validateChildren();
				return Collections.emptyList();
			}
		};
	}
}
