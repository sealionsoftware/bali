package bali.compiler.validation.validator;

import bali.compiler.parser.tree.InterfaceNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.Collections;
import java.util.List;

/**
 * Constructs the interfaces qualified class name, and adds it to the ClassLibrary
 * <p/>
 * User: Richard
 * Date: 19/06/13
 */
public class InterfaceValidatorFactory implements ValidatorFactory {

	public Validator createValidator(final ClassLibrary library, final ConstantLibrary constantLibrary) {
		return new Validator() {
			public List<ValidationFailure> validate(Node node, Control control) {

				if (node instanceof InterfaceNode){
					InterfaceNode iface = (InterfaceNode) node;
					bali.compiler.type.Class resolved = library.addDeclaration(iface);
					iface.setResolvedType(resolved);
					// TODO: validations
				}

				control.validateChildren();

				return Collections.emptyList();
			}
		};
	}

}
