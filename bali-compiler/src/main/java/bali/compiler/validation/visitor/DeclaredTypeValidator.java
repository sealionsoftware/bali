package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.TypeNode;
import bali.compiler.type.TypeLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * Constructs the Class declarations qualified name,
 * Sets the source file name
 * Checks for member name duplication
 * Adds the type to the TypeLibrary
 * <p/>
 * User: Richard
 * Date: 19/06/13
 */
public class DeclaredTypeValidator implements Validator<CompilationUnitNode> {

	private TypeLibrary library;

	public DeclaredTypeValidator(TypeLibrary library) {
		this.library = library;
	}

	public List<ValidationFailure> validate(CompilationUnitNode node) {
		List<ValidationFailure> failures = new ArrayList<>();
		List<TypeNode> nodes = new ArrayList<TypeNode>(node.getClasses());
		nodes.addAll(node.getInterfaces());
		for (TypeNode clazz : nodes) {
			clazz.setQualifiedClassName(node.getName() + "." + clazz.getClassName());
			library.notifyOfDeclaration(clazz);
		}
		return failures;
	}

}
