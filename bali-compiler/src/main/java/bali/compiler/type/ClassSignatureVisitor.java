package bali.compiler.type;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Richard
 * Date: 03/09/13
 */
public class ClassSignatureVisitor extends SignatureVisitor {

	private TypeLibrary library;

	private List<Declaration> typeParameters = new ArrayList<>();
	private List<Site> interfaces = new ArrayList<>();

	private Deque<SiteContext> typeParamStack = new LinkedList<>();

	public ClassSignatureVisitor(TypeLibrary library) {
		super(Opcodes.ASM4);
		this.library = library;
	}

	public void visitFormalTypeParameter(String name) {

		typeParamStack.push(new SiteContext(name));

		super.visitFormalTypeParameter(name);
	}

	public SignatureVisitor visitClassBound() {
		SiteSignatureVisitor typeParameterVisitor = new SiteSignatureVisitor(library);
		typeParamStack.peek().typeVisitor = typeParameterVisitor;
		return typeParameterVisitor;
	}

	public SignatureVisitor visitInterfaceBound() {
		return visitClassBound();
	}

	public SignatureVisitor visitInterface() {
		return super.visitInterface();
	}

	public void visitTypeVariable(String name) {
		super.visitTypeVariable(name);
	}

	public void visitClassType(String name) {
		super.visitClassType(name);
	}

	public void visitTypeArgument() {
		super.visitTypeArgument();
	}

	public SignatureVisitor visitTypeArgument(char wildcard) {
		return super.visitTypeArgument(wildcard);
	}


	public void visitEnd() {
		for (SiteContext context : typeParamStack) {
			typeParameters.add(new Declaration(context.name, context.typeVisitor.getSite()));
		}
	}

	public List<Declaration> getTypeParameters() {
		return typeParameters;
	}

	public List<Site> getInterfaces() {
		return interfaces;
	}

	private class SiteContext {

		private SiteContext(String name) {
			this.name = name;
		}

		private String name;
		private SiteSignatureVisitor typeVisitor;

	}

}
