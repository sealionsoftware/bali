package bali.compiler.validation.validator;

import bali.compiler.parser.tree.InterfaceNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.type.Type;
import bali.compiler.type.TypeLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.Collections;
import java.util.List;

/**
 * Constructs the interfaces qualified class name, and adds it to the TypeLibrary
 * <p/>
 * User: Richard
 * Date: 19/06/13
 */
public class InterfaceValidatorFactory implements ValidatorFactory {

	private TypeLibrary library;

	public InterfaceValidatorFactory(TypeLibrary library) {
		this.library = library;
	}

	public Validator createValidator() {
		return new Validator() {
			public List<ValidationFailure> validate(Node node, Control control) {

				if (node instanceof InterfaceNode){
					InterfaceNode iface = (InterfaceNode) node;
					Type resolved = library.addDeclaration(iface);
					iface.setResolvedType(resolved);
					// TODO: validations
				}

				control.validateChildren();

				return Collections.emptyList();
			}
		};
	}

}