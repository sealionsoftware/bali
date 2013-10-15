package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.TypeNode;
import bali.compiler.type.TypeLibrary;
import bali.compiler.validation.ValidationFailure;

import javax.lang.model.type.DeclaredType;
import java.util.ArrayList;
import java.util.Collections;
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
public class DeclaredTypeValidator implements Validator {

	private TypeLibrary library;
	private String unitName;

	public DeclaredTypeValidator(TypeLibrary library) {
		this.library = library;
	}

	public List<ValidationFailure> validate(Node node, Control control) {
		if (node instanceof CompilationUnitNode){
			unitName = ((CompilationUnitNode) node).getName();
		} else if (node instanceof TypeNode) {
			TypeNode typeNode = (TypeNode) node;
			String qualifiedName = unitName + "." + typeNode.getClassName();
			library.notifyOfDeclaration(qualifiedName);
			typeNode.setQualifiedClassName(qualifiedName);
		}
		return Collections.emptyList();
	}

	public void onCompletion() {
	}

}
