package bali.compiler.type;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 03/09/13
 */
public class ClassSignatureVisitor extends SignatureVisitor {

	private TypeLibrary library;

	private List<SiteSignatureVisitor> interfaceVisitors = new ArrayList<>();

	private Deque<SiteContext> typeParamStack = new LinkedList<>();

	private Map<String, Site> typeVariableBounds;

	public ClassSignatureVisitor(TypeLibrary library, Map<String, Site> typeVariableBounds) {
		super(Opcodes.ASM4);
		this.library = library;
		this.typeVariableBounds = typeVariableBounds;
	}

	public void visitFormalTypeParameter(String name) {

		typeParamStack.push(new SiteContext(name));

		super.visitFormalTypeParameter(name);
	}

	public SignatureVisitor visitClassBound() {
		SiteSignatureVisitor typeParameterVisitor = new SiteSignatureVisitor(library, typeVariableBounds);
		typeParamStack.peek().typeVisitor = typeParameterVisitor;
		return typeParameterVisitor;
	}

	public SignatureVisitor visitInterfaceBound() {
		return visitClassBound();
	}

	public SignatureVisitor visitInterface() {
		SiteSignatureVisitor interfaceVisitor = new SiteSignatureVisitor(library, typeVariableBounds);
		interfaceVisitors.add(interfaceVisitor);
		return interfaceVisitor;
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

	public List<Declaration> getTypeParameters() {
		List<Declaration> ret = new ArrayList<>(typeParamStack.size());
		for (SiteContext context : typeParamStack) {
			ret.add(new Declaration(context.name, context.typeVisitor.getSite()));
		}
		return ret;
	}

	public List<Site> getInterfaces() {
		List<Site> ret = new ArrayList<>(interfaceVisitors.size());
		for (SiteSignatureVisitor visitor : interfaceVisitors){
			ret.add(visitor.getSite());
		}
		return ret;
	}

	private class SiteContext {

		private SiteContext(String name) {
			this.name = name;
		}

		private String name;
		private SiteSignatureVisitor typeVisitor;

	}

}
