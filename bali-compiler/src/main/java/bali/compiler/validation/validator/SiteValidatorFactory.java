package bali.compiler.validation.validator;

import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.ObjectNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.type.Class;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.type.Site;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class SiteValidatorFactory implements ValidatorFactory {

	public Visitor createValidator(final ClassLibrary library, final ConstantLibrary constantLibrary) {

		return new Visitor() {

			public boolean allowNonInterfaceTypes = false;

			public List<ValidationFailure> validate(Node node, Control control) {
				List<ValidationFailure> failures = new ArrayList<>();
				if (node instanceof SiteNode){

					SiteNode siteNode = (SiteNode) node;
					Site site = siteNode.getSite();
					Class aClass = site.getTemplate();

					if (aClass != null){
						if (!allowNonInterfaceTypes && !aClass.getMetaType().isReference()){
							failures.add(new ValidationFailure(
									siteNode,
									"Declared site must be of interface type"
							));
						}

						if (siteNode.getParameters().size() != aClass.getTypeParameters().size()){
							failures.add(new ValidationFailure(
									siteNode,
									"Invalid parameterisation: " + siteNode.getParameters() + " => " + aClass.getTypeParameters()
							));
						}
					}
					control.validateChildren();
				} else if (node instanceof ObjectNode){
					allowNonInterfaceTypes = true;
					control.validateChildren();
					allowNonInterfaceTypes = false;
				} else {
					control.validateChildren();
				}

				return failures;
			}
		};
	}


}
