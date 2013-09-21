package bali.compiler.type;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 04/09/13
 */
public class MethodSignatureVisitor extends SignatureVisitor {

	private TypeLibrary library;

	private SiteSignatureVisitor returnTypeVisitor ;
	private List<SiteSignatureVisitor> parameterVisitors = new ArrayList<>();

	public MethodSignatureVisitor(TypeLibrary library) {
		super(Opcodes.ASM4);
		this.library = library;
	}

	public SignatureVisitor visitReturnType() {
		returnTypeVisitor = new SiteSignatureVisitor(library);
		return returnTypeVisitor;
	}

	public SignatureVisitor visitParameterType() {
		SiteSignatureVisitor visitor = new SiteSignatureVisitor(library);
		parameterVisitors.add(visitor);
		return visitor;
	}

	public Site getReturnType() {
		return returnTypeVisitor.getSite();
	}

	public List<Site> getParameterTypes() {
		List<Site> ret = new ArrayList<>();
		for (SiteSignatureVisitor visitor : parameterVisitors ){
			ret.add(visitor.getSite());
		}
		return ret;
	}
}
