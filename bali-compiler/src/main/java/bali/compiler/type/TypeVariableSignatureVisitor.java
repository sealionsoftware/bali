package bali.compiler.type;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Richard
 * Date: 13/09/13
 */
public class TypeVariableSignatureVisitor extends SignatureVisitor {

	private Site<Type> uninitialisedSite;

	private String className;
	private List<Site> typeParameters = new LinkedList<>();

	public TypeVariableSignatureVisitor() {
		super(Opcodes.ASM4);
	}

	public void visitEnd() {
		uninitialisedSite = new Site(className, typeParameters);
		super.visitEnd();
	}

	public void visitTypeVariable(String name) {
		typeParameters.add(new Site(name, Collections.emptyList()));
		super.visitTypeVariable(name);
	}

	public void visitTypeArgument() {
		super.visitTypeArgument();
	}

	public SignatureVisitor visitTypeArgument(char wildcard) {
		return this;
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

	public Site<Type> getUninitialisedSite() {
		return uninitialisedSite;
	}
}
