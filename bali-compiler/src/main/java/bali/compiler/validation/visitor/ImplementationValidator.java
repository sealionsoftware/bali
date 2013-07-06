package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.Class;
import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.Import;
import bali.compiler.parser.tree.Interface;
import bali.compiler.parser.tree.Method;
import bali.compiler.parser.tree.MethodDeclaration;
import bali.compiler.parser.tree.Type;
import bali.compiler.parser.tree.TypeDeclaration;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ImplementationValidator implements Validator<CompilationUnit> {

	// Engages at the root of the AST, constructs a lookup table
	public List<ValidationFailure> validate(CompilationUnit unit) {

		Map<String, TypeDeclaration> interfaces = new HashMap<>();

		for (Interface iface : unit.getInterfaces()) {
			interfaces.put(iface.getQualifiedClassName(), iface);
		}
		for (Import iport : unit.getImports()) {
			TypeDeclaration<MethodDeclaration> typeDeclaration = iport.getDeclaration();
			if (typeDeclaration != null && typeDeclaration.getAbstract()) {
				interfaces.put(typeDeclaration.getQualifiedClassName(), typeDeclaration);
			}
		}

		List<ValidationFailure> failures = new ArrayList<>();
		for (Class clazz : unit.getClasses()) {
			failures.addAll(validate(clazz, interfaces));
		}
		return failures;

	}

	private List<ValidationFailure> validate(Class clazz, Map<String, TypeDeclaration> interfaces) {

		List<ValidationFailure> failures = new ArrayList<>();

		for (Type type : clazz.getImplementations()) {

			if (!interfaces.containsKey(type.getDeclaration().getQualifiedClassName())) {
				failures.add(
						new ValidationFailure(clazz, "Implementation declaration " + type.getClassName() + " is not a recognised interface")
				);
				continue;
			}

			for (Method method : clazz.getMethods()) {
				for (TypeDeclaration iface : interfaces.values()) {
					MethodDeclaration declaration = iface.getDeclaration(method.getName(), method.getArguments());
					if (declaration != null) {
						method.setDeclared(true);
						break;
					}
				}
			}
		}
		return failures;
	}
}
