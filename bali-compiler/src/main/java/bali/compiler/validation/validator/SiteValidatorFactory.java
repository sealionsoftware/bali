package bali.compiler.validation.validator;

import bali.collection.Collection;
import bali.compiler.parser.tree.CatchStatementNode;
import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.CodeBlockNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ConditionalStatementNode;
import bali.compiler.parser.tree.ControlExpressionNode;
import bali.compiler.parser.tree.DeclarationNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.ForStatementNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.ReferenceNode;
import bali.compiler.parser.tree.RunStatementNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.parser.tree.UnaryOperationNode;
import bali.compiler.parser.tree.VariableNode;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.type.CopySite;
import bali.compiler.type.Declaration;
import bali.compiler.type.Site;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class SiteValidatorFactory implements ValidatorFactory {

	public Validator createValidator() {
		return new Validator() {
			public List<ValidationFailure> validate(Node node, Control control) {
				List<ValidationFailure> failures = new ArrayList<>();
				if (node instanceof SiteNode){
					SiteNode siteNode = (SiteNode) node;
					if (!siteNode.getSite().getType().isAbstract()){
						failures.add(new ValidationFailure(
							siteNode,
							"Declared site must be of abstract types"
						));
					}
				}
				control.validateChildren();
				return failures;
			}
		};
	}


}
