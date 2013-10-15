package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.DeclarationNode;
import bali.compiler.parser.tree.ImportNode;
import bali.compiler.parser.tree.InterfaceNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.type.Declaration;
import bali.compiler.type.Method;
import bali.compiler.type.Site;
import bali.compiler.type.Type;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
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
public class ImplementationValidator implements Validator {

	private Set<Type> interfaces;

	public List<ValidationFailure> validate(Node node, Control control) {
		if (node instanceof CompilationUnitNode){
			return validate((CompilationUnitNode) node);
		} else if (node instanceof ClassNode){
			return validate((ClassNode) node);
		}
		return Collections.emptyList();
	}

	public List<ValidationFailure> validate(CompilationUnitNode unit) {

		Set<Type> interfaces = new HashSet<>();

		for (InterfaceNode iface : unit.getInterfaces()) {
			interfaces.add(iface.getResolvedType());
		}
		for (ImportNode iport : unit.getImports()) {
			Type iportType = iport.getType();
			if (iportType.isAbstract()) {
				interfaces.add(iportType);
			}
		}

		this.interfaces = interfaces;
		return Collections.emptyList();
	}

	private List<ValidationFailure> validate(ClassNode classNode) {

		List<ValidationFailure> failures = new ArrayList<>();

		for (SiteNode type : classNode.getImplementations()) {

			Site ifaceSite = type.getSite();

			if (!interfaces.contains(ifaceSite.getType())) {
				failures.add(
						new ValidationFailure(classNode, "Implementation declaration " + type.getClassName() + " is not a recognised interface")
				);
				continue;
			}

			for (MethodDeclarationNode methodNode : classNode.getMethods()) {
				for (Type iface : interfaces) {
					List<Site> argumentTypes = new ArrayList<>();
					for (DeclarationNode declarationNode : methodNode.getArguments()) {
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

	public void onCompletion() {
	}

}
