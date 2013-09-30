package bali.compiler.bytecode;

import bali.compiler.parser.tree.DeclarationNode;
import bali.compiler.parser.tree.MethodNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.type.Site;
import org.objectweb.asm.Type;

import java.util.List;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMConverter {

	public String getMethodDescriptor(Site returnClass, List<Site> argumentClasses) {
		Type[] argTypes = new Type[argumentClasses.size()];
		int i = 0;
		for (Site argumentClass : argumentClasses) {
			argTypes[i++] = Type.getType(getTypeDescriptor(argumentClass));
		}
		return Type.getMethodType(Type.getType(getTypeDescriptor(returnClass)), argTypes).getDescriptor();
	}

	public String getMethodDescriptor(SiteNode returnClass, List<SiteNode> argumentClasses) {
		Type[] argTypes = new Type[argumentClasses.size()];
		int i = 0;
		for (SiteNode argumentClass : argumentClasses) {
			argTypes[i++] = Type.getType(getTypeDescriptor(argumentClass));
		}
		return Type.getMethodType(Type.getType(getTypeDescriptor(returnClass)), argTypes).getDescriptor();
	}

	public String getMethodDescriptor(MethodNode method) {
		Type[] argTypes = new Type[method.getArguments().size()];
		int i = 0;
		for (DeclarationNode argument : method.getArguments()) {
			argTypes[i++] = Type.getType(getTypeDescriptor(argument.getType()));
		}
		return Type.getMethodType(Type.getType(getTypeDescriptor(method.getType())), argTypes).getDescriptor();
	}

	public String getTypeDescriptor(String className) {
		return (className == null ? Type.VOID_TYPE : Type.getObjectType(getInternalName(className))).getDescriptor();
	}

	public String getTypeDescriptor(SiteNode type) {
		return getTypeDescriptor(type != null ? type.getSite() : null);
	}

	public String getTypeDescriptor(Site type) {
		return (type == null ? Type.VOID_TYPE : Type.getObjectType(getInternalName(type.getName()))).getDescriptor();
	}

	public String getInternalName(String className) {
		return className.replaceAll("\\.", "/");
	}

	public String getInternalName(Site type) {
		return getInternalName(type.getName());
	}

	public String getInternalName(SiteNode type) {
		return getInternalName(type.getSite().getName());
	}

}
