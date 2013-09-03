package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.ClassDeclaration;
import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.Import;
import bali.compiler.parser.tree.InterfaceDeclaration;
import bali.compiler.parser.tree.Method;
import bali.compiler.parser.tree.MethodDeclaration;
import bali.compiler.parser.tree.TypeReference;
import bali.compiler.parser.tree.TypeDeclaration;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Checks that classes implement their declared interfaces correctly
 * <p/>
 * Supplies method access modifiers
 * <p/>
 * User: Richard
 * Date: 14/05/13
 */
public class ImplementationValidator implements Validator<CompilationUnit> {

	public List<ValidationFailure> validate(CompilationUnit unit) {

		Map<String, TypeDeclaration<Method>> interfaces = new HashMap<>();

		for (InterfaceDeclaration iface : unit.getInterfaces()) {
			interfaces.put(iface.getQualifiedClassName(), iface);
		}
		for (Import iport : unit.getImports()) {
			TypeDeclaration<Method> typeDeclaration = iport.getDeclaration();
			if (typeDeclaration != null && typeDeclaration.getAbstract()) {
				interfaces.put(typeDeclaration.getQualifiedClassName(), typeDeclaration);
			}
		}

		List<ValidationFailure> failures = new ArrayList<>();
		for (ClassDeclaration clazz : unit.getClasses()) {
			failures.addAll(validate(clazz, interfaces));
		}
		return failures;

	}

	private List<ValidationFailure> validate(ClassDeclaration clazz, Map<String, TypeDeclaration<Method>> interfaces) {

		List<ValidationFailure> failures = new ArrayList<>();

		for (TypeReference type : clazz.getImplementations()) {

			TypeDeclaration<Method> ifaceDeclaration = type.getDeclaration();

			if (!interfaces.containsKey(ifaceDeclaration.getQualifiedClassName())) {
				failures.add(
						new ValidationFailure(clazz, "Implementation declaration " + type.getClassName() + " is not a recognised interface")
				);
				continue;
			}

			for (MethodDeclaration method : clazz.getMethods()) {
				for (TypeDeclaration iface : interfaces.values()) {
					List<TypeReference> argumentTypes = new ArrayList<>();
					for (Declaration declaration : method.getArguments()){
						argumentTypes.add(declaration.getType());
					}
					Method declaration = iface.getDeclaration(method.getName(), argumentTypes);
					if (declaration != null) {
						method.setDeclared(true);
						break;
					}
				}
			}

			for (Method method : ifaceDeclaration.getMethods()) {
				List<TypeReference> types = new ArrayList<>();
				for (Declaration declaration : method.getArguments()) {
					types.add(declaration.getType());
				}
				if (clazz.getDeclaration(method.getName(), types) == null) {
					failures.add(
							new ValidationFailure(clazz, "Class " + clazz.getClassName() + " does not implement method " + method)
					);
				}
			}

		}
		return failures;
	}
}
