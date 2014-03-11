package bali.compiler.validation.validator;

import bali.compiler.parser.tree.ArgumentNode;
import bali.compiler.parser.tree.AssignmentNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ConstructionExpressionNode;
import bali.compiler.parser.tree.FieldNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.ParametrisedExpressionNode;
import bali.compiler.reference.Reference;
import bali.compiler.type.Class;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.type.Declaration;
import bali.compiler.type.ParameterisedSite;
import bali.compiler.type.Site;
import bali.compiler.type.Type;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Validates the type arguments of constructor expressions from the surrounding context
 * <p/>
 * User: Richard
 * Date: 14/05/13
 */
public class TypeInferringValidatorFactory implements ValidatorFactory {

	public Validator createValidator(final ClassLibrary library, final ConstantLibrary constantLibrary) {

		return new Validator() {

			private Map<String, Reference<Class>> resolvables;
			private Deque<Site> contextSite = new LinkedList<>();

			public List<ValidationFailure> validate(Node node, Control control) {

				if (node instanceof CompilationUnitNode){
					resolvables = ((CompilationUnitNode) node).getResolvables();
				} else if (node instanceof AssignmentNode){
					return validate((AssignmentNode) node, control);
				} else if (node instanceof FieldNode){
					return validate((FieldNode) node, control);
				} else if (node instanceof ConstructionExpressionNode){
					return validate((ConstructionExpressionNode) node, control);
				} else if (node instanceof ArgumentNode){
					return validate((ArgumentNode) node, control);
				}

				control.validateChildren();

				return Collections.emptyList();
			}

			private void pushAndWalk(Control control, Site contextType){
				contextSite.push(contextType);
				control.validateChildren();
				contextSite.pop();
			}

			public List<ValidationFailure> validate(FieldNode fieldNode, Control control) {
				pushAndWalk(control, fieldNode.getType().getSite());
				return Collections.emptyList();
			}

			public List<ValidationFailure> validate(AssignmentNode assignmentNode, Control control) {
				pushAndWalk(control, assignmentNode.getType());
				return Collections.emptyList();
			}

			public List<ValidationFailure> validate(ArgumentNode argumentNode, Control control) {
				pushAndWalk(control, argumentNode.getResolvedType());
				return Collections.emptyList();
			}

			public List<ValidationFailure> validate(ConstructionExpressionNode type, Control control) {

				List<ValidationFailure> ret = new ArrayList<>();
				Reference<Class> reference = resolvables.get(type.getClassName());

				if (reference == null) {
					try {
						reference = library.getReference(type.getClassName());
					} catch (Exception e) {
						ret.add(new ValidationFailure(type, "Cannot resolve constructor class " + type.getClassName()));
						return ret;
					}
				}

				Class resolved = reference.get();
				if (resolved.getTypeParameters().size() == 0){
					type.setType(new ParameterisedSite(reference));
				} else {

					Site context = contextSite.peek();
					if (context == null){
						ret.add(new ValidationFailure(type, "No context to infer constructor type arguments from"));
						return ret;
					}
					Map<String, Site> contextArguments = new HashMap<>();
					Iterator<Declaration<Type>> i = context.getTemplate().getTypeParameters().iterator();
					Iterator<Site> j = context.getTypeArguments().iterator();
					while (i.hasNext() && j.hasNext()){
						contextArguments.put(
							i.next().getName(),
							j.next()
						);
					}

					//TODO: this will only work where the context type and constructor have the same type arg names -
					//really need to construct a mapping between the vars used in the context type and the replacements in the constructor type
					boolean fail = false;
					List<Site> typeArguments = new ArrayList<>();
					for(Declaration<Type> declaration : resolved.getTypeParameters()){
						Site inferred = contextArguments.get(declaration.getName());
						if (inferred == null){
							ret.add(new ValidationFailure(type, "Could not infer type argument for type parameter " + declaration.getName()));
							fail = true;
						}
						typeArguments.add(inferred);
					}
					if (fail){
						return ret;
					}

					type.setType(new ParameterisedSite(reference, typeArguments, false, resolved.getMetaType().isThreadSafe()));
				}

				control.validateChildren();

				return ret;
			}
		};


	}
}
