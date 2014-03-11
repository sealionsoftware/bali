package bali.compiler.validation.validator;

import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ConstantNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class UnitValidatorFactory implements ValidatorFactory {

	public Validator createValidator(final ClassLibrary library, final ConstantLibrary constantLibrary) {
		return new Validator(){

			private Set<String> memberNames;

			public List<ValidationFailure> validate(Node node, Control control) {

				if (node instanceof CompilationUnitNode){
					memberNames = new HashSet<>();
				} if (node instanceof ClassNode){
					return validate((ClassNode) node);
				} else if (node instanceof ConstantNode){
					return validate((ConstantNode) node);
				}

				control.validateChildren();
				return Collections.emptyList();
			}

			public List<ValidationFailure> validate(ClassNode node) {
				return validate(node, node.getClassName());
			}

			public List<ValidationFailure> validate(ConstantNode node) {
				return validate(node, node.getName());
			}

			public List<ValidationFailure> validate(Node node, String name) {
				if (!memberNames.add(name)){
					return Collections.singletonList(new ValidationFailure(
							node,
							"Compilation unit already has a member named " + name
					));
				}
				return Collections.emptyList();
			}
		};
	}
}
