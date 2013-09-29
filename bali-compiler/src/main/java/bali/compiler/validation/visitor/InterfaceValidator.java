package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.InterfaceNode;
import bali.compiler.type.TypeLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * Constructs the interfaces qualified class name, and adds it to the TypeLibrary
 * <p/>
 * User: Richard
 * Date: 19/06/13
 */
public class InterfaceValidator implements Validator<CompilationUnitNode>{

	private TypeLibrary library;

	public InterfaceValidator(TypeLibrary library) {
		this.library = library;
	}

	public List<ValidationFailure> validate(CompilationUnitNode node) {
		List<ValidationFailure> failures = new ArrayList<>();
		for (InterfaceNode iface : node.getInterfaces()) {

			// TODO: validations

			library.addDeclaration(iface);
		}
		return failures;
	}
}
