package bali.compiler.bytecode;

import bali.compiler.type.Class;
import bali.compiler.type.Declaration;
import bali.compiler.type.ErasedSite;
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

	private static String VOID_CLASSNAME = Object.class.getName();
	private static Class VOID_TEMPLATE = new MutableClassModel(VOID_CLASSNAME);

	public String getMethodDescriptor(Method method) {
		List<Site> parameterTypes = new ArrayList<>();
		for (Declaration<Site> declaration : method.getParameters()){
			parameterTypes.add(declaration.getType());
		}
		return getMethodDescriptor(method.getType(), parameterTypes);
	}

	public String getMethodDescriptor(bali.compiler.type.Type returnSite, List<? extends bali.compiler.type.Type> parameterSites) {
		List<Class> parameterClasses = new ArrayList<>();
		for (bali.compiler.type.Type parameterSite : parameterSites){
			parameterClasses.add(getErasure(parameterSite));
		}
		return getMethodDescriptorInternal(getErasure(returnSite), parameterClasses);
	}

	private String getMethodDescriptorInternal(Class returnClass, List<Class> parameterClasses) {
		Type[] parameterTypes = new Type[parameterClasses.size()];
		int i = 0;
		for (bali.compiler.type.Class argumentClass : parameterClasses) {
			parameterTypes[i++] = Type.getType(getTypeDescriptor(argumentClass));
		}
		Type returnType = returnClass == null ? Type.VOID_TYPE : Type.getType(getTypeDescriptor(returnClass));
		return Type.getMethodType(returnType, parameterTypes).getDescriptor();
	}

	public String getTypeDescriptor(String className) {
		return Type.getObjectType(getInternalName(className)).getDescriptor();
	}

	private String getTypeDescriptor(Class aClass) {
		return getTypeDescriptor(aClass != null ? aClass.getName() : null);
	}

	public String getTypeDescriptor(bali.compiler.type.Type site) {
		if (site == null){
			return getTypeDescriptor((Class) null);
		}
		return getTypeDescriptor(getErasure(site));
	}

	public String getInternalName(String className) {
		return (className != null ? className : VOID_CLASSNAME).replaceAll("\\.", "/");
	}

	public String getInternalName(Class aClass) {
		return getInternalName(aClass != null ? aClass.getName() : null);
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

	private Class getErasure(bali.compiler.type.Type site){
		if (site == null){
			return null;
		}
		if (site instanceof ErasedSite){
			ErasedSite erasedSite = (ErasedSite) site;
			site = erasedSite.getErasure();
			if (site == null){
				return VOID_TEMPLATE;
			}
		}
		Class template = site.getTemplate();
		if (template == null){
			return VOID_TEMPLATE;
		}
		return template;
	}

}
