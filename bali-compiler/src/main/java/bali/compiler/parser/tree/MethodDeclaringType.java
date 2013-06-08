package bali.compiler.parser.tree;

import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public abstract class MethodDeclaringType<T extends MethodDeclaration> extends Type {

	public MethodDeclaringType() {
	}

	public MethodDeclaringType(Integer line, Integer character) {
		super(line, character);
	}

	public abstract List<T> getMethods();

	public abstract void addMethod(T method);
}
