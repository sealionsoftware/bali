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

	public String getMethodDescriptor(bali.compiler.type.Type returnType, List<bali.compiler.type.Type> argumentTypes) {
		Type[] argTypes = new Type[argumentTypes.size()];
		int i = 0;
		for (bali.compiler.type.Type argumentType : argumentTypes) {
			argTypes[i++] = Type.getType(getTypeDescriptor(argumentType));
		}
		return Type.getMethodType(Type.getType(getTypeDescriptor(returnType)), argTypes).getDescriptor();
	}

	public String getMethodDescriptor(SiteNode returnClass, List<SiteNode> argumentClasses) {
		Type[] argTypes = new Type[argumentClasses.size()];
		int i = 0;
		for (SiteNode argumentClass : argumentClasses) {
			argTypes[i++] = Type.getType(getTypeDescriptor(argumentClass.getSite().getType()));
		}
		return Type.getMethodType(Type.getType(getTypeDescriptor(returnClass)), argTypes).getDescriptor();
	}

	public String getMethodDescriptor(MethodNode method) {
		Type[] argTypes = new Type[method.getArguments().size()];
		int i = 0;
		for (DeclarationNode argument : method.getArguments()) {
			argTypes[i++] = Type.getType(getTypeDescriptor(argument.getType().getSite().getType()));
		}
		return Type.getMethodType(Type.getType(getTypeDescriptor(method.getType())), argTypes).getDescriptor();
	}

	public String getTypeDescriptor(String className) {
		return (className == null ? Type.VOID_TYPE : Type.getObjectType(getInternalName(className))).getDescriptor();
	}

	public String getTypeDescriptor(SiteNode type) {
		return getTypeDescriptor(type != null ? type.getSite().getType() : null);
	}

	public String getTypeDescriptor(bali.compiler.type.Type type) {
		return (type == null ? Type.VOID_TYPE : Type.getObjectType(getInternalName(type.getName()))).getDescriptor();
	}

	public String getInternalName(String className) {
		return className.replaceAll("\\.", "/");
	}

	public String getInternalName(bali.compiler.type.Type type) {
		return getInternalName(type.getName());
	}

	public String getInternalName(SiteNode type) {
		return getInternalName(type.getSite().getName());
	}

}
