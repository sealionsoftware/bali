package bali.compiler.bytecode;

import bali.compiler.type.Site;
import bali.compiler.type.Type;

import java.util.ArrayList;
import java.util.Collections;

/**
 * User: Richard
 * Date: 08/09/13
 */
public class TestSite extends Site {

	public TestSite(String name) {
		super(new TestType(name), Collections.<Site>emptyList());
	}

	public TestSite(Class clazz) {
		super(new TestType(clazz.getName()), Collections.<Site>emptyList());
	}

}
