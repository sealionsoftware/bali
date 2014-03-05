package bali.compiler.bytecode;

import bali.compiler.type.Site;
import bali.compiler.type.Type;
import bali.compiler.type.VariableType;

/**
 * User: Richard
 * Date: 03/03/14
 */
public class TestVariableSite extends VariableType implements Site {

	public TestVariableSite(String name, Type bound) {
		super(name, bound);
	}

	public Boolean isNullable() {
		return false;
	}

	public Boolean isThreadSafe() {
		return false;
	}

	public boolean isAssignableTo(Site s) {
		return false;
	}
}
