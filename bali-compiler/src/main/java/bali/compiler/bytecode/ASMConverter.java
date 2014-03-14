package bali.compiler.bytecode;

import bali.compiler.type.Class;
import bali.compiler.type.Declaration;
import bali.compiler.type.Method;
import bali.compiler.type.MutableClassModel;
import bali.compiler.type.Site;
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

	private static Class VOID_TEMPLATE = new MutableClassModel(Object.class.getName());

	public String getMethodDescriptor(Method method) {
		List<Site> parameterTypes = new ArrayList<>();
		for (Declaration<Site> declaration : method.getParameters()){
			parameterTypes.add(declaration.getType());
		}
		return getMethodDescriptor(method.getType(), parameterTypes);
	}

	public String getMethodDescriptor(Site returnSite, List<Site> parameterSites) {
		List<Class> parameterClasses = new ArrayList<>();
		for (Site parameterSite : parameterSites){
			parameterClasses.add(getErasure(parameterSite));
		}
		return getMethodDescriptor(getErasure(returnSite), parameterClasses);
	}

	private String getMethodDescriptor(Class returnClass, List<Class> parameterClasses) {
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
		if (site == null){
			return getTypeDescriptor((String) null);
		}
		Class template = site.getTemplate();
		return getTypeDescriptor(template != null ? template : VOID_TEMPLATE);
	}

	public String getInternalName(String className) {
		return className.replaceAll("\\.", "/");
	}

	public String getInternalName(Class aClass) {
		return getInternalName(aClass != null ? aClass.getName() : VOID_TEMPLATE.getName());
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
		Class clazz = site.getTemplate();
		String internalName = getInternalName(clazz);
		writer.visitClassType(getInternalName(internalName));
		for (Site arg : site.getTypeArguments()){
			visit(writer.visitTypeArgument('='), arg);
		}
		writer.visitEnd();
	}

	public Class getErasure(Site site){
		if (site == null){
			return null;
		}
		Class template = site.getTemplate();
		if (template == null){
			return VOID_TEMPLATE;
		}
		return template;
	}

}
