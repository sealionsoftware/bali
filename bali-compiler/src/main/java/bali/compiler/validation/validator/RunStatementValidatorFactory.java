package bali.compiler.validation.validator;

import bali.compiler.BaliCompiler;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.InvocationNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.ReferenceNode;
import bali.compiler.parser.tree.RunStatementNode;
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
public class RunStatementValidatorFactory implements ValidatorFactory {

	public Validator createValidator(ClassLibrary classLibrary, ConstantLibrary constantLibrary) {

		return new Validator(){

			private String unitName;
			private String sourceName;
			private List<RunStatementNode> runStatementNodes;
			private int runContextSize = 0;

			public List<ValidationFailure> validate(Node node, Control control) {

				List<ValidationFailure> failures = new ArrayList<>();

				if (node instanceof CompilationUnitNode){

					CompilationUnitNode compilationUnitNode = (CompilationUnitNode) node;
					unitName = compilationUnitNode.getName();
					sourceName = compilationUnitNode.getSourceFile();
					runStatementNodes = new ArrayList<>();
					control.validateChildren();
					compilationUnitNode.setRunStatements(runStatementNodes);

					return failures;

				} else if (node instanceof RunStatementNode){

					RunStatementNode runStatementNode = (RunStatementNode) node;
					runStatementNode.setRunnableClassName(unitName + ".RunStatement$" + runStatementNodes.size());
					runStatementNode.setSourceFileName(sourceName);

					runStatementNodes.add(runStatementNode);

					runContextSize++;

				} else if (node instanceof InvocationNode){
					if (runContextSize > 0){
						InvocationNode invocationNode = (InvocationNode) node;
						if (invocationNode.getTarget() == null){
							failures.add(new ValidationFailure(node, "Cannot call local methods from within a run block - they may not be thread-safe"));
						}
					}
				} else if (node instanceof ReferenceNode){
					if (runContextSize > 0){
						ReferenceNode referenceNode = (ReferenceNode) node;

						if (!ReferenceNode.ReferenceScope.VARIABLE.equals(referenceNode.getScope())){
							Site type = referenceNode.getType();
							if (!type.isThreadSafe()){
								failures.add(new ValidationFailure(node, "References to objects outside a run block must be thread-safe"));
							}
						}
					}
				}

				control.validateChildren();

				if (node instanceof RunStatementNode){
					runContextSize--;
				}

				return failures;
			}
		};
	}
}
