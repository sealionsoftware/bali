package bali.compiler.validation.validator;

import bali.compiler.parser.tree.BreakStatementNode;
import bali.compiler.parser.tree.ContinueStatementNode;
import bali.compiler.parser.tree.LoopStatementNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.validation.ValidationFailure;

import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 08/07/13
 */
public class BranchStatementValidatorFactory implements ValidatorFactory {

	public Validator createValidator() {
		return new Validator() {
			public List<ValidationFailure> validate(Node node, Control control) {
				if (!(node instanceof LoopStatementNode)){
					control.validateChildren();
				}
				if (node instanceof BreakStatementNode) {
					return Collections.singletonList(new ValidationFailure(node, "Break statements cannot be used outside of loops"));
				}
				if (node instanceof ContinueStatementNode) {
					return Collections.singletonList(new ValidationFailure(node, "Continue statements cannot be used outside of loops"));
				}
				return Collections.emptyList();
			}
		};
	}
}
