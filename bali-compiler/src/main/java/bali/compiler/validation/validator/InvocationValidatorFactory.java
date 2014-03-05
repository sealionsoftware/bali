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
				}

				control.validateChildren();

				if (node instanceof InvocationNode){
					return validate((InvocationNode) node);
				} else if (node instanceof ConstructionExpressionNode){
					return validate((ConstructionExpressionNode) node);
				}

				return Collections.emptyList();
			}

			public List<ValidationFailure> validate(InvocationNode node) {

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
				return ret;

			}

			public List<ValidationFailure> validate(ConstructionExpressionNode node) {

				List<ValidationFailure> ret = new ArrayList<>();
				bali.compiler.type.Class expressionClass = node.getType().getTemplate();

				if (!expressionClass.getMetaType().isConstructable()) {
					ret.add(new ValidationFailure(node, "Cannot instanciate an interface type"));
					return ret;
				}

				ret.addAll(getResolvedArguments(node, expressionClass.getParameters()));
				return ret;

			}

			private List<ValidationFailure> getResolvedArguments(ParametrisedExpressionNode node, List<Declaration<Site>> parameters){

				boolean hasNull = false;
				boolean hasNamed = false;
				Map<String, ExpressionNode> namedArgumentsMap = new HashMap<>();
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
						namedArgumentsMap.put(name, argumentNode.getValue());
					}
				}
				if (hasNull && hasNamed){
					throw new RuntimeException("A argument list cannot mix named and unnamed arguments");
				}
				if (hasNull){
					List<ArgumentNode> arguments = node.getArguments();
					if (arguments.size() != parameters.size()){
						throw new RuntimeException("Invalid number of arguments");
					}
				}

				if (hasNamed){
					for (Declaration<Site> parameter : parameters){
						String parameterName = parameter.getName();
						ExpressionNode resolvedArgument = namedArgumentsMap.get(parameterName);
						if (resolvedArgument == null && !parameter.getType().isNullable()){
							failures.add(new ValidationFailure(node, "Parameter " + parameter + " is required"));
						}
						resolvedArguments.add(resolvedArgument);
					}
				}
				node.setResolvedArguments(resolvedArguments);
				Iterator<ExpressionNode> i = resolvedArguments.iterator();
				Iterator<Declaration<Site>> j = parameters.iterator();
				while (i.hasNext()){
					ExpressionNode argument = i.next();
					if (argument != null){
						Declaration parameter = j.next();
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
