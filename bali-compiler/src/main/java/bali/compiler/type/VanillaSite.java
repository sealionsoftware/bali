package bali.compiler.type;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 21/09/13
 */
public class VanillaSite implements Site {

	private Reference<Type> type;

	public VanillaSite(Type type) {
		this.type = new Reference<>(type);
	}

	public VanillaSite(Reference<Type> typeReference) {
		this.type = typeReference;
	}

	public boolean isAssignableTo(Site t) {
		if (t == null) {
			return true;
		}

		if (getName().equals(t.getName())) {
			Iterator<Declaration> i = t.getParameters().iterator();
			for (Declaration argument : getParameters()) {
				Declaration parameter = i.next();
				if (!argument.getType().isAssignableTo(parameter.getType())) {
					return false;
				}
			}
			return true;
		}

		for (Site iface : getInterfaces()) {
			if (iface.isAssignableTo(t)) {
				return true;
			}
		}

		return false;
	}

	public String getName() {
		return type.get().getName();
	}

	public List<Declaration> getTypeParameters() {
		return type.get().getTypeParameters();
	}

	public List<Declaration> getParameters() {
		return type.get().getParameters();
	}

	public List<Method> getMethods() {
		return type.get().getMethods();
	}

	public List<Site> getInterfaces() {
		return type.get().getInterfaces();
	}

	public List<Operator> getOperators() {
		return type.get().getOperators();
	}

	public List<UnaryOperator> getUnaryOperators() {
		return type.get().getUnaryOperators();
	}

	public List<Declaration> getProperties() {
		return type.get().getProperties();
	}

	public Type getType() {
		return type.get();
	}

	public Boolean getErase() {
		return false;
	}

	public String toString() {
		return type.get().getName();
	}
}
