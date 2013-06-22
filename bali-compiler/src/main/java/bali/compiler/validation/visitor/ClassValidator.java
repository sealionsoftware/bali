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
public class ClassValidator implements Validator<CompilationUnit> {

	private TypeDeclarationLibrary library;

	public ClassValidator(TypeDeclarationLibrary library) {
		this.library = library;
	}

	public List<ValidationFailure> validate(CompilationUnit node) {
		List<ValidationFailure> failures = new ArrayList<>();
		for (bali.compiler.parser.tree.Class clazz : node.getClasses()){

			clazz.setQualifiedClassName(node.getName() + "." + clazz.getClassName());

			// TODO: validations

			library.addDeclaration(clazz);
		}
		return failures;
	}
}
