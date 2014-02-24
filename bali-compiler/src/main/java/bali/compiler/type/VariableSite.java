package bali.compiler.type;

/**
 * User: Richard
 * Date: 12/02/14
 */
public class VariableSite extends VariableType implements Site {

	public VariableSite(String name, Type bound) {
		super(name, bound);
	}

	public Boolean isNullable() {
		return false;
	}

	public Boolean isThreadSafe() {
		return false;
	}

	public boolean isAssignableTo(Site s) {
		return super.isAssignableTo(s);
	}
}
