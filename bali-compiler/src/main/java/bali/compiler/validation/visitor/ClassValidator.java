package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.DeclarationNode;
import bali.compiler.type.TypeLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Constructs the Class declarations qualified name,
 * Sets the source file name
 * Checks for member name duplication
 * Adds the type to the TypeLibrary
 * <p/>
 * User: Richard
 * Date: 19/06/13
 */
public class ClassValidator implements Validator<CompilationUnitNode> {

	private TypeLibrary library;

	public ClassValidator(TypeLibrary library) {
		this.library = library;
	}

	public List<ValidationFailure> validate(CompilationUnitNode node) {
		List<ValidationFailure> failures = new ArrayList<>();
		for (ClassNode clazz : node.getClasses()) {

			clazz.setQualifiedClassName(node.getName() + "." + clazz.getClassName());
			clazz.setSourceFile(node.getName() + ".bali");
			failures.addAll(validateMemberNames(clazz));

			library.addDeclaration(clazz);
		}
		return failures;
	}

	private List<ValidationFailure> validateMemberNames(ClassNode node) {
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
}
