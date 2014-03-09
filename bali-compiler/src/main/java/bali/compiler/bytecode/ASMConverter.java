package bali.compiler.bytecode;

import bali.compiler.type.Class;
import bali.compiler.type.Declaration;
import bali.compiler.type.Method;
import bali.compiler.type.Site;
import org.antlr.v4.parse.ANTLRParser;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.signature.SignatureWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMConverter {

	public String getMethodDescriptor(Method method) {
		List<Class> parameterClasses = new ArrayList<>();
		for (Declaration<Site> declaration : method.getParameters()){
			parameterClasses.add(declaration.getType().getTemplate());
		}
		Class returnClass = method.getType() != null ? method.getType().getTemplate() : null;
		return getMethodDescriptor(returnClass, parameterClasses);
	}

	public String getMethodDescriptor(Class returnClass, List<Class> parameterClasses) {
		Type[] parameterTypes = new Type[parameterClasses.size()];
		int i = 0;
		for (bali.compiler.type.Class argumentClass : parameterClasses) {
			parameterTypes[i++] = Type.getType(getTypeDescriptor(argumentClass));
		}
		return Type.getMethodType(Type.getType(getTypeDescriptor(returnClass)), parameterTypes).getDescriptor();
	}

	public String getTypeDescriptor(String className) {
		return (className == null ? Type.VOID_TYPE : Type.getObjectType(getInternalName(className))).getDescriptor();
	}

	public String getTypeDescriptor(Class aClass) {
		return getTypeDescriptor(aClass != null ? aClass.getName() : null);
	}

	public String getTypeDescriptor(bali.compiler.type.Type site) {
		return getTypeDescriptor(site.getTemplate());
	}

	public String getInternalName(String className) {
		return className.replaceAll("\\.", "/");
	}

	public String getInternalName(Class aClass) {
		return getInternalName(aClass.getName());
	}

	public String getSignature(Site site){
		SignatureWriter signatureWriter = new SignatureWriter();
		visit(signatureWriter, site);
		return signatureWriter.toString();
	}

	public String getMethodSignature(Method method){
		List<Site> parameterTypes = new ArrayList<>();
		for (Declaration<Site> parameter : method.getParameters()){
			parameterTypes.add(parameter.getType());
		}
		return getMethodSignature(method.getType(), parameterTypes);
	}

	public String getMethodSignature(Site returnType, List<Site> parameterTypes){
		SignatureWriter signatureWriter = new SignatureWriter();
		for (Site parameterType : parameterTypes){
			visit(signatureWriter.visitParameterType(), parameterType);
		}
		SignatureVisitor returnVisitor = signatureWriter.visitReturnType();
		if (returnType != null){
			visit(returnVisitor, returnType);
		} else {
			returnVisitor.visitBaseType('V');
		}
		return signatureWriter.toString();
	}

	//TODO - this is well short of complete
	private void visit(SignatureVisitor writer, Site site){
		writer.visitClassType(getInternalName(site.getTemplate().getName()));
		for (Site arg : site.getTypeArguments()){
			visit(writer.visitTypeArgument('='), arg);
		}
		writer.visitEnd();
	}

}
