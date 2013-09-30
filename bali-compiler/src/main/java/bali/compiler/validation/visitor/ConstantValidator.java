package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ConstantNode;
import bali.compiler.type.Declaration;
import bali.compiler.type.TypeLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 29/09/13
 */
public class ConstantValidator implements Validator<CompilationUnitNode> {

	private TypeLibrary library;

	public ConstantValidator(TypeLibrary library) {
		this.library = library;
	}

	public List<ValidationFailure> validate(CompilationUnitNode node) {

		List<Declaration> constantDeclarations = new ArrayList();
		for (ConstantNode constantNode : node.getConstants()){
			constantDeclarations.add(new Declaration(constantNode.getName(), constantNode.getType().getSite()));
		}

		library.addConstants(node.getName(), constantDeclarations);
		return Collections.emptyList();
	}
}
