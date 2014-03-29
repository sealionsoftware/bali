package bali.compiler.type;

import java.util.List;

/**
 * User: Richard
 * Date: 27 Mar
 */
public class ErasedSite implements Site {

	private Site delegate;
	private Type erasure;

	public ErasedSite(Site delegate, Type erasure) {
		this.delegate = delegate;
		this.erasure = erasure;
	}

	public boolean isAssignableTo(Type t) {
		return delegate.isAssignableTo(t);
	}

	public List<Type> getSuperTypes() {
		return delegate.getSuperTypes();
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

	public Boolean isNullable() {
		return delegate.isNullable();
	}

	public Boolean isThreadSafe() {
		return delegate.isThreadSafe();
	}

	public boolean isAssignableTo(Site s) {
		return delegate.isAssignableTo(s);
	}

	public Type getErasure() {
		return erasure;
	}

	public String toString(){
		if (delegate == null){
			return null;
		}
		return delegate.toString();
	}
}
