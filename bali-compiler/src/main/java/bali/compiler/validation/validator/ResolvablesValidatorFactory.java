package bali.compiler.validation.validator;

import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ImportNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.reference.Reference;
import bali.compiler.reference.SimpleReference;
import bali.compiler.type.Class;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Validates the Site declarations of constants, fields, variables, arguments etc
 * <p/>
 * Requires that the imports have been resolved
 * Sets the Site object on each typed site
 * <p/>
 * User: Richard
 * Date: 14/05/13
 */
public class ResolvablesValidatorFactory implements ValidatorFactory {

	public Visitor createValidator(final ClassLibrary library, final ConstantLibrary constantLibrary) {

		return new Visitor() {

			private Map<String, Reference<bali.compiler.type.Class>> resolvables = new HashMap<>();

			public List<ValidationFailure> validate(Node node, Control control) {

				List<ValidationFailure> failures = Collections.emptyList();
				if (node instanceof CompilationUnitNode){
					control.validateChildren();
					((CompilationUnitNode) node).setResolvables(resolvables);
					return failures;
				} else if (node instanceof ImportNode){
					failures = validate((ImportNode) node);
				} else if (node instanceof ClassNode){
					failures = validate((ClassNode) node);
				}

				control.validateChildren();

				return failures;
			}

			public List<ValidationFailure> validate(ClassNode node){
				resolvables.put(node.getClassName(), library.getReference(node.getQualifiedClassName()));
				return Collections.emptyList();
			}

			// Engages at the root of the AST, constructs a lookup table of unqualified names to declarations
			public List<ValidationFailure> validate(ImportNode iport) {

				String name = iport.getName();
				Class iportClass = iport.getType();
				if (iportClass != null){
					resolvables.put(name.substring(name.lastIndexOf(".") + 1),  new SimpleReference<>(iport.getType()));
				}

				return Collections.emptyList();
			}
		};
	}
}
