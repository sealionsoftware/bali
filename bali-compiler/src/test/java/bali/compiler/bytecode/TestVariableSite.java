package bali.compiler.bytecode;

import bali.compiler.type.Site;
import bali.compiler.type.VariableSite;

/**
 * User: Richard
 * Date: 08/09/13
 */
public class TestVariableSite extends VariableSite {

	public TestVariableSite(String name) {
		super(name, null);
	}

	public TestVariableSite(String name, Site bound) {
		super(name, bound);
	}

}
