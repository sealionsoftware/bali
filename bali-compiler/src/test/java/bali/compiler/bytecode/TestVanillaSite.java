package bali.compiler.bytecode;

import bali.compiler.reference.SimpleReference;
import bali.compiler.type.*;

import java.lang.Class;

/**
 * User: Richard
 * Date: 08/09/13
 */
public class TestVanillaSite extends ParameterisedSite {

	public TestVanillaSite(String name) {
		super(new SimpleReference<bali.compiler.type.Class>(new MutableClassModel(name)));
	}

	public TestVanillaSite(Class clazz) {
		this(clazz.getName());
	}

}
