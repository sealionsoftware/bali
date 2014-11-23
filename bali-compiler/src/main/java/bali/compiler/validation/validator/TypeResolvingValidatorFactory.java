package bali.compiler.validation.validator;

import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.parser.tree.TypeParameterNode;
import bali.compiler.reference.Reference;
import bali.compiler.type.Class;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.type.ParameterisedSite;
import bali.compiler.type.Site;
import bali.compiler.type.VariableSite;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

	public Visitor createValidator(final ClassLibrary library, final ConstantLibrary constantLibrary) {

		return new Visitor() {

			private Map<String, Reference<bali.compiler.type.Class>> resolvables;
			private Map<String, VariableSite> typeVariables = Collections.emptyMap();

			public List<ValidationFailure> validate(Node node, Control control) {

				List<ValidationFailure> failures = Collections.emptyList();

				if (node instanceof CompilationUnitNode){
					resolvables = ((CompilationUnitNode) node).getResolvables();
				} else if (node instanceof ClassNode){
					ClassNode classNode = (ClassNode) node;
					Map<String, VariableSite> typeVariables = new HashMap<>();
					for (TypeParameterNode typeParameterNode : classNode.getTypeParameters()){
						typeVariables.put(typeParameterNode.getName(), new VariableSite(
								typeParameterNode.getName(),
								null //TODO: get the bound?
						));
					}

					this.typeVariables = typeVariables;
				}

				control.validateChildren();

				if (node instanceof SiteNode){
					failures =  validate((SiteNode) node);
				}

				return failures;
			}

			public List<ValidationFailure> validate(SiteNode siteNode) {

				List<ValidationFailure> ret = new ArrayList<>();

				String name = siteNode.getClassName();
				Site site = typeVariables.get(name);

				if (site == null){
					Reference<Class> reference = resolvables.get(name);

					if (reference == null) {
						try {
							reference = library.getReference(name);
						} catch (Exception e) {
							ret.add(new ValidationFailure(siteNode, "Cannot resolve class " + name));
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

					site = new ParameterisedSite(reference, parameterSites, siteNode.getNullable(), siteNode.getThreadSafe());
				}

				siteNode.setSite(site);
				return ret;
			}
		};
	}
}
