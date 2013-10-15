package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ConstantNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.type.ConstantLibrary;
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
public class ConstantValidator implements Validator {

	private ConstantLibrary library;
	private String unitName;

	public ConstantValidator(ConstantLibrary library) {
		this.library = library;
	}

	public List<ValidationFailure> validate(Node node, Control control) {

		if (node instanceof CompilationUnitNode){
			unitName = ((CompilationUnitNode) node).getName();
		} else if (node instanceof ConstantNode){
			ConstantNode constantNode = (ConstantNode) node;
			Declaration declaration = new Declaration(constantNode.getName(), constantNode.getType().getSite());
			library.addConstant(unitName, declaration);
		}

		return Collections.emptyList();
	}

	public void onCompletion() {
		library.constantsComplete();
	}
}
