package bali.compiler.type;

import bali.compiler.reference.Reference;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 13/09/13
 */
public class SiteSignatureVisitor extends SignatureVisitor {

	private TypeLibrary library;
	private Map<String, Site> typeVariableBounds;

	private Site site;

	private String className;
	private List<SiteSignatureVisitor> typeArgumentVisitors = new LinkedList<>();
	private boolean nullable;
	private boolean threadSafe;

	public SiteSignatureVisitor(TypeLibrary library, Map<String, Site> typeVariableBounds, Boolean nullable, Boolean threadSafe) {
		super(Opcodes.ASM4);
		this.library = library;
		this.typeVariableBounds = typeVariableBounds;
		this.nullable = nullable;
		this.threadSafe = threadSafe;
	}

	public void visitEnd() {

		if (className.equals(java.lang.Object.class.getName())){
			return;
		}

		Reference<Type> typeReference = library.getReference(className);

		List<Site> typeArguments = new LinkedList<>();
		for (SiteSignatureVisitor visitor : typeArgumentVisitors){
			typeArguments.add(visitor.getSite());
		}

		site = buildSite(typeReference, typeArguments);
		super.visitEnd();
	}

	private Site buildSite(Reference<Type> typeReference, List<Site> typeArguments){
		if (typeArguments.isEmpty()){
			return new VanillaSite(typeReference, nullable, threadSafe);
		} else {
			return new ParametrizedSite(typeReference, typeArguments, nullable, threadSafe);
		}
	}

	public void visitTypeVariable(String name) {
		site = new VariableSite(name, typeVariableBounds.get(name));
		super.visitTypeVariable(name);
	}

	public void visitTypeArgument() {
		super.visitTypeArgument();
	}

	public SignatureVisitor visitTypeArgument(char wildcard) {
		SiteSignatureVisitor visitor = new SiteSignatureVisitor(library, typeVariableBounds, false, false); //TODO Java 8 Type annotations so type arguments can be nullable, threadsafe
		typeArgumentVisitors.add(visitor);
		return visitor;
	}

	public SignatureVisitor visitClassBound() {
		return super.visitClassBound();
	}

	public SignatureVisitor visitInterfaceBound() {
		return super.visitInterfaceBound();
	}

	public SignatureVisitor visitParameterType() {
		return super.visitParameterType();
	}

	public void visitClassType(String name) {
		className = name.replaceAll("/", ".");
	}

	public void visitFormalTypeParameter(String name) {
		super.visitFormalTypeParameter(name);
	}

	public SignatureVisitor visitSuperclass() {
		return super.visitSuperclass();
	}

	public SignatureVisitor visitInterface() {
		return super.visitInterface();
	}

	public SignatureVisitor visitReturnType() {
		return super.visitReturnType();
	}

	public SignatureVisitor visitExceptionType() {
		return super.visitExceptionType();
	}

	public void visitBaseType(char descriptor) {
		super.visitBaseType(descriptor);
	}

	public SignatureVisitor visitArrayType() {
		return super.visitArrayType();
	}

	public void visitInnerClassType(String name) {
		super.visitInnerClassType(name);
	}

	public Site getSite() {
		return site;
	}
}
