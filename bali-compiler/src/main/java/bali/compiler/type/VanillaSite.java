package bali.compiler.type;

import bali.compiler.reference.Reference;
import bali.compiler.reference.SimpleReference;

import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 21/09/13
 */
public class VanillaSite implements Site {

	private Reference<Type> type;
	private boolean nullable;
	private boolean threadSafe;

	public VanillaSite(Type type) {
		this(type, false, false);
	}

	public VanillaSite(Reference<Type> typeReference) {
		this(typeReference, false, false);
	}

	public VanillaSite(Type type, Boolean nullable, Boolean threadSafe) {
		this(new SimpleReference<>(type), nullable, threadSafe);
	}

	public VanillaSite(Reference<Type> typeReference, Boolean nullable, Boolean threadSafe) {
		this.type = typeReference;
		this.nullable = nullable;
		this.threadSafe = threadSafe;
	}

	public boolean isAssignableTo(Site t) {
		if (t == null) {
			return true;
		}

		if (nullable && !t.isNullable()){
			return false;
		}

		if (t.isThreadSafe() && !threadSafe){
			return false;
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

		Site superType = getSuperType();
		if (superType != null && getSuperType().isAssignableTo(t)){
			return true;
		}

		for (Site iface : getInterfaces()) {
			if (iface.isAssignableTo(t)) {
				return true;
			}
		}

		return false;
	}

	public boolean isNullable() {
		return nullable;
	}

	public boolean isThreadSafe() {
		return threadSafe;
	}

	public String getName() {
		return type.get().getName();
	}

	public Site getSuperType() {
		return type.get().getSuperType();
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

	public String toString() {
		StringBuilder sb = new StringBuilder(getName());
		if (nullable) {
			sb.append("?");
		}
		if (threadSafe) {
			sb.append("!");
		}
		return sb.toString();
	}
}
