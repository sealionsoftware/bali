package bali.compiler.validation;

import bali.compiler.validation.type.Declaration;
import bali.compiler.validation.type.Site;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 03/09/13
 */
public class ClassSignatureVisitor extends SignatureVisitor {

	private List<Declaration> typeParameters = new ArrayList<>();
	private List<Site> interfaces = new ArrayList<>();



	public ClassSignatureVisitor() {
		super(Opcodes.ASM4);
	}

	public void visitFormalTypeParameter(String name) {
		super.visitFormalTypeParameter(name);
	}

	public SignatureVisitor visitClassBound() {
		return super.visitClassBound();
	}

	public SignatureVisitor visitInterfaceBound() {
		return super.visitInterfaceBound();
	}

	public SignatureVisitor visitInterface() {
		return super.visitInterface();
	}

//	public void visitTypeVariable(String name) {
//		super.visitTypeVariable(name);
//	}

//	public void visitClassType(String name) {
//		super.visitClassType(name);
//	}

//	public void visitTypeArgument() {
//		super.visitTypeArgument();
//	}

//	public SignatureVisitor visitTypeArgument(char wildcard) {
//		return super.visitTypeArgument(wildcard);
//	}


	public List<Declaration> getTypeParameters() {
		return typeParameters;
	}

	public List<Site> getInterfaces() {
		return interfaces;
	}
}
