package bali.compiler.validation.validator;

import bali.compiler.BaliCompiler;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.DeclarationNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.ObjectNode;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Constructs the Class declarations qualified name,
 * Sets the source file name
 * Checks for member name duplication
 * Adds the type to the ClassLibrary
 * <p/>
 * User: Richard
 * Date: 19/06/13
 */
public class ClassValidatorFactory implements ValidatorFactory {

	public Validator createValidator(final ClassLibrary library, final ConstantLibrary constantLibrary) {
		return new Validator() {

			private String sourceName;

			public List<ValidationFailure> validate(Node node, Control control) {

				List<ValidationFailure> failures;
				if (node instanceof CompilationUnitNode){
					failures = validate((CompilationUnitNode) node);
				} else if (node instanceof ObjectNode){
					failures = validate((ObjectNode) node);
				} else {
					failures = Collections.emptyList();
				}
				control.validateChildren();

				return failures;
			}

			public List<ValidationFailure> validate(CompilationUnitNode node) {
				sourceName = node.getSourceFile();
				return Collections.emptyList();
			}

			private List<ValidationFailure> validate(ObjectNode node) {

				node.setSourceFile(sourceName);

				bali.compiler.type.Class resolved = library.addDeclaration(node);
				node.setResolvedType(resolved);

				Set<String> memberNames = new HashSet<>();
				List<ValidationFailure> failures = new LinkedList<>();
				List<DeclarationNode> members = new LinkedList<>();
				members.addAll(node.getArgumentDeclarations());
				members.addAll(node.getFields());
				members.addAll(node.getMethods());

				for (DeclarationNode member : members) {
					String memberName = member.getName();
					if (memberNames.contains(memberName)) {
						failures.add(new ValidationFailure(member, "Class " + node.getClassName() + " already has a member named " + memberName));
					} else {
						memberNames.add(memberName);
					}
				}

				return failures;
			}
		};
	}
}
