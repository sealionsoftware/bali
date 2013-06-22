package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Interface;
import bali.compiler.validation.TypeDeclarationLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 19/06/13
 */
public class InterfaceValidator implements Validator<CompilationUnit> {

	private TypeDeclarationLibrary library;

	public InterfaceValidator(TypeDeclarationLibrary library) {
		this.library = library;
	}

	public List<ValidationFailure> validate(CompilationUnit node) {
		List<ValidationFailure> failures = new ArrayList<>();
		for (Interface iface : node.getInterfaces()){

			iface.setQualifiedClassName(node.getName() + "." + iface.getClassName());

			// TODO: validations

			library.addDeclaration(iface);
		}
		return failures;
	}
}
