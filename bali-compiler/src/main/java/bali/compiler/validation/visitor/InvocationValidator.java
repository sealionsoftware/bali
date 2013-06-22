package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.Expression;
import bali.compiler.parser.tree.Interface;
import bali.compiler.parser.tree.Invocation;
import bali.compiler.parser.tree.MethodDeclaration;
import bali.compiler.parser.tree.TypeDeclaration;
import bali.compiler.parser.tree.Node;
import bali.compiler.validation.ValidationFailure;

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

		Map<String, TypeDeclaration<MethodDeclaration>> internalTypes = new HashMap<>();

		for (Interface iface : node.getInterfaces()) {
			internalTypes.put(iface.getQualifiedClassName(), iface);
		}

		for (bali.compiler.parser.tree.Class clazz : node.getClasses()) {
			internalTypes.put(clazz.getQualifiedClassName(), (TypeDeclaration) clazz);
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

		private Map<String, TypeDeclaration<MethodDeclaration>> internalTypes;

		public InvocationValidatorAgent(Map<String, TypeDeclaration<MethodDeclaration>> internalTypes) {
			this.internalTypes = internalTypes;
		}

		public List<ValidationFailure> validate(Invocation invocation) {

			List<ValidationFailure> ret = new ArrayList<>();

			try {

				TypeDeclaration targetType = invocation.getTarget().getType().getDeclaration();
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

			} catch (Exception e) {
				ret.add(new ValidationFailure(invocation, "Could not validate invocation: " + e.getMessage()));
			}

			return ret;
		}

		private MethodDeclaration getDeclarationForInvocation(Invocation invocation, TypeDeclaration<?> type) {
			List<Expression> arguments = invocation.getArguments();
			for (MethodDeclaration declaration : type.getMethods()) {
				if (declaration.getName().equals(invocation.getMethod())) {
					if (!argumentsMatch(declaration.getArguments(), arguments)){
						continue;
					}
					return declaration;
				}
			}
			return null;
		}

		private boolean argumentsMatch(List<Declaration> declarations, List<Expression> arguments){
			if (declarations.size() != arguments.size()) {
				return false;
			}
			Iterator<Declaration> i = declarations.iterator();
			Iterator<Expression> j = arguments.iterator();
			while (i.hasNext()){
				Declaration argumentDeclaration = i.next();
				Expression argument = j.next();
				if (!argument.getType().isAssignableTo(argumentDeclaration.getType())) {
					return false;
				}
			}
			return true;
		}

	}




}
