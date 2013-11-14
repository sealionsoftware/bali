package bali.compiler.bytecode;

import bali.compiler.type.Declaration;
import bali.compiler.type.Method;
import bali.compiler.type.Operator;
import bali.compiler.type.Site;
import bali.compiler.type.Type;
import bali.compiler.type.UnaryOperator;

import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 08/09/13
 */
public class TestType extends Type {

	public TestType(String className) {
		this(className, Collections.<Declaration>emptyList());
	}

	public TestType(String className, List<Declaration> typeParameters) {
		this(className, typeParameters, Collections.<Site>emptyList());
	}

	public TestType(String className, List<Declaration> typeParameters, List<Site> interfaces) {
		this(className, typeParameters, interfaces, Collections.<Declaration>emptyList());
	}

	public TestType(String className, List<Declaration> typeParameters, List<Site> interfaces, List<Declaration> parameters) {
		this(className, typeParameters, interfaces, parameters, Collections.<Method>emptyList());
	}

	public TestType(String className, List<Declaration> typeParameters, List<Site> interfaces, List<Declaration> parameters, List<Method> methods) {
		this(className, typeParameters, interfaces, parameters, methods, Collections.<Operator>emptyList());
	}

	public TestType(String className, List<Declaration> typeParameters, List<Site> interfaces, List<Declaration> parameters, List<Method> methods, List<Operator> operators) {
		this(className, typeParameters, interfaces, parameters, methods, operators, Collections.<UnaryOperator>emptyList());
	}

	public TestType(String className, List<Declaration> typeParameters, List<Site> interfaces, List<Declaration> parameters, List<Method> methods, List<Operator> operators, List<UnaryOperator> unaryOperators) {
		this(className, typeParameters, interfaces, parameters, methods, operators, unaryOperators, Collections.<Declaration>emptyList());
	}

	public TestType(String className, List<Declaration> typeParameters, List<Site> interfaces, List<Declaration> parameters, List<Method> methods, List<Operator> operators, List<UnaryOperator> unaryOperators, List<Declaration> properties) {
		super(className, null, typeParameters, interfaces, parameters, methods, operators, unaryOperators, properties, false, false, false);
	}

	public boolean isAbstract() {
		return false;
	}

	public boolean equals(Object o) {
		if (o instanceof Type) {
			return this.getName().equals(((Type) o).getName());
		}
		return false;
	}
}
