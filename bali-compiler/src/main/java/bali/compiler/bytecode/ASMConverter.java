package bali.compiler.bytecode;

import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.Method;
import bali.compiler.parser.tree.TypeReference;
import org.objectweb.asm.Type;

import java.util.List;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMConverter {

	public String getMethodDescriptor(TypeReference returnClass, List<TypeReference> argumentClasses) {
		Type[] argTypes = new Type[argumentClasses.size()];
		int i = 0;
		for (TypeReference argumentClass : argumentClasses) {
			argTypes[i++] = Type.getType(getTypeDescriptor(argumentClass));
		}
		return Type.getMethodType(Type.getType(getTypeDescriptor(returnClass)), argTypes).getDescriptor();
	}

	public String getMethodDescriptor(Method method) {
		Type[] argTypes = new Type[method.getArguments().size()];
		int i = 0;
		for (Declaration argument : method.getArguments()) {
			argTypes[i++] = Type.getType(getTypeDescriptor(argument.getType()));
		}
		return Type.getMethodType(Type.getType(getTypeDescriptor(method.getType())), argTypes).getDescriptor();
	}

	public String getTypeDescriptor(String className) {
		return className == null ? "V" : Type.getObjectType(getInternalName(className)).getDescriptor();
	}

	public String getTypeDescriptor(TypeReference type) {
		return (type == null ? Type.VOID_TYPE : Type.getObjectType(getInternalName(type.getDeclaration().getClassName()))).getDescriptor();
	}

	public String getInternalName(String className) {
		return className.replaceAll("\\.", "/");
	}

	public String getInternalName(TypeReference type) {
		return getInternalName(type.getDeclaration().getClassName());
	}

}
