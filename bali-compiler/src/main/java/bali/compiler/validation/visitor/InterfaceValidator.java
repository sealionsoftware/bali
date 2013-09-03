package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.InterfaceDeclaration;
import bali.compiler.validation.TypeLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 19/06/13
 */
public class InterfaceValidator implements Validator<CompilationUnit> {

	private TypeLibrary library;

	public InterfaceValidator(TypeLibrary library) {
		this.library = library;
	}

	public List<ValidationFailure> validate(CompilationUnit node) {
		List<ValidationFailure> failures = new ArrayList<>();
		for (InterfaceDeclaration iface : node.getInterfaces()){

			iface.setQualifiedClassName(node.getName() + "." + iface.getClassName());

			// TODO: validations

			library.addDeclaration(iface);
		}
		return failures;
	}
}
