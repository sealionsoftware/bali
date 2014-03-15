package bali.compiler.type;

import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 10/03/14
 */
public class VoidType implements Type {

	public boolean isAssignableTo(Type t) {
		return false;
	}

	public List<Type> getSuperTypes() {
		return Collections.emptyList();
	}

	public List<Site> getTypeArguments() {
		return Collections.emptyList();
	}

	public List<Declaration<Site>> getParameters() {
		return Collections.emptyList();
	}

	public List<Method> getMethods() {
		return Collections.emptyList();
	}

	public List<Type> getInterfaces() {
		return Collections.emptyList();
	}

	public List<Operator> getOperators() {
		return Collections.emptyList();
	}

	public List<UnaryOperator> getUnaryOperators() {
		return Collections.emptyList();
	}

	public List<Declaration<Site>> getProperties() {
		return Collections.emptyList();
	}

	public Class getTemplate() {
		return new MutableClassModel(void.class.getName());
	}

	public Method getMethod(String name) {
		return null;
	}

	public String toString(){
		return "null";
	}
}
