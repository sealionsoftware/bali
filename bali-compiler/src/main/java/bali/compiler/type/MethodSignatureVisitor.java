package bali.compiler.type;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 04/09/13
 */
public class MethodSignatureVisitor extends SignatureVisitor {

	private TypeLibrary library;

	private SiteSignatureVisitor returnTypeVisitor ;
	private List<SiteSignatureVisitor> parameterVisitors = new ArrayList<>();
	private Map<String, Site> typeVariableBounds;

	public MethodSignatureVisitor(TypeLibrary library, Map<String, Site> typeVariableBounds) {
		super(Opcodes.ASM4);
		this.library = library;
		this.typeVariableBounds = typeVariableBounds;
	}

	public SignatureVisitor visitReturnType() {
		returnTypeVisitor = new SiteSignatureVisitor(library, typeVariableBounds);
		return returnTypeVisitor;
	}

	public SignatureVisitor visitParameterType() {
		SiteSignatureVisitor visitor = new SiteSignatureVisitor(library, typeVariableBounds);
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
