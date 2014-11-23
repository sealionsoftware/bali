package bali.compiler.validation.validator;

import bali.annotation.Kind;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ImportNode;
import bali.compiler.parser.tree.InterfaceNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.ObjectNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.type.Class;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.type.Declaration;
import bali.compiler.type.Method;
import bali.compiler.type.Site;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Checks that classes implement their declared interfaces correctly
 * <p/>
 * Supplies method access modifiers
 * <p/>
 * User: Richard
 * Date: 14/05/13
 */
public class ImplementationValidatorFactory implements ValidatorFactory {

	public Visitor createValidator(final ClassLibrary library, final ConstantLibrary constantLibrary) {
		return new Visitor() {

			private Set<bali.compiler.type.Class> interfaceTypes;

			public List<ValidationFailure> validate(Node node, Control control) {

				List<ValidationFailure> ret;

				if (node instanceof CompilationUnitNode){
					ret = validate((CompilationUnitNode) node);
				} else if (node instanceof ObjectNode){
					ret = validate((ObjectNode) node);
				} else {
					ret = Collections.emptyList();
				}
				control.validateChildren();

				return ret;
			}

			public List<ValidationFailure> validate(CompilationUnitNode unit) {

				Set<Class> interfaces = new HashSet<>();

				for (InterfaceNode iface : unit.getInterfaces()) {
					interfaces.add(iface.getResolvedType());
				}
				for (ImportNode iport : unit.getImports()) {
					Class iportClass = iport.getType();
					if (iportClass != null && Kind.INTERFACE.equals(iportClass.getMetaType())) {
						interfaces.add(iportClass);
					}
				}

				this.interfaceTypes = interfaces;
				return Collections.emptyList();
			}

			private List<ValidationFailure> validate(ObjectNode objectNode) {

				List<ValidationFailure> failures = new ArrayList<>();

				for (MethodDeclarationNode methodNode : objectNode.getMethods()) {
					methodNode.setDeclared(getDeclaredMethod(objectNode, methodNode.getName()));
				}

				for (SiteNode type : objectNode.getImplementations()) {

					Site ifaceSite = type.getSite();

					if (!interfaceTypes.contains(ifaceSite.getTemplate())) {
						failures.add(
								new ValidationFailure(objectNode, "Implementation declaration " + type.getClassName() + " is not a recognised interface")
						);
						continue;
					}

					for (Method method : ifaceSite.getMethods()) {
						List<Site> ifaceMethodParameter = new ArrayList<>();
						for (Declaration<Site> parameterDeclaration : method.getParameters()) {
							ifaceMethodParameter.add(parameterDeclaration.getType());
						}
						Method implementation = objectNode.getResolvedType().getMethod(method.getName());
						if (implementation == null) {
							failures.add(
									new ValidationFailure(objectNode, "Class " + objectNode.getClassName() + " does not implement method " + method)
							);
							continue;
						}
						List<Declaration<Site>> implementationParameters = implementation.getParameters();
						if (implementationParameters.size() != ifaceMethodParameter.size()) {
							failures.add(
									new ValidationFailure(objectNode, "Method " + implementation.getName() + " has wrong number of arguments to implement interface declaration " + method)
							);
							continue;
						}
						Iterator<Site> ifaceParameters = ifaceMethodParameter.iterator();
						for (Declaration<Site> implementationParameter : implementationParameters){
							Site ifaceParameter = ifaceParameters.next();
							if (!implementationParameter.getType().isAssignableTo(ifaceParameter)){
								failures.add(
										new ValidationFailure(objectNode, "Implementation parameter " + implementationParameter.getName() + " is not assignable to declared parameter type " + ifaceParameter)
								);
							}
						}
						Site implementationReturn = implementation.getType();
						if (implementationReturn == null){
							if (method.getType() != null){
								failures.add(
										new ValidationFailure(objectNode, "Declared void method cannot be implemented with a return type ")
								);
							}
						} else if (!implementationReturn.isAssignableTo(method.getType())){
							failures.add(
									new ValidationFailure(objectNode, "Implementation return type " + implementation.getType() + " is not assignable to declared return type " + method.getType())
							);
						}
					}
				}
				return failures;
			}

			private Method getDeclaredMethod(ObjectNode objectNode, String name){
				for (SiteNode type : objectNode.getImplementations()){
					Method declared = type.getSite().getTemplate().getMethod(name);
					if (declared != null){
						return declared;
					}
				}
				return null;
			}
		};
	}

}
