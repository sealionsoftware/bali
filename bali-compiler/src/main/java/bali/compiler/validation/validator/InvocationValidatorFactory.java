package bali.compiler.validation.validator;

import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.InvocationNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.type.Declaration;
import bali.compiler.type.Method;
import bali.compiler.type.Site;
import bali.compiler.type.VanillaSite;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 25/05/13
 */
public class InvocationValidatorFactory implements ValidatorFactory {

	public Validator createValidator() {

		return new Validator() {

			private Site thisSite;

			public List<ValidationFailure> validate(Node node, Control control) {

				if (node instanceof ClassNode){
					thisSite = new VanillaSite(((ClassNode) node).getResolvedType());
				}

				control.validateChildren();

				if (node instanceof InvocationNode){

					InvocationNode invocation = (InvocationNode) node;

					List<ValidationFailure> ret = new ArrayList<>();
					ExpressionNode target = invocation.getTarget();
					Site targetType = target != null ? target.getType() : thisSite;
					invocation.setTargetType(targetType);

					if (targetType == null){
						ret.add(new ValidationFailure(node, "The target of invocation " + invocation.getMethodName() + " is void"));
						return ret;
					}

					Method method = getMethodWithName(invocation.getMethodName(), targetType);
					if (method == null){
						ret.add(new ValidationFailure(invocation, "Could not resolve method with name " + invocation.getMethodName()));
						return ret;
					}
					List<ExpressionNode> arguments = invocation.getArguments();
					List<Declaration> parameters = method.getParameters();
					if (arguments.size() != parameters.size()){
						ret.add(new ValidationFailure(invocation, "Invalid number of arguments (" + arguments.size() + ") for method " + method + "."));
						return ret;
					}
					Iterator<ExpressionNode> i = arguments.iterator();
					Iterator<Declaration> j = parameters.iterator();
					while (i.hasNext()){
						ExpressionNode argument = i.next();
						Declaration parameter = j.next();
						if (!argument.getType().isAssignableTo(parameter.getType())){
							ret.add(new ValidationFailure(invocation, "Invalid argument type " + argument.getType() + " for parameter " + parameter + "."));
						}
					}
					invocation.setResolvedMethod(method);
					return ret;
				}

				return Collections.emptyList();
			}

			public Method getMethodWithName(String name, Site site) {
				for (Method method : site.getMethods()) {
					if (method.getName().equals(name)) {
						return method;
					}
				}
				for (Site iface : site.getInterfaces()){
					Method ret = getMethodWithName(name, iface);
					if (ret != null){
						return ret;
					}
				}
				return null;
			}
		};
	}

}
