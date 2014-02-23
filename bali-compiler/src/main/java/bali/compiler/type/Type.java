package bali.compiler.type;

import java.util.List;

/**
 * User: Richard
 * Date: 21/09/13
 */
public interface Type {

	boolean isAssignableTo(Type t);

	Type getSuperType();

	List<Site> getTypeArguments();

	List<Declaration<Site>> getParameters();

	List<Method> getMethods();

	List<Type> getInterfaces();

	List<Operator> getOperators();

	List<UnaryOperator> getUnaryOperators();

	List<Declaration<Site>> getProperties();

	Class getTemplate();

}
