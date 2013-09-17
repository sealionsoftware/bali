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

	private List<Declaration> typeParameters = new ArrayList<>();
	private List<Site> interfaces = new ArrayList<>();

	private Deque<SiteContext> typeParamStack = new LinkedList<>();

	public ClassSignatureVisitor() {
		super(Opcodes.ASM4);
	}

	public void visitFormalTypeParameter(String name) {

		typeParamStack.push(new SiteContext(name));

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

	public void visitTypeVariable(String name) {
		super.visitTypeVariable(name);
	}

	public void visitClassType(String name) {
		typeParamStack.peek().type = new Site(name, new ArrayList<>());
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
			typeParameters.add(new Declaration(context.name, context.type));
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
		private Site type;

	}

}
