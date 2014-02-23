package bali.compiler.validation.validator;

import bali.annotation.Kind;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ConstructionExpressionNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.reference.Reference;
import bali.compiler.type.*;
import bali.compiler.type.Class;
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

	private ClassLibrary library;

	public TypeResolvingValidatorFactory(ClassLibrary library) {
		this.library = library;
	}

	public Validator createValidator() {

		return new Validator() {

			private Map<String, Reference<bali.compiler.type.Class>> resolvables;

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

			public List<ValidationFailure> validate(SiteNode siteNode) {

				List<ValidationFailure> ret = new ArrayList<>();
				Reference<Class> reference = resolvables.get(siteNode.getClassName());

				if (reference == null) {
					try {
						reference = library.getReference(siteNode.getClassName());
					} catch (Exception e) {
						ret.add(new ValidationFailure(siteNode, "Cannot resolve type " + siteNode));
						return ret;
					}
				}

				List<SiteNode> parameterNodes = siteNode.getParameters();
				if (parameterNodes.isEmpty()){
					siteNode.setSite(new ParameterisedSite(reference, siteNode.getNullable(), siteNode.getThreadSafe()));
				}
				List<Site> parameterSites = new ArrayList<>(parameterNodes.size());
				for (SiteNode parameterNode : parameterNodes) {
					parameterSites.add(parameterNode.getSite());
				}

				siteNode.setSite(new ParameterisedSite(reference, parameterSites, siteNode.getNullable(), siteNode.getThreadSafe()));
				return ret;
			}

			public List<ValidationFailure> validate(ConstructionExpressionNode type) {

				List<ValidationFailure> ret = new ArrayList<>();
				Reference<Class> reference = resolvables.get(type.getClassName());

				if (reference == null) {
					try {
						reference = library.getReference(type.getClassName());
					} catch (Exception e) {
						ret.add(new ValidationFailure(type, "Cannot resolve type " + type));
						return ret;
					}
				}

				type.setType(new ParameterisedSite(reference, false, Kind.MONITOR.equals(reference.get().getMetaType())));
				return ret;
			}
		};
	}
}
