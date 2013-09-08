package bali.compiler.bytecode;

import bali.compiler.validation.type.Site;
import bali.compiler.validation.type.Type;

import java.util.ArrayList;

/**
 * User: Richard
 * Date: 08/09/13
 */
public class TestSite extends Site<Type> {

	public TestSite(Class clazz) {
		super(new TestType(clazz.getName()), new ArrayList<Site>());
	}
}
