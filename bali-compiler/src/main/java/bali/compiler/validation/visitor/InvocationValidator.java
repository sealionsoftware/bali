package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.Expression;
import bali.compiler.parser.tree.Interface;
import bali.compiler.parser.tree.Invocation;
import bali.compiler.parser.tree.MethodDeclaration;
import bali.compiler.parser.tree.Type;
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

				Type targetType = invocation.getTarget().getType();
				Type methodDeclarationType = getTypeForInvocation(invocation, targetType);
				invocation.setReturnType(methodDeclarationType);

			} catch (Type.CouldNotResolveException e) {
				StringBuilder sb = new StringBuilder();
				Iterator<Expression> i = invocation.getArguments().iterator();
				if (i.hasNext()) {
					sb.append(i.next().getType());
					while (i.hasNext()) {
						sb.append(",").append(i.next().getType());
					}
				}
				ret.add(new ValidationFailure(invocation, "Could not resolve method with signiture of invocation " + invocation.getTarget().getType() + "." + invocation.getMethod() + "(" + sb + ")"));
			} catch (Exception e) {
				ret.add(new ValidationFailure(invocation, "Could not validate invocation: " + e.getMessage()));
			}

			return ret;
		}

		private Type getTypeForInvocation(Invocation invocation, Type type) throws Type.CouldNotResolveException {
			List<Expression> arguments = invocation.getArguments();
			TypeDeclaration<MethodDeclaration> typeDeclaration = type.getDeclaration();
			for (MethodDeclaration methodDeclaration : typeDeclaration.getMethods()) {
				if (methodDeclaration.getName().equals(invocation.getMethod())) {
					if (!argumentsMatch(methodDeclaration.getArguments(), arguments)){
						continue;
					}

					Type methodReturnType =  methodDeclaration.getType();

					return methodReturnType == null ? null : methodReturnType.getResolvedType(type);
				}
			}
			throw new Type.CouldNotResolveException();
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
				Type argumentType = argument.getType();
				Type argumentDeclarationType = argumentDeclaration.getType();
				if (argumentType == null || !argumentType.isAssignableTo(argumentDeclarationType)) {
					return false;
				}
			}
			return true;
		}


	}




}
