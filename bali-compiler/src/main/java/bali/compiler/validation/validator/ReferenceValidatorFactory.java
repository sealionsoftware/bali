package bali.compiler.validation.validator;

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
					return validate((CompilationUnitNode) node, control);
				} else if (node instanceof ClassNode) {
					validate((ClassNode) node, control);
				} else if (node instanceof MethodDeclarationNode) {
					validate((MethodDeclarationNode) node, control);
				} else if (node instanceof CatchStatementNode) {
					validate((CatchStatementNode) node, control);
				} else if (node instanceof VariableNode) {
					validate((VariableNode) node, control);
				} else if (node instanceof ControlExpressionNode){

					Map<String, Site> originals = new HashMap<>();
					ControlExpressionNode cen = (ControlExpressionNode) node;
					for (Map.Entry<String, Site> typeOverride: cen.getTypeOverrides().entrySet() ){
						String reference = typeOverride.getKey();
						Scope overriddenScope = getScopeForReference(reference);
						Site overriddenSite = overriddenScope.find(reference);
						originals.put(reference, overriddenSite);
						overriddenScope.add(reference, typeOverride.getValue());
					}

					try {
						if (node instanceof ForStatementNode) {
							validate((ForStatementNode) node, control);
						} else if (node instanceof ConditionalStatementNode){
							validate((ConditionalStatementNode) node, control);
						} else if (node instanceof RunStatementNode) {
							validate((RunStatementNode) node, control);
						} else if (node instanceof CodeBlockNode) {
							validate((CodeBlockNode) node, control);
						} else {
							control.validateChildren();
						}
					} finally {
						for (Map.Entry<String, Site> typeOverride: originals.entrySet()){
							String reference = typeOverride.getKey();
							Scope overriddenScope = getScopeForReference(reference);
							overriddenScope.add(reference, typeOverride.getValue());
						}
					}

				} else if (node instanceof ReferenceNode) {
					control.validateChildren();
					return validate((ReferenceNode) node);
				} else {
					control.validateChildren();
				}
				return Collections.emptyList();
			}

			public List<ValidationFailure> validate(CompilationUnitNode unit, Control control) {

				Deque<Scope> unitLevelScopes = new ArrayDeque<>();
				String pkgName = "_";
				try {
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
				} catch (Exception e){
					return Collections.singletonList(new ValidationFailure(unit, "Could not load constants from " + pkgName + ": " + e.getMessage()));
				}

				String compilationUnitName = unit.getName();
				int i = -1;
				do {
					i = compilationUnitName.indexOf('.', i);
					pkgName = i > 0 ? compilationUnitName.substring(0, i) : compilationUnitName;
					List<Declaration> packageConstants = library.getConstants(pkgName);
					Scope scope = new Scope(
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

			private void validate(ConditionalStatementNode statement, Control agent) {

				ExpressionNode condition = statement.getCondition();
				if (condition instanceof UnaryOperationNode){
					UnaryOperationNode unary = (UnaryOperationNode) condition;
					if (unary.getOperator().equals(UnaryOperationValidatorFactory.NULL_CHECK_OPERATOR_NAME)){
						ExpressionNode target = unary.getTarget();
						if (target instanceof ReferenceNode){
							ReferenceNode referenceNode = (ReferenceNode) target;
							String name = referenceNode.getName();
							Site original = getSiteForReference(name);
							Site newSite = new CopySite(
									original.getType(),
									original.getSuperType(),
									original.getTypeParameters(),
									original.getInterfaces(),
									original.getParameters(),
									original.getMethods(),
									original.getOperators(),
									original.getUnaryOperators(),
									original.getProperties(),
									false,
									original.isThreadSafe()
							);
							ControlExpressionNode conditional = statement.getConditional();
							conditional.overrideType(name, newSite);
						}
					}
				}

				agent.validateChildren();

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

			private void validate(RunStatementNode statement, Control agent) {
				Scope scope = new Scope(
						ReferenceNode.ReferenceScope.FIELD,
						statement.getRunnableClassName(),
						true
				);

				for (Scope superScope : scopeStack){
					if (!ReferenceNode.ReferenceScope.STATIC.equals(superScope.getScope())){
						for (Declaration declaration : superScope.getDeclarations()){
							scope.add(declaration.getName(), declaration.getType());
						}
					}
				}

				pushAndWalk(agent, scope);

				List<RunStatementNode.RunArgument> arguments = new ArrayList<>();
				for (Declaration declaration : scope.getDeclarations()){

					Scope originalScope = getScopeForReference(declaration.getName());
					arguments.add(new RunStatementNode.RunArgument(
							declaration.getName(),
							declaration.getType(),
							originalScope.getScope(),
							originalScope.getClassName()
					));
				}
				statement.setArguments(arguments);
			}

			private Scope getScopeForReference(String name){
				for (Scope parentScope : scopeStack) {
					if (parentScope.find(name) != null) {
						return parentScope;
					}
				}
				return null;
			}

			private Site getSiteForReference(String name){
				for (Scope parentScope : scopeStack) {
					Site site = parentScope.find(name);
					if (site != null) {
						return site;
					}
				}
				return null;
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
				ExpressionNode target = value.getTarget();

				if (target == null){

					Scope declarationScope = getScopeForReference(value.getName());

					if (declarationScope == null) {
						failures.add(new ValidationFailure(value, "Could not resolve reference " + value.getName()));
						return failures;
					}

					value.setFinal(declarationScope.getFinal());
					value.setDeclaration( declarationScope.find(value.getName()));
					value.setHostClass(declarationScope.getClassName());
					value.setScope(declarationScope.getScope());
					value.setGetterName(null);

				} else {
					String name = value.getName();
					Site site = target.getType();
					PropertyData propertyData = getProperty(site, name);
					if (propertyData == null){
						failures.add(new ValidationFailure(value, "Could not resolve reference " + site.getType().getName() + "." + name));
						return failures;
					}
					value.setFinal(false);
					value.setDeclaration(propertyData.getProperty().getType());
					value.setHostClass(propertyData.getHostClass());
					value.setScope(ReferenceNode.ReferenceScope.FIELD);
					value.setGetterName("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
				}

				return failures;
			}
		};
	}

	private PropertyData getProperty(Site site, String name){
		for (Declaration p : site.getProperties())
			if(p.getName().equals(name))
				return new PropertyData(p, site.getType().getName());
		Site superType = site.getSuperType();
		if (superType != null)
			return getProperty(superType, name);
		return null;
	}

	private static class PropertyData {

		private Declaration property;
		private String hostClass;

		public PropertyData(Declaration property, String hostClass) {
			this.property = property;
			this.hostClass = hostClass;
		}

		private Declaration getProperty() {
			return property;
		}

		private String getHostClass() {
			return hostClass;
		}
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

		public List<Declaration> getDeclarations() {
			List<Declaration> ret = new ArrayList<>();
			for (Map.Entry<String, Site> entry : declarations.entrySet()){
				ret.add(new Declaration(entry.getKey(), entry.getValue()));
			}
			return ret;
		}
	}


}
