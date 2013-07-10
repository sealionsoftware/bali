package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Class;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.validation.TypeDeclarationLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
		for (Class clazz : node.getClasses()){

			clazz.setQualifiedClassName(node.getName() + "." + clazz.getClassName());
			clazz.setSourceFile(node.getName() + ".bali");
			failures.addAll(validateMemberNames(clazz));

			library.addDeclaration(clazz);
		}
		return failures;
	}

	private List<ValidationFailure> validateMemberNames(Class node){
		Set<String> memberNames = new HashSet<>();
		List<ValidationFailure> failures = new LinkedList<>();
		List<Declaration> members = new LinkedList<>();
		members.addAll(node.getArguments());
		members.addAll(node.getFields());
		members.addAll(node.getMethods());


		for (Declaration member : members){
			String memberName = member.getName();
			if (memberNames.contains(memberName)){
				failures.add(new ValidationFailure(member, "Class " + node.getClassName() + " already has a member named " + memberName));
			} else {
				memberNames.add(memberName);
			}
		}

		return failures;
	}

	private void checkMemberName(List<String> names, String current){

	}
}
