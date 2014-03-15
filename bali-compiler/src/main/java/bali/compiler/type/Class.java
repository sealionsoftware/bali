package bali.compiler.type;

import bali.annotation.Kind;

import java.util.List;

/**
 * A Class is a compiled Type Template
 *
 * User: Richard
 * Date: 28/08/13
 */
public interface Class {

	public String getName();

	public List<Type> getSuperTypes();

	public List<Declaration<Type>> getTypeParameters();

	public List<Declaration<Site>> getParameters();

	public List<Operator> getOperators();

	public List<UnaryOperator> getUnaryOperators();

	public List<Declaration<Site>> getProperties();

	public List<Type> getInterfaces();

	public List<Method> getMethods();

	public Kind getMetaType();

	public Method getMethod(String name);

	public String toString();
}
