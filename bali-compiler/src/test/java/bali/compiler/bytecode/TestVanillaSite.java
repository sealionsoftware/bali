package bali.compiler.bytecode;

import bali.compiler.type.VanillaSite;

/**
 * User: Richard
 * Date: 08/09/13
 */
public class TestVanillaSite extends VanillaSite {

	public TestVanillaSite(String name) {
		super(new TestType(name));
	}

	public TestVanillaSite(Class clazz) {
		super(new TestType(clazz.getName()));
	}

}
