package bali.compiler.bytecode;

import bali.compiler.parser.tree.ArgumentDeclarationNode;
import bali.compiler.parser.tree.MethodNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.type.Class;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMConverter {

	public String getMethodDescriptor(Class returnClass, List<Class> argumentClasses) {
		Type[] argTypes = new Type[argumentClasses.size()];
		int i = 0;
		for (bali.compiler.type.Class argumentClass : argumentClasses) {
			argTypes[i++] = Type.getType(getTypeDescriptor(argumentClass));
		}
		return Type.getMethodType(Type.getType(getTypeDescriptor(returnClass)), argTypes).getDescriptor();
	}

	public String getMethodDescriptor(SiteNode returnClass, List<SiteNode> argumentClasses) {
		Type[] argTypes = new Type[argumentClasses.size()];
		int i = 0;
		for (SiteNode argumentClass : argumentClasses) {
			argTypes[i++] = Type.getType(getTypeDescriptor(argumentClass.getSite().getTemplate()));
		}
		return Type.getMethodType(Type.getType(getTypeDescriptor(returnClass)), argTypes).getDescriptor();
	}

	public String getMethodDescriptor(MethodNode method) {
		List<Class> argumentClasses = new ArrayList<>(method.getArguments().size());
		for (ArgumentDeclarationNode argument : method.getArguments()){
			argumentClasses.add(argument.getType().getSite().getTemplate());
		}
		SiteNode returnType = method.getType();
		return getMethodDescriptor(returnType != null ? returnType.getSite().getTemplate() : null, argumentClasses);
	}

	public String getTypeDescriptor(String className) {
		return (className == null ? Type.VOID_TYPE : Type.getObjectType(getInternalName(className))).getDescriptor();
	}

	public String getTypeDescriptor(SiteNode type) {
		return getTypeDescriptor(type != null ? type.getSite().getTemplate() : null);
	}

	public String getTypeDescriptor(Class aClass) {
		return (aClass == null ? Type.VOID_TYPE : Type.getObjectType(getInternalName(aClass.getName()))).getDescriptor();
	}

	public String getInternalName(String className) {
		return className.replaceAll("\\.", "/");
	}

	public String getInternalName(Class aClass) {
		return getInternalName(aClass.getName());
	}

	public String getInternalName(SiteNode type) {
		return getInternalName(type.getSite().getTemplate().getName());
	}

}
