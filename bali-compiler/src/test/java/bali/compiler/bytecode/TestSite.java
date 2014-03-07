package bali.compiler.bytecode;

import bali.compiler.reference.SimpleReference;
import bali.compiler.type.*;

import java.lang.Class;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 08/09/13
 */
public class TestSite extends ParameterisedSite {

	public TestSite(Class template) {
		this(template, Collections.<Site>emptyList());
	}

	public TestSite(Class template, List<Site> typeArguments) {
		super(new SimpleReference<bali.compiler.type.Class>(new TestClass(template)), typeArguments, false, false);
	}

	public boolean equals(Object o){
		return o instanceof Site && ((Site) o).getTemplate().equals(getTemplate());
	}
}
