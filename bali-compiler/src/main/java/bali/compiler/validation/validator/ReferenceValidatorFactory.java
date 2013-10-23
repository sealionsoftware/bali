package bali.compiler.validation.validator;

import bali.compiler.parser.tree.CatchStatementNode;
import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.CodeBlockNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.DeclarationNode;
import bali.compiler.parser.tree.ForStatementNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.ReferenceNode;
import bali.compiler.parser.tree.VariableNode;
import bali.compiler.type.ConstantLibrary;
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
public class ReferenceValidatorFactory implements ValidatorFactory {

	private ConstantLibrary library;

	public ReferenceValidatorFactory(ConstantLibrary library) {
		this.library = library;
	}

	public Validator createValidator() {
		return new Validator() {

			private Deque<Scope> scopeStack = new ArrayDeque<>();

			public List<ValidationFailure> validate(Node node, Control control) {
				if (node instanceof CompilationUnitNode) {
					validate((CompilationUnitNode) node, control);
				} else if (node instanceof ClassNode) {
					validate((ClassNode) node, control);
				} else if (node instanceof MethodDeclarationNode) {
					validate((MethodDeclarationNode) node, control);
				} else if (node instanceof ForStatementNode) {
					validate((ForStatementNode) node, control);
				} else if (node instanceof CatchStatementNode) {
					validate((CatchStatementNode) node, control);
				} else if (node instanceof CodeBlockNode) {
					validate((CodeBlockNode) node, control);
				} else if (node instanceof VariableNode) {
					validate((VariableNode) node, control);
				} else if (node instanceof ReferenceNode) {
					return validate((ReferenceNode) node);
				} else {
					control.validateChildren();
				}
				return Collections.emptyList();
			}

			public List<ValidationFailure> validate(CompilationUnitNode unit, Control control) {

				Deque<Scope> unitLevelScopes = new ArrayDeque<>();
				String pkgName = "_";
				List<Declaration> packageConstants = library.getConstants(pkgName);
				Scope scope = new Scope(
						ReferenceNode.ReferenceScope.STATIC,
						pkgName,
						true
				);
				for (Declaration declaration : packageConstants) {
					scope.add(declaration.getName(), declaration.getType());
				}
				unitLevelScopes.push(scope);

				String compilationUnitName = unit.getName();
				int i = -1;
				do {
					i = compilationUnitName.indexOf('.', i);
					pkgName = i > 0 ? compilationUnitName.substring(0, i) : compilationUnitName;
					packageConstants = library.getConstants(pkgName);
					scope = new Scope(
							ReferenceNode.ReferenceScope.STATIC,
							pkgName + "._",
							true
					);
					for (Declaration declaration : packageConstants) {
						scope.add(declaration.getName(), declaration.getType());
					}
					unitLevelScopes.push(scope);
				} while (i > 0);

				scopeStack = unitLevelScopes;
				control.validateChildren();
				return Collections.emptyList();
			}

			private void validate(ClassNode clazz, Control agent) {
				List<DeclarationNode> referenceableFields = new ArrayList<>();
				referenceableFields.addAll(clazz.getArgumentDeclarations());
				referenceableFields.addAll(clazz.getFields());

				Scope scope = new Scope(
						ReferenceNode.ReferenceScope.FIELD,
						clazz.getQualifiedClassName(),
						false
				);
				for (DeclarationNode field : referenceableFields) {
					scope.add(field.getName(), field.getType().getSite());
				}
				pushAndWalk(agent, scope);
			}

			private void validate(MethodDeclarationNode method, Control agent) {
				Scope scope = new Scope(
						ReferenceNode.ReferenceScope.VARIABLE,
						null,
						true
				);
				for (DeclarationNode declaration : method.getArguments()) {
					scope.add(declaration.getName(), declaration.getType().getSite());
				}
				pushAndWalk(agent, scope);
			}

			private void validate(ForStatementNode statement, Control agent) {
				Scope scope = new Scope(
						ReferenceNode.ReferenceScope.VARIABLE,
						null,
						true
				);
				DeclarationNode element = statement.getElement();
				scope.add(element.getName(), element.getType().getSite());
				pushAndWalk(agent, scope);
			}

			private void validate(CatchStatementNode statement, Control agent) {
				Scope scope = new Scope(
						ReferenceNode.ReferenceScope.VARIABLE,
						null,
						true
				);
				DeclarationNode element = statement.getDeclaration();
				scope.add(element.getName(), element.getType().getSite());
				pushAndWalk(agent, scope);
			}

			private void validate(CodeBlockNode codeBlock, Control agent) {
				Scope scope = new Scope(
						ReferenceNode.ReferenceScope.VARIABLE,
						null,
						false
				);
				pushAndWalk(agent, scope);
			}

			private void validate(VariableNode variable, Control control) {
				DeclarationNode declarationNode = variable.getDeclaration();
				scopeStack.peek().add(declarationNode.getName(), declarationNode.getType().getSite());
				control.validateChildren();
			}

			private void pushAndWalk(Control control, Scope scope) {
				scopeStack.push(scope);
				control.validateChildren();
				scopeStack.pop();
			}

			public List<ValidationFailure> validate(ReferenceNode value) {

				List<ValidationFailure> failures = new ArrayList<>();
				Site declaration = null;
				Scope declarationScope = null;

				for (Scope scope : scopeStack) {
					declaration = scope.find(value.getName());
					if (declaration != null) {
						declarationScope = scope;
						break;
					}
				}

				if (declaration == null) {
					failures.add(new ValidationFailure(value, "Could not resolve reference " + value.getName()));
					return failures;
				}

				value.setFinal(declarationScope.getFinal());
				value.setDeclaration(declaration);
				value.setHostClass(declarationScope.getClassName());
				value.setScope(declarationScope.getScope());
				return failures;
			}


		};
	}

	public static class Scope {

		private ReferenceNode.ReferenceScope scope;
		private String className;
		private Boolean isFinal;
		private Map<String, Site> declarations = new HashMap<>();

		public Scope(ReferenceNode.ReferenceScope scope, String className, Boolean isFinal) {
			this.scope = scope;
			this.className = className;
			this.isFinal = isFinal;
		}

		public ReferenceNode.ReferenceScope getScope() {
			return scope;
		}

		public String getClassName() {
			return className;
		}

		public Boolean getFinal() {
			return isFinal;
		}

		public void add(String name, Site type) {
			declarations.put(name, type);
		}

		public Site find(String name) {
			return declarations.get(name);
		}
	}


}
