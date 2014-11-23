package bali.compiler.validation.validator;

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
import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 25/05/13
 */
public class InvocationValidatorFactory implements ValidatorFactory {

	public Visitor createValidator(ClassLibrary library, ConstantLibrary constantLibrary) {

		return new Visitor() {

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
				node.setResolvedMethod(method);

				ret.addAll(checkArguments(node, method.getParameters(), control));
				return ret;
			}

			public List<ValidationFailure> validate(ConstructionExpressionNode node, Control control) {

				List<ValidationFailure> ret = new ArrayList<>();
				Type expressionType = node.getType();

				if (!expressionType.getTemplate().getMetaType().isConstructable()) {
					ret.add(new ValidationFailure(node, "Cannot instanciate an interface type"));
					return ret;
				}

				ret.addAll(checkArguments(node, expressionType.getParameters(), control));
				return ret;

			}

			private List<ValidationFailure> checkArguments(ParametrisedExpressionNode node, List<Declaration<Site>> parameters, Control control){

				List<ExpressionNode> resolvedArguments = node.getResolvedArguments();
				List<ValidationFailure> failures = new ArrayList<>();

				control.validateChildren();
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
