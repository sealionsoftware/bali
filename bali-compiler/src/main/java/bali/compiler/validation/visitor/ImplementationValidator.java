package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.DeclarationNode;
import bali.compiler.parser.tree.ImportNode;
import bali.compiler.parser.tree.InterfaceNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.validation.ValidationFailure;
import bali.compiler.validation.type.Declaration;
import bali.compiler.validation.type.Interface;
import bali.compiler.validation.type.Method;
import bali.compiler.validation.type.Site;
import bali.compiler.validation.type.Type;

import java.util.ArrayList;
import java.util.HashSet;
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
public class ImplementationValidator implements Validator<CompilationUnitNode> {

	public List<ValidationFailure> validate(CompilationUnitNode unit) {

		Set<Interface> interfaces = new HashSet<>();

		for (InterfaceNode iface : unit.getInterfaces()) {
			interfaces.add(iface.getResolvedType());
		}
		for (ImportNode iport : unit.getImports()) {
			Type iportType = iport.getType();
			if (iportType instanceof Interface) {
				interfaces.add((Interface) iportType);
			}
		}

		List<ValidationFailure> failures = new ArrayList<>();
		for (ClassNode clazz : unit.getClasses()) {
			failures.addAll(validate(clazz, interfaces));
		}
		return failures;

	}

	private List<ValidationFailure> validate(ClassNode classNode, Set<Interface> interfaces) {

		List<ValidationFailure> failures = new ArrayList<>();

		for (SiteNode<Interface> type : classNode.getImplementations()) {

			Site<Interface> ifaceSite = type.getSite();

			if (!interfaces.contains(ifaceSite.getType())) {
				failures.add(
						new ValidationFailure(classNode, "Implementation declaration " + type.getClassName() + " is not a recognised interface")
				);
				continue;
			}

			for (MethodDeclarationNode methodNode : classNode.getMethods()) {
				for (Interface iface : interfaces) {
					List<Site> argumentTypes = new ArrayList<>();
					for (DeclarationNode declarationNode : methodNode.getArguments()){
						argumentTypes.add(declarationNode.getType().getSite());
					}
					Method methodDeclaration = iface.getMethod(methodNode.getName(), argumentTypes);
					if (methodDeclaration != null) {
						methodNode.setDeclared(true);
						break;
					}
				}
			}

			for (Method method : ifaceSite.getMethods()) {
				List<Site> types = new ArrayList<>();
				for (Declaration declaration : method.getParameters()) {
					types.add(declaration.getType());
				}
				if (classNode.getResolvedType().getMethod(method.getName(), types) == null) {
					failures.add(
							new ValidationFailure(classNode, "Class " + classNode.getClassName() + " does not implement method " + method)
					);
				}
			}

		}
		return failures;
	}

}
