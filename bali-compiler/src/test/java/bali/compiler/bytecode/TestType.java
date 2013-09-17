package bali.compiler.bytecode;

import bali.compiler.type.Declaration;
import bali.compiler.type.Method;
import bali.compiler.type.MethodDeclaringType;
import bali.compiler.type.Site;
import bali.compiler.type.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 08/09/13
 */
public class TestType extends MethodDeclaringType {

	public TestType(String className) {
		this(className, new ArrayList<Declaration>());
	}

	public TestType(String className, List<Declaration> parameters) {
		this(className, parameters, new ArrayList<Method>());
	}

	public TestType(String className, List<Declaration> parameters, List<Method> methods) {
		this(className, parameters, methods, new ArrayList<Site>());
	}

	public TestType(String className, List<Declaration> parameters, List<Method> methods, List<Site> interfaces) {
		super(className, parameters, methods, interfaces);
	}

	public boolean isAbstract() {
		return false;
	}


	public boolean equals(Object o) {
		if (o instanceof Type) {
			return this.getClassName().equals(((Type) o).getClassName());
		}
		return false;
	}
}
