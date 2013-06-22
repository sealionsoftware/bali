package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.Class;
import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.Import;
import bali.compiler.parser.tree.Interface;
import bali.compiler.parser.tree.Method;
import bali.compiler.parser.tree.MethodDeclaration;
import bali.compiler.parser.tree.Type;
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

		Map<String, Interface> interfaces = new HashMap<>();

		for (Interface iface : unit.getInterfaces()) {
			interfaces.put(iface.getQualifiedClassName(), iface);
		}
		for (Import iport : unit.getImports()) {
			java.lang.Class clazz = iport.getResolvedClass();
			if (clazz != null && clazz.isInterface()) {
				interfaces.put(clazz.getName(), map(clazz));
			}
		}

		List<ValidationFailure> failures = new ArrayList<>();
		for (Class clazz : unit.getClasses()) {
			failures.addAll(validate(clazz, interfaces));
		}
		return failures;

	}

	private Interface map(java.lang.Class clazz) {
		Interface iface = new Interface();
		iface.setQualifiedClassName(clazz.getName());
		for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
			iface.addMethod(map(method));
		}
		return iface;
	}

	private MethodDeclaration map(java.lang.reflect.Method method) {
		MethodDeclaration ret = new MethodDeclaration();

		Type returnType = new Type();
		returnType.setClassName(method.getReturnType().getName());

		ret.setName(method.getName());
		ret.setType(returnType);

		for (java.lang.Class clazz : method.getParameterTypes()) {
			Type argumentType = new Type();
			argumentType.setClassName(clazz.getName());
			Declaration declaration = new Declaration();
			declaration.setType(argumentType);
			ret.addArgument(declaration);
		}

		return ret;
	}

	private List<ValidationFailure> validate(Class clazz, Map<String, Interface> interfaces) {

		List<ValidationFailure> failures = new ArrayList<>();

		for (Type type : clazz.getImplementations()) {
			if (!interfaces.containsKey(type.getDeclaration().getQualifiedClassName())) {
				failures.add(
						new ValidationFailure(clazz, "Implementation declaration " + type.getClassName() + " is not a recognised interface")
				);
				continue;
			}

			for (Method method : clazz.getMethods()) {
				for (Interface iface : interfaces.values()) {
					MethodDeclaration declaration = iface.getDeclaration(method.getName());
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
