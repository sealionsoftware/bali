package bali.compiler.bytecode;

import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.MethodDeclaration;
import org.objectweb.asm.Type;

import java.util.List;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMConverter {

	public String getMethodDescriptor(String returnClass, List<String> argumentClasses) {
		Type[] argTypes = new Type[argumentClasses.size()];
		int i = 0;
		for (String argumentClass : argumentClasses){
			argTypes[i++] = Type.getType(getTypeDescriptor(argumentClass));
		}
		return Type.getMethodType(Type.getType(getTypeDescriptor(returnClass)), argTypes).getDescriptor();
	}

	public String getMethodDescriptor(MethodDeclaration method) {
		Type[] argTypes = new Type[method.getArguments().size()];
		int i = 0;
		for (Declaration argument : method.getArguments()){
			argTypes[i++] = Type.getType(getTypeDescriptor(argument.getType()));
		}
		return Type.getMethodType(Type.getType(getTypeDescriptor(method.getType())), argTypes).getDescriptor();
	}

	public String getTypeDescriptor(String className){
		return className == null ? "V" : Type.getObjectType(getInternalName(className)).getDescriptor();
	}

	public String getTypeDescriptor(bali.compiler.parser.tree.Type type){
		return (type == null ? Type.VOID_TYPE : Type.getObjectType(getInternalName(type.getQualifiedClassName()))).getDescriptor();
	}

	public String getInternalName(String className){
		return className.replaceAll("\\.","/");
	}

}
