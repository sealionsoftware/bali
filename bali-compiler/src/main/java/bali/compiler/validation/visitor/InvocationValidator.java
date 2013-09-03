package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.Expression;
import bali.compiler.parser.tree.Invocation;
import bali.compiler.parser.tree.Method;
import bali.compiler.parser.tree.TypeReference;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 25/05/13
 */
public class InvocationValidator implements Validator<Invocation> {

	public List<ValidationFailure> validate(Invocation invocation) {

		List<ValidationFailure> ret = new ArrayList<>();

		try {

			TypeReference targetType = invocation.getTarget().getType();
			TypeReference methodDeclarationType = getTypeForInvocation(invocation, targetType);
			invocation.setReturnType(methodDeclarationType);

		} catch (TypeReference.CouldNotResolveException e) {
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

	private TypeReference getTypeForInvocation(Invocation invocation, TypeReference type) {

		List<Expression> arguments = invocation.getArguments();
		List<Method> parametrisedMethods = type.getParametrisedMethods();

		for (Method methodDeclaration : parametrisedMethods) {
			if (methodDeclaration.getName().equals(invocation.getMethod())) {
				if (!argumentsMatch(methodDeclaration.getArguments(), arguments)) {
					continue;
				}

				return methodDeclaration.getType();
			}
		}
		throw new TypeReference.CouldNotResolveException();
	}


	private boolean argumentsMatch(List<Declaration> declarations, List<Expression> arguments) {
		if (declarations.size() != arguments.size()) {
			return false;
		}
		Iterator<Declaration> i = declarations.iterator();
		Iterator<Expression> j = arguments.iterator();
		while (i.hasNext()) {
			Declaration argumentDeclaration = i.next();
			Expression argument = j.next();
			TypeReference argumentType = argument.getType();
			TypeReference argumentDeclarationType = argumentDeclaration.getType();
			if (argumentType == null || !argumentType.isAssignableTo(argumentDeclarationType)) {
				return false;
			}
		}
		return true;
	}
}
