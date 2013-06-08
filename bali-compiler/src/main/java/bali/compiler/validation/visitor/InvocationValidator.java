package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.Expression;
import bali.compiler.parser.tree.Interface;
import bali.compiler.parser.tree.Invocation;
import bali.compiler.parser.tree.MethodDeclaration;
import bali.compiler.parser.tree.MethodDeclaringType;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.Type;
import bali.compiler.validation.ValidationFailure;

import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 25/05/13
 */
public class InvocationValidator implements Validator<CompilationUnit> {

	public List<ValidationFailure> validate(CompilationUnit node) {

		Map<String, MethodDeclaringType<MethodDeclaration>> internalTypes = new HashMap<>();

		for (Interface iface : node.getInterfaces()) {
			internalTypes.put(iface.getQualifiedClassName(), iface);
		}

		for (bali.compiler.parser.tree.Class clazz : node.getClasses()) {
			internalTypes.put(clazz.getQualifiedClassName(), (MethodDeclaringType) clazz);
		}

		InvocationValidatorAgent agent = new InvocationValidatorAgent(internalTypes);

		return walk(agent, node);
	}

	private List<ValidationFailure> walk(InvocationValidatorAgent agent, Node node) {
		List<ValidationFailure> validationFailures = new ArrayList<>();
		for (Node child : node.getChildren()) {
			validationFailures.addAll(walk(agent, child));
		}
		if (node instanceof Invocation) {
			validationFailures.addAll(agent.validate((Invocation) node));
		}
		return validationFailures;
	}


	public static class InvocationValidatorAgent implements Validator<Invocation> {

		private Map<String, MethodDeclaringType<MethodDeclaration>> internalTypes;

		public InvocationValidatorAgent(Map<String, MethodDeclaringType<MethodDeclaration>> internalTypes) {
			this.internalTypes = internalTypes;
		}

		public List<ValidationFailure> validate(Invocation invocation) {

			List<ValidationFailure> ret = new ArrayList<>();

			try {

				MethodDeclaringType targetType = getMethodDeclaringType(invocation.getTarget().getType());
				MethodDeclaration declaration = getDeclarationForInvocation(invocation, targetType);

				if (declaration != null) {
					invocation.setReturnType(declaration.getType());
				} else {
					StringBuilder sb = new StringBuilder();
					Iterator<Expression> i = invocation.getArguments().iterator();
					if (i.hasNext()) {
						sb.append(i.next().getType());
						while (i.hasNext()) {
							sb.append(",").append(i.next().getType());
						}
					}
					ret.add(new ValidationFailure(invocation, "Could not resolve method with signiture of invocation " + invocation.getTarget().getType() + "." + invocation.getMethod() + "(" + sb + ")"));
				}

			} catch (ClassNotFoundException e) {
				ret.add(new ValidationFailure(invocation, "Could not resolve class: " + e.getMessage()));
			} catch (Exception e) {
				ret.add(new ValidationFailure(invocation, "Could not validate invocation: " + e.getMessage()));
			}

			return ret;
		}

		private MethodDeclaringType getMethodDeclaringType(Type valueType) throws Exception {
			String className = valueType.getQualifiedClassName();
			MethodDeclaringType<MethodDeclaration> ret = internalTypes.get(className);
			if (ret != null) {
				return ret;
			}

			Class clazz = Class.forName(className);
			ret = new ClasspathType();
			for (Method method : clazz.getDeclaredMethods()) {
				MethodDeclaration declaration = new MethodDeclaration();
				for (Class argumentClass : method.getParameterTypes()) {
					Declaration argumentDeclaration = new Declaration();
					Type argumentType = new Type();
					argumentType.setQualifiedClassName(argumentClass.getName());
					argumentDeclaration.setType(argumentType);
					declaration.addArgument(argumentDeclaration);
				}
				declaration.setName(method.getName());

				java.lang.reflect.Type returnType = method.getGenericReturnType();

				if (returnType instanceof TypeVariable) {
					TypeVariable genericReturnType = (TypeVariable) returnType;
					int i = 0;
					for (TypeVariable tv : clazz.getTypeParameters()) {
						if (tv.equals(genericReturnType)) {
							declaration.setType(valueType.getParameters().get(i));
							break;
						}
						i++;
					}
				} else if (returnType != void.class) {
					Type methodReturnType = new Type();
					methodReturnType.setQualifiedClassName(((Class) returnType).getName());
					declaration.setType(methodReturnType);
				}

				ret.addMethod(declaration);
			}

			return ret;
		}


		private MethodDeclaration getDeclarationForInvocation(Invocation invocation, MethodDeclaringType<?> type) {
			List<Expression> arguments = invocation.getArguments();
			method:
			for (MethodDeclaration declaration : type.getMethods()) {
				if (declaration.getName().equals(invocation.getMethod())) {
					List<Declaration> argumentDeclarations = declaration.getArguments();
					if (argumentDeclarations.size() == arguments.size()) {
						int i = 0;
						for (Expression argument : arguments) {
							if (!argument.getType().isAssignableTo(argumentDeclarations.get(i++).getType())) {
								continue method;
							}
						}
						return declaration;
					}
				}
			}
			return null;
		}
	}

	private static class ClasspathType extends MethodDeclaringType<MethodDeclaration> {

		private List<MethodDeclaration> declarations = new ArrayList<>();

		public List<MethodDeclaration> getMethods() {
			return declarations;
		}

		public void addMethod(MethodDeclaration method) {
			declarations.add(method);
		}
	}


}
