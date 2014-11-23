package bali.compiler.validation.validator;

import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ConstantNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.type.Declaration;
import bali.compiler.type.Site;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 29/09/13
 */
public class ConstantValidatorFactory implements ValidatorFactory {

	public Visitor createValidator(final ClassLibrary library, final ConstantLibrary constantLibrary) {
		return new Visitor() {

			private List<Declaration<Site>> declarations = new ArrayList<>();

			public List<ValidationFailure> validate(Node node, Control control) {

				if (node instanceof CompilationUnitNode){
					String unitName = ((CompilationUnitNode) node).getName();
					control.validateChildren();
					constantLibrary.addPackageConstants(unitName, declarations);
				} else if (node instanceof ConstantNode){
					ConstantNode constantNode = (ConstantNode) node;
					declarations.add(
							new Declaration<>(constantNode.getName(), constantNode.getType().getSite())
					);
				} else {
					control.validateChildren();
				}

				return Collections.emptyList();
			}
		};
	}
}
