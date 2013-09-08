package bali.compiler.bytecode;

import bali.compiler.validation.type.Declaration;
import bali.compiler.validation.type.Method;
import bali.compiler.validation.type.MethodDeclaringType;
import bali.compiler.validation.type.Site;

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
}
