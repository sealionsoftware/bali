package example;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/09/13
 */
public class HighlyGeneric<A,B> implements GenericInterface<A> {

	private List<A> as = new ArrayList<>();

	public A getAT() {
		return as.get(0);
	}

	public void doSomethingWithT(A a) {
		as.add(a);
	}

	public List<B> getLotsOfBs(HighlyGeneric<B, Integer> arg) {
		return arg.as;
	}
}
