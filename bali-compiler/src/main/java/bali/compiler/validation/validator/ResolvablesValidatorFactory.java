package bali.compiler.validation.validator;

import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ConstructionExpressionNode;
import bali.compiler.parser.tree.ImportNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.parser.tree.TypeNode;
import bali.compiler.reference.Reference;
import bali.compiler.reference.SimpleReference;
import bali.compiler.type.ParametrizedSite;
import bali.compiler.type.Site;
import bali.compiler.type.Type;
import bali.compiler.type.TypeLibrary;
import bali.compiler.type.VanillaSite;
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
public class ResolvablesValidatorFactory implements ValidatorFactory {

	private TypeLibrary library;

	public ResolvablesValidatorFactory(TypeLibrary library) {
		this.library = library;
	}

	public Validator createValidator() {

		return new Validator() {

			private Map<String, Reference<Type>> resolvables = new HashMap<>();

			public List<ValidationFailure> validate(Node node, Control control) {

				List<ValidationFailure> failures = Collections.emptyList();
				if (node instanceof CompilationUnitNode){
					control.validateChildren();
					((CompilationUnitNode) node).setResolvables(resolvables);
					return failures;
				} else if (node instanceof ImportNode){
					failures = validate((ImportNode) node);
				} else if (node instanceof TypeNode){
					failures = validate((TypeNode) node);
				}

				control.validateChildren();

				return failures;
			}

			public List<ValidationFailure> validate(TypeNode node){
				resolvables.put(node.getClassName(), library.getReference(node.getQualifiedClassName()));
				return Collections.emptyList();
			}

			// Engages at the root of the AST, constructs a lookup table of unqualified names to declarations
			public List<ValidationFailure> validate(ImportNode iport) {

				String name = iport.getName();
				Type iportType = iport.getType();
				if (iportType != null){
					resolvables.put(name.substring(name.lastIndexOf(".") + 1),  new SimpleReference<>(iport.getType()));
				}

				return Collections.emptyList();
			}
		};
	}
}
