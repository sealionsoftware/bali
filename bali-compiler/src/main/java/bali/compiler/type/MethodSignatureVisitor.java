package bali.compiler.type;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * User: Richard
 * Date: 04/09/13
 */
public class MethodSignatureVisitor extends SignatureVisitor {

	private TypeLibrary library;

	private SiteSignatureVisitor returnTypeVisitor ;
	private List<SiteSignatureVisitor> parameterVisitors = new ArrayList<>();
	private Map<String, Site> typeVariableBounds;
	private TypeData returnData;
	private Queue<TypeData> parameterData;

	public MethodSignatureVisitor(TypeLibrary library, Map<String, Site> typeVariableBounds, TypeData returnData, List<? extends TypeData> parameterData) {
		super(Opcodes.ASM4);
		this.library = library;
		this.typeVariableBounds = typeVariableBounds;
		this.returnData = returnData;
		this.parameterData = new LinkedList<>(parameterData);
	}

	public SignatureVisitor visitReturnType() {
		returnTypeVisitor = new SiteSignatureVisitor(library, typeVariableBounds, returnData.nullable, returnData.threadSafe);
		return returnTypeVisitor;
	}

	public SignatureVisitor visitParameterType() {
		TypeData data = parameterData.poll();
		SiteSignatureVisitor visitor = new SiteSignatureVisitor(library, typeVariableBounds, data.nullable, data.threadSafe);
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
