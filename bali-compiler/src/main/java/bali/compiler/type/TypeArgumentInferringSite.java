package bali.compiler.type;

import bali.compiler.reference.SimpleReference;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 07/03/14
 */
public class TypeArgumentInferringSite implements Site {

	private Site delegate;
	private Class template;
	private Boolean nullable;
	private Boolean threadsafe;

	public TypeArgumentInferringSite(Class template, Boolean nullable, Boolean threadsafe) {
		this.template = template;
		this.nullable = nullable;
		this.threadsafe = threadsafe;
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
			List<Declaration<Type>> params = template.getTypeParameters();
			int noParams = params.size();
			if (noParams > 0){
				List<Site> typeArguments = t.getTypeArguments();
				if (noParams == typeArguments.size()){
					delegate = new ParameterisedSite(new SimpleReference<>(template), t.getTypeArguments(), nullable, threadsafe);
				} else {
					throw new RuntimeException("Could not infer Type Arguments. Context arguments: " + typeArguments + " required parameters: " + params);
				}
			} else {
				delegate = new ParameterisedSite(new SimpleReference<>(template), nullable, threadsafe);
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
		return template;
	}
}
