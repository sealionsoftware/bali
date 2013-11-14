package bali.compiler.type;

import java.util.List;

/**
 * User: Richard
 * Date: 01/10/13
 */
public class ErasedSite implements Site {

	private Site replacement;
	private VariableSite replacedVariable;

	public ErasedSite(Site replacement, VariableSite replacedVariable) {
		this.replacement = replacement;
		this.replacedVariable = replacedVariable;
	}

	public boolean isAssignableTo(Site t) {
		return replacement.isAssignableTo(t);
	}

	public String getName() {
		return replacement.getName();
	}

	public Site getSuperType() {
		return replacement.getSuperType();
	}

	public List<Declaration> getTypeParameters() {
		return replacement.getTypeParameters();
	}

	public List<Declaration> getParameters() {
		return replacement.getParameters();
	}

	public List<Method> getMethods() {
		return replacement.getMethods();
	}

	public List<Site> getInterfaces() {
		return replacement.getInterfaces();
	}

	public List<Operator> getOperators() {
		return replacement.getOperators();
	}

	public List<UnaryOperator> getUnaryOperators() {
		return replacement.getUnaryOperators();
	}

	public List<Declaration> getProperties() {
		return replacement.getProperties();
	}

	public Type getType() {
		return replacement.getType();
	}

	public Type getBoundType(){
		return replacedVariable.getType();
	}

	public String toString() {
		return replacement.toString();
	}
}
