package bali.compiler.type;

import java.util.List;

/**
 * User: Richard
 * Date: 21/09/13
 */
public interface Site {

	boolean isAssignableTo(Site t);

	String getName();

	List<Declaration> getTypeParameters();

	List<Declaration> getParameters();

	List<Method> getMethods();

	List<Site> getInterfaces();

	List<Operator> getOperators();

	List<UnaryOperator> getUnaryOperators();

	List<Declaration> getProperties();

	Type getType();

	Boolean getErase();

}
