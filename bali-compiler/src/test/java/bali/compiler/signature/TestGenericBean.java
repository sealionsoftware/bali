package bali.compiler.signature;

import java.util.Map;

/**
 * User: Richard
 * Date: 06/03/14
 */
public class TestGenericBean {

	public TestGenericBean(Map<String, Integer> property, java.lang.Integer property2) {
		this.property = property;
		this.property2 = property2;
	}

	public Map<String, Integer> property;
	public Integer property2;

}
