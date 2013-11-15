package bali.compiler.validation.validator;

import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ConstructionExpressionNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.reference.Reference;
import bali.compiler.type.ParametrizedSite;
import bali.compiler.type.Site;
import bali.compiler.type.Type;
import bali.compiler.type.TypeLibrary;
import bali.compiler.type.VanillaSite;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Validates the Site declarations of constants, fields, variables, arguments etc
 * <p/>
 * Requires that the imports have been resolved
 * Sets the Site object on each typed site
 * <p/>
 * User: Richard
 * Date: 14/05/13
 */
public class TypeResolvingValidatorFactory implements ValidatorFactory {

	private TypeLibrary library;

	public TypeResolvingValidatorFactory(TypeLibrary library) {
		this.library = library;
	}

	public Validator createValidator() {

		return new Validator() {

			private Map<String, Reference<Type>> resolvables;

			public List<ValidationFailure> validate(Node node, Control control) {

				List<ValidationFailure> failures = Collections.emptyList();

				if (node instanceof CompilationUnitNode){
					resolvables = ((CompilationUnitNode) node).getResolvables();
				}

				control.validateChildren();

				if (node instanceof SiteNode){
					failures =  validate((SiteNode) node);
				} else if (node instanceof ConstructionExpressionNode){
					failures =  validate((ConstructionExpressionNode) node);
				}

				return failures;
			}

			public List<ValidationFailure> validate(SiteNode type) {

				List<ValidationFailure> ret = new ArrayList<>();
				Reference<Type> reference = resolvables.get(type.getClassName());

				if (reference == null) {
					try {
						reference = library.getReference(type.getClassName());
					} catch (Exception e) {
						ret.add(new ValidationFailure(type, "Cannot resolve type " + type));
						return ret;
					}
				}

				List<SiteNode> parameterNodes = type.getParameters();
				if (parameterNodes.isEmpty()){
					type.setSite(new VanillaSite(reference));
				}

				List<Site> parameterSites = new ArrayList<>(parameterNodes.size());
				for (SiteNode parameterNode : parameterNodes) {
					parameterSites.add(parameterNode.getSite());
				}

				type.setSite(new ParametrizedSite(reference, parameterSites));
				return ret;
			}

			public List<ValidationFailure> validate(ConstructionExpressionNode type) {

				List<ValidationFailure> ret = new ArrayList<>();
				Reference<Type> reference = resolvables.get(type.getClassName());

				if (reference == null) {
					try {
						reference = library.getReference(type.getClassName());
					} catch (Exception e) {
						ret.add(new ValidationFailure(type, "Cannot resolve type " + type));
						return ret;
					}
				}

				type.setType(new VanillaSite(reference));
				return ret;
			}
		};
	}
}
