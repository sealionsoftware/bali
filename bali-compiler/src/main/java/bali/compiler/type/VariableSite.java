package bali.compiler.type;

import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 21/09/13
 */
public class VariableSite implements Site {

	private String name;
	private Site bound;

	public VariableSite(String name, Site bound) {
		this.name = name;
		this.bound = bound;
	}

	public String getName() {
		return name;
	}

	public boolean isAssignableTo(Site t) {
		if (bound == null){
			return true;
		}
		return bound.isAssignableTo(t);
	}

	public List<Declaration> getTypeParameters() {
		if (bound == null){
			return Collections.emptyList();
		}
		return bound.getTypeParameters();
	}

	public List<Declaration> getParameters() {
		if (bound == null){
			return Collections.emptyList();
		}
		return bound.getParameters();
	}

	public List<Method> getMethods() {
		if (bound == null){
			return Collections.emptyList();
		}
		return bound.getMethods();
	}

	public List<Site> getInterfaces() {
		if (bound == null){
			return Collections.emptyList();
		}
		return bound.getInterfaces();
	}

	public List<Operator> getOperators() {
		if (bound == null){
			return Collections.emptyList();
		}
		return bound.getOperators();
	}

	public List<UnaryOperator> getUnaryOperators() {
		if (bound == null){
			return Collections.emptyList();
		}
		return bound.getUnaryOperators();
	}

	public List<Declaration> getProperties() {
		if (bound == null){
			return Collections.emptyList();
		}
		return bound.getProperties();
	}

	public Type getType() {
		if (bound == null){
			return null;
		}
		return bound.getType();
	}

	public Boolean getErase() {
		return true;
	}

	public String toString() {
		return name;
	}
}
