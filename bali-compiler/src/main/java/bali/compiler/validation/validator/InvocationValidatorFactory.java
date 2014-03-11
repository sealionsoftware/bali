package bali.compiler.validation.validator;

import bali.compiler.parser.tree.ArgumentNode;
import bali.compiler.parser.tree.ConstructionExpressionNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.InvocationNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.ObjectNode;
import bali.compiler.parser.tree.ParametrisedExpressionNode;
import bali.compiler.reference.SimpleReference;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.type.Declaration;
import bali.compiler.type.Method;
import bali.compiler.type.ParameterisedSite;
import bali.compiler.type.Site;
import bali.compiler.type.Type;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 25/05/13
 */
public class InvocationValidatorFactory implements ValidatorFactory {

	public Validator createValidator(ClassLibrary library, ConstantLibrary constantLibrary) {

		return new Validator() {

			private Site thisSite;

			public List<ValidationFailure> validate(Node node, Control control) {

				if (node instanceof ObjectNode){
					thisSite = new ParameterisedSite(new SimpleReference<>(((ObjectNode) node).getResolvedType()));
				} else if (node instanceof InvocationNode){
					return validate((InvocationNode) node, control);
				} else if (node instanceof ConstructionExpressionNode){
					return validate((ConstructionExpressionNode) node, control);
				}

				control.validateChildren();

				return Collections.emptyList();
			}

			public List<ValidationFailure> validate(InvocationNode node, Control control) {

				List<ValidationFailure> ret = new ArrayList<>();
				ExpressionNode target = node.getTarget();
				Site targetType = target != null ? target.getType() : thisSite;
				node.setTargetType(targetType);

				if (targetType == null) {
					ret.add(new ValidationFailure(node, "The target of invocation " + node + " is void"));
					return ret;
				}

				if (targetType.isNullable()) {
					ret.add(new ValidationFailure(node, "The target of invocation " + node + " is nullable"));
				}

				Method method = getMethodWithName(node.getMethodName(), targetType);
				if (method == null) {
					ret.add(new ValidationFailure(node, "Could not resolve method with name " + node.getMethodName()));
					return ret;
				}
				node.setResolvedType(method.getType());

				ret.addAll(getResolvedArguments(node, method.getParameters()));
				control.validateChildren();
				return ret;
			}

			public List<ValidationFailure> validate(ConstructionExpressionNode node, Control control) {

				List<ValidationFailure> ret = new ArrayList<>();
				Type expressionType = node.getType();

				if (!expressionType.getTemplate().getMetaType().isConstructable()) {
					ret.add(new ValidationFailure(node, "Cannot instanciate an interface type"));
					return ret;
				}

				ret.addAll(getResolvedArguments(node, expressionType.getParameters()));
				control.validateChildren();
				return ret;

			}

			private List<ValidationFailure> getResolvedArguments(ParametrisedExpressionNode node, List<Declaration<Site>> parameters){

				boolean hasNull = false;
				boolean hasNamed = false;
				Map<String, ArgumentNode> namedArgumentsMap = new HashMap<>();
				List<ExpressionNode> resolvedArguments = new ArrayList<>();
				List<ValidationFailure> failures = new ArrayList<>();
				for (ArgumentNode argumentNode : node.getArguments()){
					String name = argumentNode.getName();
					if (name == null){
						hasNull = true;
						resolvedArguments.add(argumentNode.getValue());
					} else {
						hasNamed = true;
						if (namedArgumentsMap.containsKey(name)){
							failures.add(new ValidationFailure(argumentNode.getValue(), "Repeated argument " + name));
						}
						namedArgumentsMap.put(name, argumentNode);
					}
				}
				if (hasNull && hasNamed){
					failures.add(new ValidationFailure(node, "A argument list cannot mix named and unnamed arguments"));
					return failures;
				}
				if (hasNull){
					List<ArgumentNode> arguments = node.getArguments();
					if (arguments.size() != parameters.size()){
						failures.add(new ValidationFailure(node, "Invalid number of arguments"));
						return failures;
					}
					Iterator<ArgumentNode> i = arguments.iterator();
					for (Declaration<Site> parameter : parameters){
						i.next().setResolvedType(parameter.getType());
					}
				} else {
					boolean fail = false;
					for (Declaration<Site> parameter : parameters){
						String parameterName = parameter.getName();
						Site parameterType = parameter.getType();
						ArgumentNode resolvedArgument = namedArgumentsMap.remove(parameterName);
						if (resolvedArgument == null && !parameterType.isNullable()){
							failures.add(new ValidationFailure(node, "Parameter " + parameter + " is required"));
							fail = true;
						}
						if (resolvedArgument != null){
							resolvedArguments.add(resolvedArgument.getValue());
							resolvedArgument.setResolvedType(parameterType);
						} else {
							resolvedArguments.add(null);
						}

					}
					for (Map.Entry<String, ArgumentNode> argument : namedArgumentsMap.entrySet()){
						failures.add(new ValidationFailure(argument.getValue(), "No parameter " + argument.getKey() + " required for this call"));
					}
					if (fail){
						return failures;
					}
				}
				node.setResolvedArguments(resolvedArguments);
				Iterator<ExpressionNode> i = resolvedArguments.iterator();
				Iterator<Declaration<Site>> j = parameters.iterator();
				while (i.hasNext()){
					ExpressionNode argument = i.next();
					if (argument != null){
						Declaration<Site> parameter = j.next();
						if (!argument.getType().isAssignableTo(parameter.getType())){
							failures.add(new ValidationFailure(node, "Invalid argument type " + argument.getType() + " for parameter " + parameter + "."));
						}
					}
				}
				return failures;
			}

			private Method getMethodWithName(String name, Type site) {
				for (Method method : site.getMethods()) {
					if (method.getName().equals(name)) {
						return method;
					}
				}
				return null;
			}
		};
	}

}
