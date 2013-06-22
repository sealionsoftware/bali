package bali.compiler.bytecode;

import bali.compiler.parser.tree.Method;
import bali.compiler.parser.tree.TypeDeclaration;

import java.util.List;

/**
 * User: Richard
 * Date: 19/06/13
 */
public class TestDeclaration extends TypeDeclaration<Method> {

	public TestDeclaration(String name) {
		setQualifiedClassName(name);
	}

	public List<Method> getMethods() {
		return null;
	}

	public void addMethod(Method method) {
	}

	public Boolean getAbstract() {
		return false;
	}
}
