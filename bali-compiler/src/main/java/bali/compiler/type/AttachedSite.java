package bali.compiler.type;

import java.util.List;

/**
 * User: Richard
 * Date: 19/02/14
 */
public class AttachedSite implements Site {

	private Site delegate;

	public AttachedSite(Site delegate) {
		this.delegate = delegate;
	}

	public Boolean isNullable() {
		return false;
	}

	public Boolean isThreadSafe() {
		return delegate.isThreadSafe();
	}

	public boolean isAssignableTo(Site t) {
		return delegate.isAssignableTo(t);
	}

	public boolean isAssignableTo(Type t) {
		return delegate.isAssignableTo(t);
	}

	public Type getSuperType() {
		return delegate.getSuperType();
	}

	public List<Site> getTypeArguments() {
		return delegate.getTypeArguments();
	}

	public List<Declaration<Site>> getParameters() {
		return delegate.getParameters();
	}

	public List<Method> getMethods() {
		return delegate.getMethods();
	}

	public List<Type> getInterfaces() {
		return delegate.getInterfaces();
	}

	public List<Operator> getOperators() {
		return delegate.getOperators();
	}

	public List<UnaryOperator> getUnaryOperators() {
		return delegate.getUnaryOperators();
	}

	public List<Declaration<Site>> getProperties() {
		return delegate.getProperties();
	}

	public Class getTemplate() {
		return delegate.getTemplate();
	}
}
