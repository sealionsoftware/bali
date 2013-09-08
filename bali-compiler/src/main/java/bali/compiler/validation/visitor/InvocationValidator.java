package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.InvocationNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.validation.ValidationFailure;
import bali.compiler.validation.type.Declaration;
import bali.compiler.validation.type.Method;
import bali.compiler.validation.type.Site;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 25/05/13
 */
public class InvocationValidator implements Validator<InvocationNode> {

	public List<ValidationFailure> validate(InvocationNode invocation) {

		List<ValidationFailure> ret = new ArrayList<>();

		try {

			Site targetType = invocation.getTarget().getType();
			Site methodDeclarationType = getTypeForInvocation(invocation, targetType);
			invocation.setReturnType(methodDeclarationType);

		} catch (SiteNode.CouldNotResolveException e) {
			StringBuilder sb = new StringBuilder();
			Iterator<ExpressionNode> i = invocation.getArguments().iterator();
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

	private Site getTypeForInvocation(InvocationNode invocation, Site type) {

		List<ExpressionNode> arguments = invocation.getArguments();
		List<Method> methods = type.getMethods();

		for (Method method : methods) {
			if (method.getName().equals(invocation.getMethod())) {
				if (!argumentsMatch(method.getParameters(), arguments)) {
					continue;
				}

				return method.getType();
			}
		}
		throw new SiteNode.CouldNotResolveException();
	}


	private boolean argumentsMatch(List<Declaration> declarations, List<ExpressionNode> arguments) {
		if (declarations.size() != arguments.size()) {
			return false;
		}
		Iterator<Declaration> i = declarations.iterator();
		Iterator<ExpressionNode> j = arguments.iterator();
		while (i.hasNext()) {
			Declaration argumentDeclaration = i.next();
			ExpressionNode argument = j.next();
			Site argumentType = argument.getType();
			Site argumentDeclarationType = argumentDeclaration.getType();
			if (argumentType == null || !argumentType.isAssignableTo(argumentDeclarationType)) {
				return false;
			}
		}
		return true;
	}
}
