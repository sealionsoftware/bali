package bali.compiler.validation.validator;

import bali.Exception;
import bali.compiler.parser.tree.CatchStatementNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.parser.tree.ThrowStatementNode;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ParameterisedSite;
import bali.compiler.type.Site;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Checks that throw statements are attempting to throw Throwable expressions
 * <p/>
 * User: Richard
 * Date: 07/07/13
 */
public class ThrowStatementValidatorFactory implements ValidatorFactory {

	private Site throwableType;

	public ThrowStatementValidatorFactory(ClassLibrary library) {
		throwableType = new ParameterisedSite(library.getReference(Exception.class.getName()));
	}

	public Validator createValidator() {
		return new Validator() {

			public List<ValidationFailure> validate(Node node, Control control) {
				List<ValidationFailure> ret = new ArrayList<>();
				if (node instanceof ThrowStatementNode){
					ret.addAll(validate((ThrowStatementNode) node));
				} else if (node instanceof CatchStatementNode){
					ret.addAll(validate((CatchStatementNode) node));
				}
				control.validateChildren();
				return ret;
			}

			public List<ValidationFailure> validate(ThrowStatementNode node) {
				ExpressionNode value = node.getValue();
				if (!value.getType().isAssignableTo(throwableType)){
					return Collections.singletonList(new ValidationFailure(
							value,
							"throw statement requires an argument that implements " + throwableType.getTemplate().getName()
					));
				}
				return Collections.emptyList();
			}

			public List<ValidationFailure> validate(CatchStatementNode node) {
				SiteNode type = node.getDeclaration().getType();
				List<ValidationFailure> ret = new ArrayList<>();
				if (!type.getSite().isAssignableTo(throwableType)){
					ret.add(new ValidationFailure(
							type,
							"catch block requires a parameter that implements " +  throwableType.getTemplate().getName()
					));
				}
				return ret;
			}


		};
	}



}
