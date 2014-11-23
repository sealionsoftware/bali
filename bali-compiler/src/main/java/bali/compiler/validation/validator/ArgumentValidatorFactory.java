package bali.compiler.validation.validator;

import bali.compiler.parser.tree.ArgumentNode;
import bali.compiler.parser.tree.ConstructionExpressionNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.InvocationNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.ParametrisedExpressionNode;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.type.Declaration;
import bali.compiler.type.Site;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This forms a pair with the InvocationValidator factory, except this works down the AST whilst the
 * other works upward
 *
 * User: Richard
 * Date: 25/05/13
 */
public class ArgumentValidatorFactory implements ValidatorFactory {

	public Visitor createValidator(ClassLibrary library, ConstantLibrary constantLibrary) {

		return new Visitor() {


			public List<ValidationFailure> validate(Node node, Control control) {

			    if (node instanceof InvocationNode){
					return validate((InvocationNode) node, control);
				} else if (node instanceof ConstructionExpressionNode){
					return validate((ConstructionExpressionNode) node, control);
				}

				control.validateChildren();

				return Collections.emptyList();
			}

			public List<ValidationFailure> validate(InvocationNode node, Control control) {
				return resolveArguments(node, node.getResolvedMethod().getParameters(), control);
			}

			public List<ValidationFailure> validate(ConstructionExpressionNode node, Control control) {
				return resolveArguments(node, node.getType().getParameters(), control);
			}

			private List<ValidationFailure> resolveArguments(ParametrisedExpressionNode node, List<Declaration<Site>> parameters, Control control){

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
					failures.add(new ValidationFailure(node, "An argument list cannot mix named and unnamed arguments"));
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
				control.validateChildren();
				return failures;
			}

		};
	}

}
