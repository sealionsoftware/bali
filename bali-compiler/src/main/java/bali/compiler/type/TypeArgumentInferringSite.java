package bali.compiler.type;

import bali.compiler.reference.Reference;

import java.util.List;

/**
 * User: Richard
 * Date: 07/03/14
 */
public class TypeArgumentInferringSite implements Site {

	private Site delegate;
	private Reference<Class> template;

	public TypeArgumentInferringSite(Reference<Class> template) {
		this.template = template;
	}

	public Boolean isNullable() {
		return delegate.isNullable();
	}

	public Boolean isThreadSafe() {
		return delegate.isThreadSafe();
	}

	public boolean isAssignableTo(Site s) {
		return isAssignableTo((Type) s);
	}

	//TODO - this needs to be much tighter
	public boolean isAssignableTo(Type t) {
		if (delegate == null){
			Class referenced = template.get();
			boolean threadsafe = referenced.getMetaType().isThreadSafe();
			List<Declaration<Type>> params = template.get().getTypeParameters();
			int noParams = params.size();
			if (noParams > 0){
				List<Site> typeArguments = t.getTypeArguments();
				if (noParams == typeArguments.size()){
					delegate = new ParameterisedSite(template, t.getTypeArguments(), false, threadsafe);
				} else {
					throw new RuntimeException("Could not infer Type Arguments. Context arguments: " + typeArguments + " required parameters: " + params);
				}
			} else {
				delegate = new ParameterisedSite(template, false, threadsafe);
			}
		}
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
		return template.get();
	}

	public String toString() {
		return delegate.toString();
	}
}
